package com.example.olpgas.roomdetails.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.olpgas.auth.ui.LoginActivity
import com.example.olpgas.auth.viewmodel.SupabaseAuthViewModel
import com.example.olpgas.databinding.FragmentSettingBinding
import com.example.olpgas.profile.ui.UserAccount


class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: SupabaseAuthViewModel by lazy {
        ViewModelProvider(this)[SupabaseAuthViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingBinding.inflate(layoutInflater, container, false)

        binding.userAccountBtn.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(), UserAccount::class.java))
        }

        binding.signOut.setOnClickListener {
            authViewModel.logout(requireContext())
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}