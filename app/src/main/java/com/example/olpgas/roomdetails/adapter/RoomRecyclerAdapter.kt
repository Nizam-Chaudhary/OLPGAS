package com.example.olpgas.roomdetails.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.olpgas.R
import com.example.olpgas.auth.data.network.SupabaseClient
import com.example.olpgas.roomdetails.data.model.AllRoomsDetails
import com.example.olpgas.roomdetails.ui.RoomDetails
import com.example.olpgas.roomdetails.ui.getCircularProgressDrawable
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class RoomRecyclerAdapter(var roomsData: List<AllRoomsDetails>, private val context: Context) :
    RecyclerView.Adapter<RoomRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val roomNameTV: TextView = view.findViewById(R.id.roomNameTV)
        val roomLocationTV: TextView = view.findViewById(R.id.roomLocationTV)
        val roomPrice: TextView = view.findViewById(R.id.rent_amount_tv)
        val roomDeposit: TextView = view.findViewById(R.id.room_deposit_tv)
        val roomContactBtn: Button = view.findViewById(R.id.room_contact_button)
        val roomImage: ImageView = view.findViewById(R.id.room_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.room_raw, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentRoom = roomsData[position]

        CoroutineScope(Dispatchers.IO).launch {
            val imageByteArray= getDisplayImage(currentRoom.ownerId)
            withContext(Dispatchers.Main) {
                Glide.with(context)
                    .load(imageByteArray)
                    .placeholder(getCircularProgressDrawable(context))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(holder.roomImage)
            }
        }

        val roomPrice = String.format(Locale.UK, "%,d", currentRoom.rentAmount) + "/-"
        val deposit = String.format(Locale.UK, "%,d", currentRoom.deposit) + "/-"
        holder.roomNameTV.text = currentRoom.roomName
        holder.roomLocationTV.text = currentRoom.city
        holder.roomPrice.text = roomPrice
        holder.roomDeposit.text = deposit
//        holder.roomContactBtn.text = data[position][2]

        holder.roomImage.setOnClickListener {
            val intent = Intent(context, RoomDetails::class.java)
            intent.putExtra("roomId",currentRoom.id)
            intent.putExtra("ownerId",currentRoom.ownerId)
            context.startActivity(intent)

        }
    }

    override fun getItemCount() = roomsData.size

    private suspend fun getDisplayImage(userId: String) : ByteArray? {
        try {
            val bucket = SupabaseClient.client.storage.from("RoomPics")
            val files = bucket.list(userId)
            return bucket.downloadPublic("$userId/${files[0].name}")
        }catch (e: Exception) {
            Log.d("Room", "Error: ${e.message}")
        }
        return null
    }
}