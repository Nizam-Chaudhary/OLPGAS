package com.example.olpgas.manage_room.presentation.owned_rooms

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.example.olpgas.R
import com.example.olpgas.browse_rooms.presentation.RoomsRecyclerViewAdapter
import com.example.olpgas.databinding.FragmentManageBinding
import com.example.olpgas.main_activity.presentation.MainActivity
import com.example.olpgas.manage_room.presentation.add_room.AddRoomActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageFragment : Fragment() {
    private lateinit var binding: FragmentManageBinding

    private val viewModel: OwnedRoomsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_manage, container, false)
        binding = FragmentManageBinding.bind(view)
       return binding.root
   }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        binding.hideViewBTN.setOnClickListener {
//            if (binding.hideView.visibility == View.VISIBLE) {
//                TransitionManager.beginDelayedTransition(binding.card, AutoTransition())
//                binding.hideView.visibility = View.GONE
//                binding.visibilityBtn.setImageResource(R.drawable.ic_arrow_up)
//            } else {
//                TransitionManager.beginDelayedTransition(binding.card, AutoTransition())
//                binding.hideView.visibility = View.VISIBLE
//                binding.visibilityBtn.setImageResource(R.drawable.ic_arrow_down)
//            }
//        }

        setUpRecyclerViewAdapter()

        onAddRoomFabClick()
    }

    private fun setUpRecyclerViewAdapter() {
        viewModel.onEvent(OwnedRoomEvents.OnCreate)

        val adapter = RoomsRecyclerViewAdapter(emptyList(), requireContext())
        binding.recyclerView.apply {
            this.layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
        viewModel.allRoomDetailsState?.observe(viewLifecycleOwner) {
            adapter.roomsData = it
            adapter.notifyDataSetChanged()
        }
    }

    private fun onAddRoomFabClick() {
        binding.addRoomFab.setOnClickListener {
            startActivity(Intent(requireContext(), AddRoomActivity::class.java))
        }
    }
}