package com.example.olpgas.view_room_details.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.olpgas.core.data.remote.SupabaseClient
import com.example.olpgas.databinding.ImageRawBinding
import com.example.olpgas.core.util.getCircularProgressDrawable
import io.github.jan.supabase.storage.BucketItem
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomImageRecyclerPagerAdapter(
    var imagesUrl: List<String>,
    val context: Context
) : RecyclerView.Adapter<RoomImageRecyclerPagerAdapter.ImageViewHolder>() {
    inner class ImageViewHolder(view: ImageRawBinding): ViewHolder(view.root) {
        val imageView = view.imageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(ImageRawBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imagesUrl[position]
        Glide.with(context)
            .load(imageUrl)
            .placeholder(getCircularProgressDrawable(context))
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .into(holder.imageView)
    }

    override fun getItemCount() = imagesUrl.size
}
