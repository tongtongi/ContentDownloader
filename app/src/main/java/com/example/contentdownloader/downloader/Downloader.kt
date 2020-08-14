package com.example.contentdownloader.downloader

import android.util.Log
import com.example.contentdownloader.BuildConfig
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.*
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.Proxy
import java.net.URL
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 *  Helper class for downloading the given url
 */
object Downloader {

    private val client by lazy { clientBuilder() }

    private fun clientBuilder(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)

        okHttpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            val request = requestBuilder.build()
            chain.proceed(request)
        }

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
            okHttpClient.addInterceptor(logging)
        }

        return okHttpClient.build()
    }

    suspend fun downloadOrResume(
        url: String, destination: File,
        onProgress: ((percent: Int, downloaded: Long, total: Long) -> Unit)? = null
    ) {
        var headers: Pair<String, String>? = null

        var startingFrom = 0L
        if (destination.exists() && destination.length() > 0L) {
            startingFrom = destination.length()
            headers = Pair("Range", "bytes=${startingFrom}-")
        }

        download(url, destination, headers, onProgress)
    }

    private suspend fun download(
        url: String,
        destination: File,
        headers: Pair<String, String>? = null,
        onProgress: ((percent: Int, downloaded: Long, total: Long) -> Unit)? = null
    ) {

        val request = if (headers == null) {
            Request.Builder().url(url).tag(url).build()
        } else {
            Request.Builder().url(url)
                .addHeader(headers.first, headers.second).tag(url).build()
        }

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("ERROR", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                handleDownloadResponse(response, destination, onProgress)
            }
        })
    }

    fun handleDownloadResponse(
        response: Response,
        destination: File,
        onProgress: ((percent: Int, downloaded: Long, total: Long) -> Unit)?
    ) {

        var startingByte = 0L
        var endingByte = 0L
        var totalBytes = 0L

        val contentLength = response.body!!.contentLength()

        if (response.code == 206) {
            // 206: Continue download
            val matcher = Pattern.compile("bytes ([0-9]*)-([0-9]*)/([0-9]*)")
                .matcher(response.headers["Content-Range"].toString())
            if (matcher.find()) {
                startingByte = matcher.group(1)?.toLong() ?: 0L
                endingByte = matcher.group(2)?.toLong() ?: 0L
                totalBytes = matcher.group(3)?.toLong() ?: 0L
            }
        } else {
            endingByte = contentLength
            totalBytes = contentLength
            if (destination.exists()) {
                destination.delete()
            }
        }

        val sink = if (startingByte > 0) {
            destination.appendingSink().buffer()
        } else {
            destination.sink().buffer()
        }

        var lastPercentage = -1
        var totalRead = startingByte
        sink.use {
            it.writeAll(object : ForwardingSource(response.body!!.source()) {

                override fun read(sink: Buffer, byteCount: Long): Long {
                    val bytesRead = super.read(sink, byteCount)

                    totalRead += bytesRead

                    val currentPercentage = (totalRead * 100 / totalBytes).toInt()
                    if (currentPercentage > lastPercentage) {
                        lastPercentage = currentPercentage
                        if (onProgress != null) {
                            onProgress(currentPercentage, totalRead, totalBytes)
                        }
                    }
                    return bytesRead
                }
            })
        }
    }

    fun stop(url: String) {
        for (call in client.dispatcher.runningCalls()) {
            if (call.request().tag() == url) {
                call.cancel()
            }
        }
    }

    fun getExpandedUrl(shortenedUrl: String): String {
        val url = URL(shortenedUrl)
        val httpURLConnection: HttpURLConnection =
            url.openConnection(Proxy.NO_PROXY) as HttpURLConnection
        httpURLConnection.instanceFollowRedirects = false
        val expandedURL: String? = httpURLConnection.getHeaderField("Location")
        httpURLConnection.disconnect()
        return expandedURL ?: shortenedUrl
    }
}