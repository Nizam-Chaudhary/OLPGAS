package com.example.olpgas.manage_room.presentation.booking_history

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.olpgas.R
import com.example.olpgas.bookings_history.data.local.database.entity.RoomBookingLocal
import com.example.olpgas.core.util.getCircularProgressDrawable
import com.example.olpgas.databinding.RawBookedRoomListBinding
import com.example.olpgas.view_room_details.presentation.RoomDetailsActivity

class RoomBookingsRecyclerViewAdapter(
    var roomsData: List<RoomBookingLocal>,
    private val context: Context,
)  : RecyclerView.Adapter<RoomBookingsRecyclerViewAdapter.ViewHolder>(){
    class ViewHolder(view: RawBookedRoomListBinding) : RecyclerView.ViewHolder(view.root) {
        val roomName = view.bookedRoomName
        val imageView = view.bookedRoomImageRaw
        val location = view.bookedRoomLocation
        val occupiedBy = view.bookedRoomSharedBy
        val bookingDate = view.bookedRoomBookingDate
        val rent = view.bookedRoomRentTv
        val rentStatus = view.bookedRoomRentStatusChip
        val nextRentDate = view.bookedRoomNextRentTv
        val deposit = view.bookedRoomDepositTv
        val depositStatus = view.bookedRoomDepositStatusChip
        val card = view.card
        val materialDivider = view.materialDivider
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RawBookedRoomListBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount() = roomsData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = roomsData[position]

        holder.roomName.text = currentData.roomName
        holder.bookingDate.text = currentData.bookingDate
        holder.rent.text = ((currentData.rentAmount / currentData.shareableBy) * currentData.occupiedBy).toString()
        holder.occupiedBy.text = currentData.occupiedBy.toString() + " Person"
        holder.deposit.text  = ((currentData.deposit / currentData.shareableBy) * currentData.occupiedBy).toString()
        holder.rentStatus.text = currentData.paymentStatus
        holder.nextRentDate.text = currentData.nextPaymentDate
        holder.depositStatus.text = currentData.paymentStatus
        holder.location.text = currentData.city


        Glide.with(context)
            .load(currentData.imageUrl)
            .placeholder(getCircularProgressDrawable(context))
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, RoomDetailsActivity::class.java)

            intent.putExtra("id", currentData.roomId)
            context.startActivity(intent)
        }
    }

}