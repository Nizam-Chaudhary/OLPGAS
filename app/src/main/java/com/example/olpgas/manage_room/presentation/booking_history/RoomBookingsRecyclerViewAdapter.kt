package com.example.olpgas.manage_room.presentation.booking_history

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.olpgas.R
import com.example.olpgas.bookings_history.data.local.database.RoomBookingDatabase
import com.example.olpgas.bookings_history.data.local.database.entity.RoomBookingLocal
import com.example.olpgas.bookings_history.data.remote.SupabaseBookings
import com.example.olpgas.bookings_history.data.repository.RoomBookingRepositoryImpl
import com.example.olpgas.browse_rooms.data.local.database.BrowseRoomDatabase
import com.example.olpgas.browse_rooms.data.remote.SupabaseListRooms
import com.example.olpgas.browse_rooms.data.repository.BrowseRoomsRepositoryImpl
import com.example.olpgas.browse_rooms.domain.repository.BrowseRoomsRepository
import com.example.olpgas.browse_rooms.domain.use_case.RefreshLocalCacheUseCase
import com.example.olpgas.core.data.remote.SupabaseClient
import com.example.olpgas.core.util.getCircularProgressDrawable
import com.example.olpgas.databinding.RawBookedRoomListBinding
import com.example.olpgas.databinding.RawRoomBookingsHistoryListBinding
import com.example.olpgas.view_room_details.data.local.database.FullRoomDetailsDatabase
import com.example.olpgas.view_room_details.data.remote.SupabaseBookRoom
import com.example.olpgas.view_room_details.data.remote.SupabaseRoomDetails
import com.example.olpgas.view_room_details.data.remote.model.RoomBooking
import com.example.olpgas.view_room_details.data.repository.ViewRoomDetailsRepositoryImpl
import com.example.olpgas.view_room_details.domain.repository.ViewRoomDetailsRepository
import com.example.olpgas.view_room_details.presentation.RoomDetailsActivity
import com.example.olpgas.view_room_details.presentation.RoomDetailsEvent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RoomBookingsRecyclerViewAdapter(
    var roomsData: List<RoomBookingLocal>,
    private val context: Context,
)  : RecyclerView.Adapter<RoomBookingsRecyclerViewAdapter.ViewHolder>(){
    class ViewHolder(view: RawRoomBookingsHistoryListBinding) : RecyclerView.ViewHolder(view.root) {
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
        val payerName = view.roomBookerName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RawRoomBookingsHistoryListBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount() = roomsData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = roomsData[position]

        holder.roomName.text = currentData.roomName
        holder.bookingDate.text = currentData.bookingDate
        holder.rent.text = ((currentData.rentAmount / currentData.shareableBy) * currentData.totalStayingPersons).toString() + "/-"
        holder.occupiedBy.text = currentData.totalStayingPersons.toString() + " Person"
        holder.deposit.text  = ((currentData.deposit / currentData.shareableBy) * currentData.totalStayingPersons).toString() + "/-"
        holder.rentStatus.text = currentData.paymentStatus
        holder.nextRentDate.text = currentData.nextPaymentDate
        holder.depositStatus.text = currentData.paymentStatus
        holder.location.text = currentData.city
        holder.payerName.text = currentData.payerName

        Glide.with(context)
            .load(currentData.imageUrl)
            .placeholder(getCircularProgressDrawable(context))
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle("Update Payment Status")
                .setMessage("is Payment Done?")
                .setPositiveButton("Yes") {_,_ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        SupabaseClient.client.postgrest.from("BookMaster")
                            .update({
                                set("paymentStatus", "Done")
                            }) {
                                filter {
                                    eq("id", currentData.id)
                                }
                            }


                        RefreshLocalCacheUseCase(
                            BrowseRoomsRepositoryImpl(SupabaseListRooms(), BrowseRoomDatabase.Companion(context)),
                            ViewRoomDetailsRepositoryImpl(SupabaseRoomDetails(), SupabaseBookRoom(), FullRoomDetailsDatabase.Companion(context)),
                            RoomBookingRepositoryImpl(RoomBookingDatabase.Companion(context), SupabaseBookings())
                        ).invoke()
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

}