package com.adrammedia.storenet.frontend.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.adrammedia.storenet.R
import com.adrammedia.storenet.backend.data.tools.alltools.ToolImage
import com.adrammedia.storenet.databinding.ImageItemBinding
import com.adrammedia.storenet.utils.Constants
import java.io.File
import javax.inject.Inject

class ToolImagesAdapter @Inject constructor():RecyclerView.Adapter<ToolImagesAdapter.ImageViewHolder>() {

    inner class ImageViewHolder( val binding: ImageItemBinding):ViewHolder(binding.root)
    private val diffUtil = object :DiffUtil.ItemCallback<ToolImage>(){
        override fun areItemsTheSame(oldItem: ToolImage, newItem: ToolImage): Boolean {
            return oldItem.path == newItem.path
        }

        override fun areContentsTheSame(oldItem: ToolImage, newItem: ToolImage): Boolean {
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
        holder.binding.ivItemImg
               .load(Constants.IMAGES_BASE_URL + image.path){
               crossfade(true)
                crossfade(500)
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_background)
        }
    }
}