package com.example.olpgas.roomdetails.ui


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.olpgas.MyRoom
import com.example.olpgas.R
import com.example.olpgas.auth.ui.LoginActivity
import com.example.olpgas.auth.viewmodel.SupabaseAuthViewModel
import com.example.olpgas.databinding.ActivityMainBinding
import com.example.olpgas.profile.ui.UserAccount
import com.example.olpgas.roomdetails.adapter.RoomRecyclerAdapter
import com.example.olpgas.roomdetails.viewmodel.RoomsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val authViewModel: SupabaseAuthViewModel by lazy {
        ViewModelProvider(this)[SupabaseAuthViewModel::class.java]
    }

    private val roomViewModel: RoomsViewModel by lazy {
        ViewModelProvider(this)[RoomsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setToggleButtonForNavigationDrawer()
        navMenuItemClick()

        authViewModel.isUserLoggedIn(this)
        authViewModel.isLoggedIn.observe(this) {loggedIn ->
            if(!loggedIn) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        fetchRoomsDataAndSetAdapter()

        fetchRoomsData()
    }

    private fun setToggleButtonForNavigationDrawer() {
        toggle = ActionBarDrawerToggle(this@MainActivity,binding.drawerLayout,R.string.open,R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navMenuItemClick() {
        //On Click for menu drawer menu Item
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.profile -> startActivity(Intent(this@MainActivity,UserAccount::class.java))
                R.id.my_rooms -> startActivity(Intent(this@MainActivity,MyRoom::class.java))
                R.id.setting -> {}
                R.id.signOut -> {
                    authViewModel.logout(this)
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }
                R.id.exit -> {
                    val dialog = MaterialAlertDialogBuilder(this@MainActivity)
                        .setTitle("Exit!")
                        .setCancelable(false)
                        .setMessage("Do you want to Close the App?")
                        .setPositiveButton("Yes") { _, _ ->
                            finish()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                    dialog.show()
                }
            }
            true
        }
    }

    private fun fetchRoomsData() {

    }

    private fun fetchRoomsDataAndSetAdapter() {
        binding.rvRooms.layoutManager = LinearLayoutManager(this)
        val adapter = RoomRecyclerAdapter(emptyList(), this)
        binding.rvRooms.adapter = adapter

        roomViewModel.fetchAllRooms()
        roomViewModel.allRoomsDetails.observe(this) {
            adapter.roomsData = it
            adapter.notifyDataSetChanged()
        }
    }
}
