package com.example.olpgas.browse_rooms.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.olpgas.R
import com.example.olpgas.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: BrowseRoomsViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerViewAdapter()

        onSwipeRefresh()
    }

    private fun setRecyclerViewAdapter() {
        val adapter = RoomsRecyclerViewAdapter(emptyList(), emptyList(), requireContext())
        binding.rvListRooms.apply {
            this.layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        viewModel.onEvent(BrowseRoomsEvent.OnCreate)
        viewModel.allRoomsImageState.observe(viewLifecycleOwner) {
            val roomsData = viewModel.allRoomDetailsState.value
            val roomsImageData = it
            if(roomsData != null && roomsImageData != null) {
                adapter.roomsData = roomsData
                adapter.roomsImage = roomsImageData
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun onSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.onEvent(BrowseRoomsEvent.OnRefresh)
        }
    }
}