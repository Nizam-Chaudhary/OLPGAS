package com.example.olpgas.bookings_history.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.olpgas.R
import com.example.olpgas.databinding.FragmentBookingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingsFragment : Fragment() {
    private val viewModel: BookingsViewModel by viewModels()

    private lateinit var binding: FragmentBookingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_bookings, container, false)
        binding = FragmentBookingsBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
    }

    private fun setAdapter() {
        viewModel.onEvent(BookingEvent.OnCreate)

        val adapter = BookingsRecyclerViewAdapter(emptyList(), requireContext())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.allBookings.observe(viewLifecycleOwner) {
            adapter.roomsData = it
            adapter.notifyDataSetChanged()

            if(it.isEmpty()) {
                binding.noBookings.visibility = View.VISIBLE
            } else {
                binding.noBookings.visibility = View.GONE
            }
        }
    }
}