package com.example.olpgb

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.olpgb.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUpBar()

//        binding.btnSignOut.setOnClickListener {
//            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
//        }


        val roomData: Array<Array<String>> = arrayOf(
            arrayOf(
                "Sun View Apartment",
                "Station Road, Anand Nagar, Surat, Gujarat",
                "+91 9876543210",
                "7000/-",
                "15000/-",
                "2024-03-15"
            ), arrayOf(
                "Green Meadows",
                "Ring Road, Rajkot, Gujarat",
                "+91 8765432190",
                "4500/-",
                "10000/-",
                "2024-03-14"
            ), arrayOf(
                "Royal Heights",
                "Bypass Road, Vadodara, Gujarat",
                "+91 7896543211",
                "6000/-",
                "12000/-",
                "2024-03-11"
            )
        )


        binding.rv.layoutManager = LinearLayoutManager(this)
        val RoomRecyclerAdapter = RoomRecyclerAdapter(roomData, this)
        binding.rv.adapter = RoomRecyclerAdapter

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
