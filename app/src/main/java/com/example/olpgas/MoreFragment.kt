package com.example.olpgas

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.olpgas.databinding.FragmentMoreBinding
import com.example.olpgas.user_profile.presentation.UserProfileActivity

class MoreFragment : Fragment() {

    private lateinit var binding: FragmentMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_more, container, false)
        binding = FragmentMoreBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.userProfileBTN.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    UserProfileActivity::class.java
                )
            )
        }

        binding.themeBTN.setOnClickListener {

        }

        binding.moreSignOut.setOnClickListener {

        }

    }

}