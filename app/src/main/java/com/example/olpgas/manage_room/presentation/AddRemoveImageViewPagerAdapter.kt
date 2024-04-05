package com.example.olpgas.manage_room.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.olpgas.R
import com.example.olpgas.core.util.getCircularProgressDrawable
import com.example.olpgas.databinding.ImageRawBinding

class AddRemoveImageViewPagerAdapter(
    private val context: Context,
    private val images: List<Any>,
    var onItemClickListener: OnItemClickListener? = null
    ) : RecyclerView.Adapter<AddRemoveImageViewPagerAdapter.MyViewHolder>() {
    class MyViewHolder(view: ImageRawBinding) : RecyclerView.ViewHolder(view.root) {
        val imageView = view.imageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ImageRawBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    interface OnItemClickListener {
        fun onAddImageItemClick()

        fun onRemoveImageItemClick(position: Int)
    }

    override fun getItemCount() = images.size + 1

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(position == images.size) {

            Glide.with(context)
                .load(R.drawable.ic_add)
                .fitCenter()
                .into(holder.imageView)

            holder.itemView.setOnClickListener {
                onItemClickListener?.onAddImageItemClick()
            }
        } else {
            val image = images[position]

            Glide.with(context)
                .load(image)
                .placeholder(getCircularProgressDrawable(context))
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(holder.imageView)

            holder.itemView.setOnClickListener {
                onItemClickListener?.onRemoveImageItemClick(position)
            }
        }
    }
}