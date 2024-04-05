package com.example.olpgas.more_details.presentation

import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.olpgas.R
import com.example.olpgas.auth.presentation.login_activity.LoginActivity
import com.example.olpgas.databinding.FragmentMoreBinding
import com.example.olpgas.databinding.RawThemeBinding
import com.example.olpgas.more_details.domain.util.ThemeMode
import com.example.olpgas.user_profile.presentation.UserProfileActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreFragment : Fragment() {

    private lateinit var binding: FragmentMoreBinding

    private val viewModel: MoreViewModel by viewModels()

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


        //binding.moreUserName.text = viewModel.userProfileState.value?.userName
        setState()

        onProfileBtn()

        onThemeBtn()

        onSignOutClick()
    }

    private fun setState() {
        viewModel.onEvent(MoreEvent.OnCreate)
        viewModel.userNameState.observe(viewLifecycleOwner) {
            if(!it.isNullOrEmpty()) {
                binding.moreUserName.text = it
            }
        }

        viewModel.userProfileImageState.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) {
                Glide.with(requireContext())
                    .load(it)
                    .into(binding.moreProfile)
            }
        }
    }

    private fun onThemeBtn() {
        binding.themeBTN.setOnClickListener {
            val view = View.inflate(requireContext(), R.layout.raw_theme, null)

            val rawThemeBinding = RawThemeBinding.bind(view)
            val currentThemeMode = viewModel.themeModeState.value
            when(currentThemeMode) {
                "Light" -> {
                    rawThemeBinding.rdLight.isChecked = true
                }
                "Dark" -> {
                    rawThemeBinding.rdDark.isChecked = true
                }
                "System" -> {
                    rawThemeBinding.rdSystem.isChecked = true
                }
            }

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Theme")
                .setView(view)
                .setPositiveButton("Apply") { _, _ ->
                    val checkedRadioButtonId = rawThemeBinding.rdgTheme.checkedRadioButtonId
                    val themeMode = when(checkedRadioButtonId) {
                        R.id.rd_light -> ThemeMode.Light
                        R.id.rd_dark -> ThemeMode.Dark
                        R.id.rd_system -> ThemeMode.System
                        else -> ThemeMode.System
                    }

                    viewModel.onEvent(MoreEvent.OnThemeModeChange(themeMode))
                    setTheme(themeMode)
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .show()

        }
    }

    private fun onProfileBtn() {
        binding.userProfileBTN.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(), UserProfileActivity::class.java
                )
            )
        }

        binding.moreProfile.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(), UserProfileActivity::class.java
                )
            )
        }
    }

    private fun onSignOutClick() {
        binding.moreSignOut.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Sign out")
                .setMessage("Do you want to sign out")
                .setPositiveButton("Yes") {_,_ ->
                    viewModel.onEvent(MoreEvent.SignOut)
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private fun setTheme(theme: ThemeMode) {
        when (theme) {
             ThemeMode.Light -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val uiModeManager = getSystemService(requireActivity(), UiModeManager::class.java) as UiModeManager
                    uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }

            ThemeMode.Dark -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val uiModeManager = getSystemService(requireActivity(), UiModeManager::class.java) as UiModeManager
                    uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }

            ThemeMode.System -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val uiModeManager = getSystemService(requireActivity(), UiModeManager::class.java) as UiModeManager
                    uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_AUTO)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }

    }

}