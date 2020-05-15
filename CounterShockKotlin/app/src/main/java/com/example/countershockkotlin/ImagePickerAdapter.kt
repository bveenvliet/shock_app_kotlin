package com.example.countershockkotlin

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.File

class ImagePickerAdapter(private var items: List<ImageModel>, private var callback: Callback) :
    RecyclerView.Adapter<ImagePickerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemView.setOnClickListener {
            callback.itemSelected(item)
        }

        val imgUri = if (item.isAsset) {
            ShockUtils.getDrawableUri(holder.itemView.context, item.imgFilename)
        } else {
            Uri.fromFile(File(item.imgFilename))
        }
        Glide.with(holder.itemView.context)
            .load(imgUri)
            .into(holder.imageView)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.gridImageView)
    }

    interface Callback {
        fun itemSelected(item: ImageModel)
    }

}