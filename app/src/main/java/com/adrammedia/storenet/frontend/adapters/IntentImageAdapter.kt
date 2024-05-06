package com.adrammedia.storenet.frontend.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.adrammedia.storenet.backend.data.tools.alltools.ToolImage
import com.adrammedia.storenet.databinding.ImageItemBinding

import javax.inject.Inject

class IntentImageAdapter @Inject constructor():RecyclerView.Adapter<IntentImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder( val binding: ImageItemBinding): RecyclerView.ViewHolder(binding.root)
    private val diffUtil = object : DiffUtil.ItemCallback<Uri>(){
        override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return oldItem.path == newItem.path
        }

        override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = differ.currentList[position]
        holder.binding.ivItemImg.setImageURI(image)

    }
}