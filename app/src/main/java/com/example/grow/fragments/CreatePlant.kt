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

        view.care_days_group.setOnClickListener {
            dropsChoosing()
        }

        return view
    }

    private fun insertDataToDb() {
        val name = etPlantName.text.toString()
        val watering = dropsChoosing()
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

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return super.onOptionsItemSelected(item)
//    }
//
//    private fun parseWatering(watering: String): Watering {
//        return when(watering){
//            "1 drop" -> {Watering.LOW}
//            "2 drops" -> {Watering.MEDIUM}
//            "3 drops" -> {Watering.ALOT}
//            else -> {Watering.LOW}
//
//        }
//    }

    private fun dropsChoosing(): Int {
        var wateringUser = 0
        val dropFullID = resources.getIdentifier("ic_drop_full", "drawable", "res")
        toggle_drop1.setOnClickListener {
            wateringUser = 1
            it.setBackgroundResource(dropFullID)
            }
        toggle_drop2.setOnClickListener {
            wateringUser = 2
            toggle_drop1.setBackgroundResource(dropFullID)
            it.setBackgroundResource(dropFullID)
        }
        toggle_drop3.setOnClickListener {
            wateringUser = 3
            it.setBackgroundResource(dropFullID)
        }
        println(wateringUser)
        return wateringUser
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
    }}

