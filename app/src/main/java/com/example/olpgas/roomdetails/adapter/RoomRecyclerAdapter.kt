package com.example.olpgas.roomdetails.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.olpgas.auth.data.remote.SupabaseClient
import com.example.olpgas.databinding.RecyclerViewRoomsListBinding
import com.example.olpgas.roomdetails.data.model.AllRoomsDetails
import com.example.olpgas.roomdetails.ui.RoomDetails
import com.example.olpgas.roomdetails.ui.getCircularProgressDrawable
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class RoomRecyclerAdapter(var roomsData: List<AllRoomsDetails>, private val context: Context,private val manageRoom: Boolean = false) :
    RecyclerView.Adapter<RoomRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: RecyclerViewRoomsListBinding) : RecyclerView.ViewHolder(view.root) {
        val roomNameTV: TextView = view.roomNameTv
        val roomLocationTV: TextView = view.roomLocationTv
        val roomPrice: TextView = view.rentAmountTv
        val roomImage: ImageView = view.roomImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerViewRoomsListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentRoom = roomsData[position]

        CoroutineScope(Dispatchers.IO).launch {
            val imageByteArray= getDisplayImage(currentRoom.ownerId,currentRoom.roomName)
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
//        holder.roomContactBtn.text = data[position][2]

        holder.roomImage.setOnClickListener {
            val intent = Intent(context, RoomDetails::class.java)
            intent.putExtra("roomId",currentRoom.id)
            intent.putExtra("ownerId",currentRoom.ownerId)
            intent.putExtra("roomName",currentRoom.roomName)
            if(manageRoom) {
                intent.putExtra("manageRoom", true)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = roomsData.size

    private suspend fun getDisplayImage(userId: String,roomName: String) : ByteArray? {
        try {
            val bucket = SupabaseClient.client.storage.from("RoomPics")
            val files = bucket.list("$userId/$roomName")
            return bucket.downloadPublic("$userId/$roomName/${files[0].name}")
        }catch (e: Exception) {
            Log.d("Room", "Error: ${e.message}")
        }
        return null
    }
}