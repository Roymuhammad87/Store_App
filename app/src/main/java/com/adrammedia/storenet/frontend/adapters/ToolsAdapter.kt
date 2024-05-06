package com.adrammedia.storenet.frontend.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.adrammedia.storenet.R
import com.adrammedia.storenet.backend.data.tools.alltools.Tool
import com.adrammedia.storenet.databinding.ImageItemBinding
import com.adrammedia.storenet.databinding.ToolIttemBinding
import com.adrammedia.storenet.utils.Constants
import com.bumptech.glide.Glide
import javax.inject.Inject

class ToolsAdapter @Inject constructor(): RecyclerView.Adapter<ToolsAdapter.ToolViewHolder>() {
    private lateinit var context: Context

    private val diffUtil = object : DiffUtil.ItemCallback<Tool>() {
        override fun areItemsTheSame(oldItem: Tool, newItem: Tool): Boolean {
            return oldItem.toolId == newItem.toolId
        }

        override fun areContentsTheSame(oldItem: Tool, newItem: Tool): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtil)

    inner class ToolViewHolder(val binding: ToolIttemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imagesAdapter = ToolImagesAdapter()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolViewHolder {
        context = parent.context

        return ToolViewHolder(
            ToolIttemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ToolViewHolder, position: Int) {
        val tool = differ.currentList[position]
        holder.binding.apply {
            toolItemTvName.text = tool?.toolName
            toolItemTvPrice.text = "${tool.toolPrice}$"
            toolItemTvCategory.text = "Type->" + tool?.toolCategory
            toolItemTvDescription.text =tool?.toolDescription
            toolItemUser.text = "Publisher: "+tool.toolUser
            toolItemRv.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = holder.imagesAdapter
                holder.imagesAdapter.differ.submitList(tool?.toolImages)
            }
        }
    }
}
