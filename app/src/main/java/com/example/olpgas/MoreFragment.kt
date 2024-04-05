package com.example.olpgas

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.olpgas.databinding.FragmentMoreBinding
import com.example.olpgas.databinding.RawThemeBinding
import com.example.olpgas.user_profile.presentation.UserProfileActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MoreFragment : Fragment() {

    private lateinit var binding: FragmentMoreBinding

//    private val viewModel: UserProfileViewModel by viewModels()

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


//        binding.moreUserName.text = viewModel.userProfileState.value?.userName

        onProfileBtn()

        onThemeBtn()

        onSignOutBtn()

    }

    private fun onSignOutBtn() {
        binding.moreSignOut.setOnClickListener {

        }
    }

    private fun onThemeBtn() {
        binding.themeBTN.setOnClickListener {
            val view = View.inflate(requireContext(), R.layout.raw_theme, null)

            val rawThemeBinding = RawThemeBinding.bind(view)

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Theme")
                .setView(view)
                .setPositiveButton("Apply") { _, _ ->
                    val checkedRadioButtonId = rawThemeBinding.rdgTheme.checkedRadioButtonId
                    val gender = when(checkedRadioButtonId) {
                        R.id.rd_light -> "Light"
                        R.id.rd_dark-> "Dark"
                        else -> "Sysytem"
                    }


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

    }

//    private fun setTheme() {
//        when (SharedPreferencesAmplifted(this).getUiMode()) {
//            "light" -> {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                    val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
//                    uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO)
//                } else {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                }
//            }
//
//            "dark" -> {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                    val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
//                    uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)
//                } else {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                }
//            }
//
//            "system" -> {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                    val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
//                    uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_AUTO)
//                } else {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//                }
//            }
//        }
//
//    }

}