package com.example.olpgas.browse_rooms.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.core.util.getCircularProgressDrawable
import com.example.olpgas.databinding.RawRecyclerViewRoomsListBinding
import com.example.olpgas.view_room_details.presentation.RoomDetailsActivity
import java.util.Locale

class RoomsRecyclerViewAdapter(
    var roomsData: List<AllRoomsDetailsLocal>,
    private val context: Context,
    private val activity: Activity
) : RecyclerView.Adapter<RoomsRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: RawRecyclerViewRoomsListBinding) : RecyclerView.ViewHolder(view.root) {
        val roomNameTv: TextView = view.roomNameTv
        val roomLocationTv: TextView = view.roomLocationTv
        val roomDescriptionTv = view.roomAboutTv
        val roomPriceTv: TextView = view.rentAmountTv
        val roomDepositTv: TextView = view.depositAmountTv
        val roomRatingsTv = view.ratingsTv
        val roomImage: ImageView = view.roomImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RawRecyclerViewRoomsListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentRoomData = roomsData[position]
        Glide.with(context)
            .load(currentRoomData.imageUrl)
            .placeholder(getCircularProgressDrawable(context))
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .into(holder.roomImage)

        val roomPrice = String.format(Locale.UK, "%,d", currentRoomData.rentAmount) + "/-"
        val deposit = String.format(Locale.UK, "%,d", currentRoomData.deposit) + "/-"

        holder.roomImage.transitionName = "uniqueTransitionName${position}"


        holder.roomNameTv.text = currentRoomData.roomName
        holder.roomLocationTv.text = currentRoomData.city
        holder.roomDescriptionTv.text = currentRoomData.description
        holder.roomPriceTv.text = roomPrice
        holder.roomDepositTv.text = deposit
        holder.roomRatingsTv.text = currentRoomData.ratings.toString()

        holder.itemView.setOnClickListener {
            val intent = Intent(context, RoomDetailsActivity::class.java)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                holder.roomImage,
                holder.roomImage.transitionName
            )
            intent.putExtra("id", currentRoomData.id)
            context.startActivity(intent, options.toBundle())
        }
    }

    override fun getItemCount() = roomsData.size
}