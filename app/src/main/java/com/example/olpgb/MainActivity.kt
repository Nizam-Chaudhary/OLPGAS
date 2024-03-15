package com.example.olpgb

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.olpgb.auth.presentation.LoginActivity
import com.example.olpgb.auth.viewmodel.SupabaseAuthViewModel
import com.example.olpgb.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val authViewModel: SupabaseAuthViewModel by lazy {
        ViewModelProvider(this)[SupabaseAuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUpBar()

        authViewModel.isUserLoggedIn(this)

        authViewModel.isUserLoggedIn(this)
        authViewModel.isLoggedIn.observe(this) {loggedIn ->
            if(!loggedIn) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.btnSignOut.setOnClickListener {
            authViewModel.logout(this)
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun setUpBar() {
        setSupportActionBar(binding.toolBar)

        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, binding.drawerLayout, R.string.nav_open, R.string.nav_close)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navClick()

    }

    private fun navClick() {
        binding.nav.setNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.nav_user_Account -> {
                    startActivity(Intent(this@MainActivity, UserAccount::class.java))
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu,menu)
        return true
    }

}
