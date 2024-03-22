package com.example.olpgas.roomdetails.ui


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.olpgas.R
import com.example.olpgas.auth.ui.LoginActivity
import com.example.olpgas.auth.viewmodel.SupabaseAuthViewModel
import com.example.olpgas.databinding.ActivityViewRoomsBinding
import com.example.olpgas.manage_room.MyRoomActivity
import com.example.olpgas.profile.ui.UserAccount
import com.example.olpgas.roomdetails.adapter.RoomRecyclerAdapter
import com.example.olpgas.roomdetails.viewmodel.RoomsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ViewRoomActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle

    private val binding: ActivityViewRoomsBinding by lazy {
        ActivityViewRoomsBinding.inflate(layoutInflater)
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

        setUserName()
    }

    private fun setToggleButtonForNavigationDrawer() {
        toggle = ActionBarDrawerToggle(this@ViewRoomActivity,binding.drawerLayout,R.string.open,R.string.close)
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
                R.id.profile -> startActivity(Intent(this@ViewRoomActivity,UserAccount::class.java))
                R.id.my_rooms -> startActivity(Intent(this@ViewRoomActivity, MyRoomActivity::class.java))
                R.id.setting -> {}
                R.id.signOut -> {
                    val dialog = MaterialAlertDialogBuilder(this@ViewRoomActivity)
                        .setTitle("Sign Out!")
                        .setCancelable(false)
                        .setMessage("Are you sure you want to sign out?")
                        .setPositiveButton("Yes") { _, _ ->
                            authViewModel.logout(this)
                            startActivity(Intent(this@ViewRoomActivity, LoginActivity::class.java))
                            finish()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                    dialog.show()
                }
                R.id.exit -> {
                    val dialog = MaterialAlertDialogBuilder(this@ViewRoomActivity)
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

    private fun setUserName() {
        roomViewModel.getUserName()
        roomViewModel.userName.observe(this) {
            binding.navView.getHeaderView(0).findViewById<TextView>(R.id.userNameHeader).text = it
        }
    }
}
