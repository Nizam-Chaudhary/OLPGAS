package com.example.olpgb

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

class RoomRecyclerAdapter(private val data: Array<Array<String>>, private val context: Context) :
    RecyclerView.Adapter<RoomRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val roomNameTV: TextView = view.findViewById(R.id.roomNameTV)
        val roomLocationTV: TextView = view.findViewById(R.id.roomLocationTV)
        val roomPrice: TextView = view.findViewById(R.id.roomPrice)
        val roomDeposit: TextView = view.findViewById(R.id.roomDeposit)
        val dateOfPost: TextView = view.findViewById(R.id.dateOfPost)
        val roomContactBtn: Button = view.findViewById(R.id.roomContactBtn)
        val roomImage: ImageView = view.findViewById(R.id.roomImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.raw_room, parent, false)
        Toast.makeText(context, "he", Toast.LENGTH_SHORT)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position == 0) {
            holder.roomImage.setImageResource(R.drawable.room1)
        } else if (position == 1) {
            holder.roomImage.setImageResource(R.drawable.room2)
        } else {
            holder.roomImage.setImageResource(R.drawable.room3)
        }

        holder.roomNameTV.text = data[position][0]
        holder.roomLocationTV.text = data[position][1]
        holder.roomPrice.text = data[position][3]
        holder.roomDeposit.text = data[position][4]
        holder.dateOfPost.text = data[position][5]
        holder.roomContactBtn.text = data[position][2]

        holder.roomImage.setOnClickListener {

            val intent = Intent(context, RoomDetails::class.java)
            intent.putExtra("transitionName", ViewCompat.getTransitionName(holder.roomImage))
            val option = ViewCompat.getTransitionName(holder.roomImage)?.let { it1 ->
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as Activity,
                    holder.roomImage,
                    it1
                )
            }
            if (option != null) {
                context.startActivity(intent,option.toBundle())
            }
        }


    }

    override fun getItemCount() = data.size
}