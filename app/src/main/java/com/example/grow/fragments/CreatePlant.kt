package com.example.grow.fragments

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.findNavController
import com.example.grow.R
import com.example.grow.data.PlantViewModel
import com.example.grow.data.models.PlantData
import com.example.grow.data.models.Watering
import kotlinx.android.synthetic.main.fragment_create_plant.*

class CreatePlant : Fragment() {

    val viewModel = PlantViewModel(application = Application())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_plant, container, false)

        setHasOptionsMenu(true)

        save_button.setOnClickListener {
            checkCareDays()
            insertDatatoDb()
            it.findNavController().navigate(R.id.action_createPlant_to_plantList)
        }

        return view
    }

    private fun insertDatatoDb() {
        val name = etPlantName.text.toString()
        val watering = spinner_watering.selectedItem.toString()
        val caredays = checkCareDays()
        val additionalcare = additional_care.text.toString()
        val validation = checkDataFromUser(caredays)
        if(validation){
            //insert data to database
            val newData = PlantData(
                0,
                name,
                parseWatering(watering),
                caredays,
                additionalcare
            )
        }
    }

    private fun checkDataFromUser(careDays:MutableList<String>): Boolean{
        return careDays.isNotEmpty()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    private fun parseWatering(watering: String): Watering {
        return when(watering){
            "1 drop" -> {Watering.LOW}
            "2 drops" -> {Watering.MEDIUM}
            "3 drops" -> {Watering.ALOT}
            else -> {Watering.LOW}

        }
    }

    private fun checkCareDays(): MutableList<String> {
        val careDaysList = mutableListOf<String>()
        when {
            mo_btn.isChecked -> careDaysList.add(mo_btn.text.toString())
            tu_btn.isChecked -> careDaysList.add(tu_btn.text.toString())
            we_btn.isChecked -> careDaysList.add(we_btn.text.toString())
            th_btn.isChecked -> careDaysList.add(th_btn.text.toString())
            fr_btn.isChecked -> careDaysList.add(fr_btn.text.toString())
            sa_btn.isChecked -> careDaysList.add(sa_btn.text.toString())
            su_btn.isChecked -> careDaysList.add(su_btn.text.toString())
        }
        return careDaysList
    }


}