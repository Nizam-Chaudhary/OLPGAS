package com.example.olpgas.bookings_history.presentation

import android.content.Context
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.olpgas.R
import com.example.olpgas.bookings_history.data.local.database.entity.RoomBookingLocal
import com.example.olpgas.core.util.getCircularProgressDrawable
import com.example.olpgas.databinding.RawBookedRoomListBinding

class BookingsRecyclerViewAdapter(
    var roomsData: List<RoomBookingLocal>,
    private val context: Context,
)  : RecyclerView.Adapter<BookingsRecyclerViewAdapter.ViewHolder>(){
    class ViewHolder(view: RawBookedRoomListBinding) : RecyclerView.ViewHolder(view.root) {
        val imageView = view.bookedRoomImageRaw
        val location = view.bookedRoomLocation
        val occupiedBy = view.bookedRoomSharedBy
        val bookingDate = view.bookedRoomBookingDate
        val rent = view.bookedRoomRentTv
        val rentStatus = view.bookedRoomRentStatusChip
        val nextRentDate = view.bookedRoomNextRentTv
        val deposit = view.bookedRoomDepositTv
        val depositStatus = view.bookedRoomDepositStatusChip
        val visibilityBtn = view.visibilityBtn
        val hideView = view.hideView
        val card = view.card
        val materialDivider = view.materialDivider
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RawBookedRoomListBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount() = roomsData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = roomsData[position]

        holder.bookingDate.text = currentData.bookingDate
        holder.rent.text = ((currentData.rentAmount / currentData.shareableBy) * currentData.occupiedBy).toString()
        holder.occupiedBy.text = currentData.occupiedBy.toString()
        holder.deposit.text  = currentData.deposit.toString()
        holder.rentStatus.text = currentData.paymentStatus
        holder.nextRentDate.text = currentData.nextPaymentDate
        holder.depositStatus.text = currentData.paymentStatus
        holder.location.text = currentData.city


        holder.visibilityBtn.setOnClickListener {
            if (holder.hideView.visibility == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(holder.card, AutoTransition())
                holder.materialDivider.visibility = View.GONE
                holder.hideView.visibility = View.GONE
                holder.visibilityBtn.setImageResource(R.drawable.ic_arrow_up)
            } else {
                TransitionManager.beginDelayedTransition(holder.card, AutoTransition())
                holder.materialDivider.visibility = View.VISIBLE
                holder.hideView.visibility = View.VISIBLE
                holder.visibilityBtn.setImageResource(R.drawable.ic_arrow_down)
            }
        }


        Glide.with(context)
            .load(currentData.imageUrl)
            .placeholder(getCircularProgressDrawable(context))
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .into(holder.imageView)
    }

}