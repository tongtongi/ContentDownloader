package com.example.contentdownloader.ui.download

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.URLUtil
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contentdownloader.R
import com.example.contentdownloader.ui.download.adapter.ContentAdapter
import com.example.contentdownloader.ui.model.ContentUIModel
import com.example.contentdownloader.ui.videoplayer.VideoPlayerActivity
import com.example.contentdownloader.utils.VerticalSpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_download.*
import kotlinx.android.synthetic.main.dialog_add_url.view.*
import java.io.File

private const val DIRECTORY_NAME = "/TEST"

@AndroidEntryPoint
class DownloadActivity : AppCompatActivity() {

    private val contentAdapter: ContentAdapter by lazy {
        ContentAdapter(
            ::onDownloadClicked,
            ::onDeleteClicked,
            ::onPlayClicked
        )
    }

    private val path by lazy { this.getExternalFilesDir(null)!!.absolutePath }

    private val viewModel: DownloadViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        val file = File(this.getExternalFilesDir(null)!!.absolutePath + DIRECTORY_NAME)
        if (!file.isDirectory) {
            file.mkdir()
        }

        initRecyclerView()
        observeViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                addUrlButtonClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addUrlButtonClicked() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.title_add_content_url))

        val view = layoutInflater.inflate(R.layout.dialog_add_url, null)

        builder.setView(view)
            .setPositiveButton(R.string.add_url) { _, _ -> }
            .setNegativeButton(R.string.text_cancel) { dialog, _ -> dialog.cancel() }
        val dialog = builder.create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val editText = view.textInputEditTextUrl
            val editTextLayout = view.textInputLayoutUrl
            val url = editText.text.toString()
            if (URLUtil.isValidUrl(url)) {
                viewModel.addToList(path, url)
                editTextLayout.error = null
                dialog.dismiss()
            } else {
                editTextLayout.error = getString(R.string.error_text_add_url)
            }
        }
    }

    private fun showConfirmationDialog(item: ContentUIModel) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.title_delete_content))
        builder.setMessage(getString(R.string.delete_confirmation_message))
        builder.setPositiveButton(R.string.text_delete) { dialog, id ->
            viewModel.cancelDownload(item)
        }.setNegativeButton(R.string.text_cancel) { dialog, id ->
            dialog.cancel()
        }

        builder.create().show()
    }

    private fun observeViewModel() {
        viewModel.contentList.observe(this, Observer {
            contentAdapter.submitList(it)
            if (it.isEmpty()) {
                showVideoHolder()
            } else {
                hideVideoHolder()
            }
        })
    }

    private fun showVideoHolder() {
        lottieVideoHolder.isVisible = true
        textViewHint.isVisible = true
        recyclerView.isVisible = false
    }

    private fun hideVideoHolder() {
        recyclerView.isVisible = true
        lottieVideoHolder.isVisible = false
        textViewHint.isVisible = false
    }

    private fun initRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@DownloadActivity)
            adapter = contentAdapter
            addItemDecoration(
                VerticalSpaceItemDecoration(
                    resources.getDimension(R.dimen.material_grid_6x).toInt()
                )
            )
        }
    }

    private fun onDownloadClicked(item: ContentUIModel) {
        when (item.isPaused) {
            true -> viewModel.startDownloading(item)
            else -> viewModel.stopDownloading(item)
        }
    }

    private fun onDeleteClicked(item: ContentUIModel) {
        showConfirmationDialog(item)
    }

    private fun onPlayClicked(item: ContentUIModel) {
        startActivity(VideoPlayerActivity.newIntent(this, item.path))
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, DownloadActivity::class.java)
    }
}