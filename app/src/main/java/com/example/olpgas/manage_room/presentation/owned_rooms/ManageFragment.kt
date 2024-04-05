package com.example.olpgas.manage_room.presentation.owned_rooms

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.olpgas.R
import com.example.olpgas.browse_rooms.presentation.RoomsRecyclerViewAdapter
import com.example.olpgas.databinding.FragmentManageBinding
import com.example.olpgas.manage_room.presentation.post_room.PostRoomActivity
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

        setUpRecyclerViewAdapter()

        onAddRoomFabClick()
    }

    private fun setUpRecyclerViewAdapter() {
        viewModel.onEvent(OwnedRoomEvents.OnCreate)
        val activityContext = requireActivity()
        val adapter = OwnedRoomsRecyclerViewAdapter(emptyList(), requireContext(),activityContext)
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
            startActivity(Intent(requireContext(), PostRoomActivity::class.java))
        }
    }
}