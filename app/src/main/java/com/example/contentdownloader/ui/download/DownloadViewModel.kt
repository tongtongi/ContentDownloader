package com.example.contentdownloader.ui.download

import android.webkit.URLUtil
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contentdownloader.domain.AddContentUseCase
import com.example.contentdownloader.domain.DeleteContentUseCase
import com.example.contentdownloader.domain.IsContentExistsUseCase
import com.example.contentdownloader.domain.LoadAllContentsUseCase
import com.example.contentdownloader.downloader.Downloader
import com.example.contentdownloader.ui.model.ContentUIModel
import com.example.contentdownloader.ui.model.toEntityModel
import com.example.contentdownloader.ui.model.toUIModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class DownloadViewModel @ViewModelInject constructor(
    private val addContentUseCase: AddContentUseCase,
    private val loadAllContentsUseCase: LoadAllContentsUseCase,
    private val deleteContentUseCase: DeleteContentUseCase,
    private val isContentExistsUseCase: IsContentExistsUseCase
) : ViewModel() {

    private val _contentList = MutableLiveData<List<ContentUIModel>>()
    val contentList: LiveData<List<ContentUIModel>> = _contentList

    init {
        loadAllContent()
    }

    fun addToList(path: String, url: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val realUrl = Downloader.getExpandedUrl(url)
                val isContentExist = isContentExistsUseCase(realUrl)
                if (!isContentExist) {
                    // Add content into list if it does not exist yet
                    val fileName = URLUtil.guessFileName(realUrl, null, null)
                    val destination = "$path/$fileName"
                    val contentUIModel = ContentUIModel(
                        url = realUrl,
                        path = destination,
                        progress = 0,
                        name = fileName
                    )
                    addAndFetch(contentUIModel)
                }
            }
        }
    }

    private fun loadAllContent() {
        viewModelScope.launch {
            val list = loadAllContentsUseCase().map { it.toUIModel() }
            _contentList.value = list
        }
    }

    fun startDownloading(item: ContentUIModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Downloader.downloadOrResume(
                    item.url,
                    File(item.path)
                ) { progress, read, total ->
                    val content = item.copy(
                        progress = progress,
                        readLength = read,
                        totalLength = total,
                        isCompleted = (progress == 100),
                        isPaused = false
                    )
                    addAndFetch(content)
                }
            }
        }
    }

    fun stopDownloading(item: ContentUIModel) {
        val content = item.copy(isPaused = true)
        Downloader.stop(content.url)
        addAndFetch(content)
    }

    fun cancelDownload(content: ContentUIModel) {
        Downloader.stop(content.url)
        deleteContent(content)
    }

    private fun addAndFetch(content: ContentUIModel) {
        viewModelScope.launch {
            addContentUseCase(content.toEntityModel())
            loadAllContent()
        }
    }

    private fun deleteContent(content: ContentUIModel) {
        viewModelScope.launch {
            deleteContentUseCase(content.toEntityModel())
            File(content.path).delete()
            loadAllContent()
        }
    }
}