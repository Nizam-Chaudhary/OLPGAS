package com.example.olpgas.manage_room.presentation.owned_rooms

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.olpgas.R
import com.example.olpgas.browse_rooms.data.local.database.entities.AllRoomsDetailsLocal
import com.example.olpgas.core.util.getCircularProgressDrawable
import com.example.olpgas.databinding.RawMangeRoomBinding
import com.example.olpgas.manage_room.presentation.update_room.UpdateRoomActivity
import com.example.olpgas.view_room_details.presentation.RoomDetailsActivity
import java.util.Locale

class OwnedRoomsRecyclerViewAdapter(
    var roomsData: List<AllRoomsDetailsLocal>,
    private val context: Context,
    private val activity: Activity,
) : RecyclerView.Adapter<OwnedRoomsRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: RawMangeRoomBinding) : RecyclerView.ViewHolder(view.root) {
        val roomNameTv: TextView = view.roomNameTv
        val roomLocationTv: TextView = view.roomLocationTv
        val roomDescriptionTv = view.roomAboutTv
        val roomPriceTv: TextView = view.rentAmountTv
        val roomDepositTv: TextView = view.depositAmountTv
        val roomRatingsTv = view.ratingsTv
        val roomImage: ImageView = view.roomImage
        val controlLayout = view.controlLayout
        val card = view.card

        val detailsBtn = view.detailsBtn
        val detailsTv = view.detailsTv

        val updateBtn = view.updateBtn
        val updateTv = view.updateTv

        val historyBtn = view.historyBtn
        val historyTv = view.historyTv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RawMangeRoomBinding.inflate(
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

        val collapseAnimation = AnimationUtils.loadAnimation(context, R.anim.collapse_animation) // Replace with your animation resource
        collapseAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // Optional: Set visibility to GONE before animation starts (avoids a flicker)
                holder.controlLayout.visibility = View.GONE
            }

            override fun onAnimationEnd(animation: Animation?) {
                // Set visibility to GONE after animation is complete
                holder.controlLayout.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // Not used in this case
            }
        })

        holder.itemView.setOnClickListener {
            if (holder.controlLayout.visibility == View.VISIBLE) {
                //holder.controlLayout.visibility = View.GONE
                holder.controlLayout.startAnimation(collapseAnimation)
            } else {
                TransitionManager.beginDelayedTransition(holder.card, AutoTransition())
                holder.controlLayout.visibility = View.VISIBLE
            }
        }

        holder.detailsBtn.setOnClickListener {
            onDetailsClick(currentRoomData.id, holder.roomImage)
        }

        holder.detailsTv.setOnClickListener {
            onDetailsClick(currentRoomData.id, holder.roomImage)
        }

        holder.updateBtn.setOnClickListener {
            onUpdateClick(currentRoomData.id)
        }

        holder.updateTv.setOnClickListener {
            onUpdateClick(currentRoomData.id)
        }

        holder.historyBtn.setOnClickListener {

        }

        holder.historyTv.setOnClickListener {

        }
    }

    override fun getItemCount() = roomsData.size

    private fun onUpdateClick(id: Int) {
        val intent = Intent(context, UpdateRoomActivity::class.java)
        intent.putExtra("id", id)
        context.startActivity(intent)
    }

    private fun onHistoryClick() {

    }

    private fun onDetailsClick(
        id: Int, roomImage: ImageView
    ) {
        val intent = Intent(context, RoomDetailsActivity::class.java)
        intent.putExtra("id", id)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity, roomImage, roomImage.transitionName
        )
        context.startActivity(intent, options.toBundle())
    }
}