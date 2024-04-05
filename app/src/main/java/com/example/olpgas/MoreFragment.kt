package com.example.olpgas

import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.olpgas.databinding.FragmentMoreBinding
import com.example.olpgas.user_profile.presentation.UserProfileActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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

//            val sharedPreferences = SharedPreferences(requireContext())
            val light = RadioButton(context)
            val dark = RadioButton(context)
            val system = RadioButton(context)


//            light.id = 1
//            dark.id = 2
//            system.id = 3
//

            light.setText(R.string.light_theme)
            dark.setText(R.string.dark_theme)
            system.setText(R.string.system_theme)

//            when (sharedPreferences.getUiMode()) {
//                "light" -> light.isChecked = true
//                "dark" -> dark.isChecked = true
//                "system" -> system.isChecked = true
//            }

            val radioGroup = RadioGroup(requireContext())
            //byNameAsc.isChecked = true

            radioGroup.addView(light)
            radioGroup.addView(dark)
            radioGroup.addView(system)

            val linearLayout = LinearLayout(requireContext())
            linearLayout.addView(radioGroup)
            linearLayout.setPadding(40, 10, 40, 10)

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Theme")
                .setView(linearLayout)
                .setMessage("Select Theme Mode.")
                .setPositiveButton("Apply") { _, _ ->
                    println(radioGroup.checkedRadioButtonId.toString())
                    when (radioGroup.checkedRadioButtonId) {
                        1 -> {
//                            sharedPreferencesAmplifted.setUiMode("light")
//                            setTheme()
                        }

                        2 -> {
//                            sharedPreferencesAmplifted.setUiMode("dark")
//                            setTheme()
                        }

                        3 -> {
//                            sharedPreferencesAmplifted.setUiMode("system")
//                            setTheme()
                        }

                    }
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .show()

        }

        binding.moreSignOut.setOnClickListener {

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