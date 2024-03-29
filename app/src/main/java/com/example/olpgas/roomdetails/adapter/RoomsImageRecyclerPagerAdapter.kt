package com.example.olpgas.roomdetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.olpgas.auth.data.remote.SupabaseClient
import com.example.olpgas.databinding.ImageRawBinding
import com.example.olpgas.roomdetails.ui.getCircularProgressDrawable
import io.github.jan.supabase.storage.BucketItem
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomsImageRecyclerPagerAdapter(
    private val ownerId: String,
    private val roomName: String,
    private val files: List<BucketItem>,
    val context: Context
) : RecyclerView.Adapter<RoomsImageRecyclerPagerAdapter.ImageViewHolder>() {
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
        CoroutineScope(Dispatchers.IO).launch {
            val path = "${ownerId}/$roomName/${files[position].name}"
            val imageByteArray = SupabaseClient.client.storage.from("RoomPics").downloadPublic(path)

            withContext(Dispatchers.Main) {
                Glide.with(context)
                    .load(imageByteArray)
                    .placeholder(getCircularProgressDrawable(context))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(holder.imageView)
            }
        }
    }

    override fun getItemCount() = files.size
}
