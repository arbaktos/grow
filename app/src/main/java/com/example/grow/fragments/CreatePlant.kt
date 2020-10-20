package com.example.grow.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ToggleButton
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.grow.R
import com.example.grow.data.PlantViewModel
import com.example.grow.data.models.PlantData
import com.example.grow.data.models.Watering
import kotlinx.android.synthetic.main.fragment_create_plant.*
import kotlinx.android.synthetic.main.fragment_create_plant.view.*

class CreatePlant : Fragment() {

    private val viewModel: PlantViewModel by viewModels()
    private var wateringUser = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_plant, container, false)

        setHasOptionsMenu(true)

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


    private fun insertDataToDb() {
        val name = etPlantName.text.toString()
        val watering = wateringUser
        val caredays = checkCareDays()
        val additionalcare = additional_care.text.toString()
        val validation = checkDataFromUser(caredays)
        if(validation){
            //insert data to database
            val newData = PlantData(
                0,
                name,
                watering,
                caredays,
                additionalcare
            )
            viewModel.insertData(newData)

            Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_SHORT).show() //TODO what is requireContext()??
            findNavController().navigate(R.id.action_createPlant_to_plantList)
            println(caredays)
        }else{
            Toast.makeText(requireContext(), "Please fill in watering days", Toast.LENGTH_SHORT).show()
        }

    }

    private fun checkDataFromUser(careDays:String): Boolean{
        return !careDays.isEmpty()
    }

    private fun checkCareDays(): String {
        val buttonList = listOf<ToggleButton>(mo_btn, tu_btn, we_btn, th_btn, fr_btn, sa_btn, su_btn)
        var careDaysList = ""
        for (button in buttonList){
            if(button.isChecked){
                careDaysList += button.text.toString()
            }
        }
        return careDaysList
    }

}

