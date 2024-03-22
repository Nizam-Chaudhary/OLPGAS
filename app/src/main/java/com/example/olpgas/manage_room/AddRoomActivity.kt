package com.example.olpgas.manage_room

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.olpgas.R
import com.example.olpgas.databinding.ActivityAddRoomBinding

class AddRoomActivity : AppCompatActivity() {
    private val binding: ActivityAddRoomBinding by lazy {
        ActivityAddRoomBinding.inflate(layoutInflater)
    }

    private val GALLERY_REQUEST_CODE = 1
    private val PERMISSION_REQUEST_EXTERNAL_STORAGE = 2
    private val PERMISSION_REQUEST_MEDIA_IMAGES = 3
    private var hasPermission = false

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.scrollView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpSpinner()

        binding.myRoomAddImage.setOnClickListener {
            getImages()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getImages() {
        if (!hasPermission) {
            getImagesPermission()
        } else {
            picImages()
        }
    }

    private fun picImages() {
        val intentGallery = Intent(Intent.ACTION_PICK)
        intentGallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intentGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intentGallery, GALLERY_REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getImagesPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_EXTERNAL_STORAGE
                )
            } else {
                picImages()
            }
        }
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                PERMISSION_REQUEST_MEDIA_IMAGES
            )
        } else {
            picImages()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            hasPermission = true
            picImages()
        }
        if (requestCode == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            hasPermission = true
            picImages()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            if (requestCode == GALLERY_REQUEST_CODE) {
                if (data != null) {
                    if (data.clipData != null) {
                        val clipData = data.clipData
                        val imageUris = mutableListOf<Uri>()
                        if (clipData != null) {
                            for (i in 0 until clipData.itemCount) {
                                val imageUri = clipData.getItemAt(i).uri
                                imageUris.add(imageUri)
                                Toast.makeText(this, i.toString(), Toast.LENGTH_SHORT).show()
                            }
                            binding.myRoomImages.setImageURI(imageUris[0])
                            binding.myRoomImages2.setImageURI(imageUris[1])
                        }

                    } else {
                        val imageUri = data.data
                        binding.myRoomImages.setImageURI(imageUri)
                    }
                } else {

                    Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun setUpSpinner() {

        val stateAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.indian_states,
            android.R.layout.simple_spinner_item
        )
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.myRoomAddressState.adapter = stateAdapter

        // Create array adapter for city spinner (initially empty)
        val cityAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item
        )
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.myRoomAddressCity.adapter = cityAdapter

        // Set up on item selected listener for state spinner
        binding.myRoomAddressState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                // Clear previous city selection
                cityAdapter.clear()

                // Get selected state
                val selectedState = parent?.getItemAtPosition(position).toString()

                // Populate cities based on the selected state
                val citiesArrayId = resources.getIdentifier(
                    selectedState.lowercase().replace(" ", "_") + "_cities",
                    "array",
                    packageName
                )
                val citiesArray = resources.getStringArray(citiesArrayId)
                cityAdapter.addAll(citiesArray.toList())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }



        val roomTypeAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,resources.getStringArray(R.array.room_type))
        binding.myRoomType.adapter=roomTypeAdapter

        val roomSuitableAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,resources.getStringArray(R.array.room_suitableFor))
        binding.myRoomSuitableFor.adapter=roomSuitableAdapter

    }
}