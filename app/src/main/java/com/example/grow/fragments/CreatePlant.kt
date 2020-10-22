package com.example.grow.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.grow.R
import com.example.grow.data.PlantViewModel
import com.example.grow.data.models.PlantData
import com.example.grow.data.models.Watering
import kotlinx.android.synthetic.main.fragment_create_plant.*
import kotlinx.android.synthetic.main.fragment_create_plant.view.*
import java.io.File

private const val FILE_NAME = "photo.jpg"
private const val REQUEST_CODE = 42
private lateinit var photoFile: File


class CreatePlant : Fragment() {

    private val viewModel: PlantViewModel by viewModels()
    private var wateringUser = 0

    fun makeToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_plant, container, false)

        setHasOptionsMenu(true)

        view.plantImage.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)

            //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)
            val fileProvider = FileProvider.getUriForFile(requireContext(), "com.example.grow.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CODE)

            } else {
                makeToast("Unable to open camera")
            }
        }


        view.save_button.setOnClickListener() {
            checkCareDays()
            insertDataToDb()
        }

        val dropFull = R.drawable.ic_drop_full
        val dropEmpty = R.drawable.ic_droplet_empty

        view.toggle_drop1.setOnClickListener {
            wateringUser = 1
            it.setBackgroundResource(dropFull)
            toggle_drop2.setBackgroundResource(dropEmpty)
            toggle_drop3.setBackgroundResource(dropEmpty)

        }
        view.toggle_drop2.setOnClickListener {
            wateringUser = 2
            toggle_drop1.setBackgroundResource(dropFull)
            it.setBackgroundResource(dropFull)
            toggle_drop3.setBackgroundResource(dropEmpty)
        }
        view.toggle_drop3.setOnClickListener {
            wateringUser = 3
            toggle_drop1.setBackgroundResource(dropFull)
            toggle_drop2.setBackgroundResource(dropFull)
            it.setBackgroundResource(dropFull)

        }
        return view
    }

    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)

    }

    private fun insertDataToDb() {
            val name = etPlantName.text.toString()
            val watering = wateringUser
            val caredays = checkCareDays()
            val additionalcare = additional_care.text.toString()
            val validation = checkDataFromUser(caredays)
            if (validation) {
                //insert data to database
                val newData = PlantData(
                    0,
                    name,
                    watering,
                    caredays,
                    additionalcare
                )
                viewModel.insertData(newData)

                Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_SHORT)
                    .show() //TODO what is requireContext()??
                findNavController().navigate(R.id.action_createPlant_to_plantList)
                println(caredays)
            } else {
                Toast.makeText(requireContext(), "Please fill in watering days", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        private fun checkDataFromUser(careDays: String): Boolean {
            return careDays.isNotEmpty()
        }

        private fun checkCareDays(): String {
            val buttonList =
                listOf<ToggleButton>(mo_btn, tu_btn, we_btn, th_btn, fr_btn, sa_btn, su_btn)
            var careDaysList = ""
            for (button in buttonList) {
                if (button.isChecked) {
                    careDaysList += button.text.toString()
                }
            }
            return careDaysList
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                //val takenImage = data?.extras?.get("data") as Bitmap

                val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
                plantImage.setImageBitmap(takenImage)
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }


        }

    }


