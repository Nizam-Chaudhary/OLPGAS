package com.example.olpgas.roomdetails.ui


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.olpgas.R
import com.example.olpgas.auth.ui.LoginActivity
import com.example.olpgas.auth.viewmodel.SupabaseAuthViewModel
import com.example.olpgas.databinding.ActivityViewRoomsBinding
import com.example.olpgas.manage_room.ui.MyRoomActivity
import com.example.olpgas.profile.ui.UserAccount
import com.example.olpgas.roomdetails.adapter.RoomRecyclerAdapter
import com.example.olpgas.roomdetails.data.model.Filter
import com.example.olpgas.roomdetails.viewmodel.RoomsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout

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

    private lateinit var filterView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.drawerLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

        setUserName()

        refreshLayout()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
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
        when(item.itemId) {
            R.id.filter -> {
                val view = View.inflate(this, R.layout.filter_raw, null)
                MaterialAlertDialogBuilder(this)
                    .setView(view)
                    .setTitle("Apply Filters")
                    .setPositiveButton("Apply") {_, _ ->

                        val city = if( view.findViewById<TextInputLayout>(R.id.input_city).editText?.text.toString() != "") {
                            view.findViewById<TextInputLayout>(R.id.input_city).editText?.text.toString()
                        } else {
                            null
                        }
                        val minRentAmount = if( view.findViewById<TextInputLayout>(R.id.input_min_rent_amount).editText?.text.toString() != "") {
                            view.findViewById<TextInputLayout>(R.id.input_min_rent_amount).editText?.text.toString().toInt()
                        } else {
                            null
                        }
                        val maxRentAmount = if( view.findViewById<TextInputLayout>(R.id.input_max_rent_amount).editText?.text.toString() != "") {
                            view.findViewById<TextInputLayout>(R.id.input_max_rent_amount).editText?.text.toString().toInt()
                        } else {
                            null
                        }
                        val filter = Filter(city, minRentAmount, maxRentAmount)
                        fetchRoomsDataAndSetAdapter(filter)
                    }
                    .setNegativeButton("Cancel") {_, _ ->

                    }
                    .show()
            }
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

    private fun fetchRoomsDataAndSetAdapter(filter: Filter? = null) {
        binding.rvRooms.layoutManager = LinearLayoutManager(this)
        val adapter = RoomRecyclerAdapter(emptyList(), this)
        binding.rvRooms.adapter = adapter

        roomViewModel.fetchAllRooms(filter)
        roomViewModel.allRoomsDetails.observe(this) {
            adapter.roomsData = it
            adapter.notifyDataSetChanged()
            binding.refreshLayout.isRefreshing = false
        }
    }

    private fun setUserName() {
        roomViewModel.getUserName()
        roomViewModel.userName.observe(this) {
            binding.navView.getHeaderView(0).findViewById<TextView>(R.id.userNameHeader).text = it
        }
    }

    private fun refreshLayout() {
        binding.refreshLayout.setOnRefreshListener {
            fetchRoomsDataAndSetAdapter()
        }
    }
}
