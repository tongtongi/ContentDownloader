package com.example.contentdownloader.ui.download.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.contentdownloader.ui.model.ContentUIModel

class ContentAdapter(
    private val onDownloadClicked: (ContentUIModel) -> Unit,
    private val onDeleteClicked: (ContentUIModel) -> Unit,
    private val onPlayClicked: (ContentUIModel) -> Unit
) : ListAdapter<ContentUIModel, ContentViewHolder>(ContentItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        return ContentViewHolder(parent, onDownloadClicked, onDeleteClicked, onPlayClicked)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class ContentItemDiffCallback : DiffUtil.ItemCallback<ContentUIModel>() {

    override fun areItemsTheSame(oldItem: ContentUIModel, newItem: ContentUIModel) =
        oldItem.url == newItem.url

    override fun areContentsTheSame(oldItem: ContentUIModel, newItem: ContentUIModel) =
        oldItem == newItem
}