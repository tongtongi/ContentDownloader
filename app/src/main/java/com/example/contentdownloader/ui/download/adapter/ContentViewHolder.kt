package com.example.contentdownloader.ui.download.adapter

import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contentdownloader.R
import com.example.contentdownloader.ui.model.ContentUIModel
import kotlinx.android.synthetic.main.item_content.view.*

class ContentViewHolder(
    parent: ViewGroup,
    private val onDownloadClicked: (ContentUIModel) -> Unit,
    private val onDeleteClicked: (ContentUIModel) -> Unit,
    private val onPlayClicked: (ContentUIModel) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_content, parent, false)
) {
    fun bind(item: ContentUIModel) {
        itemView.textViewName.text = item.name
        itemView.progressBar.progress = item.progress

        setDownloadRatio(item.readLength, item.totalLength)

        itemView.buttonDownload.apply {
            when (item.isPaused) {
                true -> {
                    text = itemView.resources.getString(R.string.text_download)
                    setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_download, 0, 0, 0)
                }
                else -> {
                    text = itemView.resources.getString(R.string.text_pause)
                    setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause_circle, 0, 0, 0)
                }
            }
        }

        itemView.buttonDownload.isEnabled = !item.isCompleted
        itemView.buttonDownload.setOnClickListener { onDownloadClicked(item) }
        itemView.buttonDelete.setOnClickListener { onDeleteClicked(item) }
        itemView.buttonPlay.setOnClickListener { onPlayClicked(item) }
    }

    private fun setDownloadRatio(readLength: Long, totalLength: Long) {
        itemView.textViewDownloadRatio.apply {
            text = itemView.resources.getString(
                R.string.text_download_ratio,
                Formatter.formatShortFileSize(itemView.context, readLength),
                Formatter.formatShortFileSize(itemView.context, totalLength)
            )
            setTextColor(
                context.getColor(
                    if (readLength == totalLength && readLength > 0) R.color.green else R.color.offGrey
                )
            )
        }
    }
}