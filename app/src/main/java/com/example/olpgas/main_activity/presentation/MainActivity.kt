package com.example.olpgas.main_activity.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.widget.ViewPager2
import com.example.olpgas.R
import com.example.olpgas.auth.presentation.login_activity.LoginActivity
import com.example.olpgas.databinding.ActivityMainBinding
import com.example.olpgas.main_activity.domain.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalArgumentException

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var splashScreen: SplashScreen

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            viewModel.isUserLoggedInState.observe(this@MainActivity) {
                if(!it) {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            false
        }

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setViewPager(supportFragmentManager, lifecycle)

        onBottomNavigationViewItemSelected()

        onViewPageSwipe()
    }

    private fun setViewPager(fragmentManager: FragmentManager, lifecycle: Lifecycle) {
        val adapter = MainViewPagerAdapter(fragmentManager, lifecycle)

        binding.mainViewPager.apply {
            this.adapter = adapter
            this.currentItem = Constants.FRAGMENT_HOME
        }
    }

    private fun onViewPageSwipe() {
        val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageSelected(position: Int) {
                binding.bottomNavigationView.selectedItemId = getNavigationItemId(position)
            }

            override fun onPageScrollStateChanged(state: Int) = Unit
        }
        binding.mainViewPager.registerOnPageChangeCallback(onPageChangeCallback)
    }

    private fun getNavigationItemId(position: Int) : Int {
        return when(position) {
            1 -> R.id.bookings
            2 -> R.id.manage
            3 -> R.id.more
            else -> R.id.home
        }
    }

    private fun onBottomNavigationViewItemSelected() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> binding.mainViewPager.currentItem = Constants.FRAGMENT_HOME
                R.id.bookings -> binding.mainViewPager.currentItem = Constants.FRAGMENT_BOOKINGS
                R.id.manage -> binding.mainViewPager.currentItem = Constants.FRAGMENT_MANAGE
                R.id.more -> binding.mainViewPager.currentItem = Constants.FRAGMENT_MORE
                else -> {
                    throw (IllegalArgumentException("Invalid Position"))
                }
            }
            true
        }
    }
}