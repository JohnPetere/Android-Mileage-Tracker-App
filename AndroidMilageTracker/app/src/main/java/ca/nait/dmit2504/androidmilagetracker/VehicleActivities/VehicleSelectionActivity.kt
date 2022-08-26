package ca.nait.dmit2504.androidmilagetracker.VehicleActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.close
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import ca.nait.dmit2504.androidmilagetracker.MileagActvities.MileageMenuActivity
import ca.nait.dmit2504.androidmilagetracker.R
import ca.nait.dmit2504.androidmilagetracker.customListAdapters.vehicleCursorAdapter
import ca.nait.dmit2504.androidmilagetracker.trackerDatabase.milageTrackerDatabase

class VehicleSelectionActivity : AppCompatActivity() {

    private val trackerDB = milageTrackerDatabase(this)
    private val noVehicleAddButton: Button by lazy{findViewById(R.id.if_no_vehicles_vehicle_selection_add_vehicle_button)}
    private val ifVehicleAddButton:Button by lazy{findViewById(R.id.if_vehicles_vehicle_selection_add_vehicle_button)}
    private val vehicleListView: ListView by lazy{findViewById(R.id.vehicles_listView)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_selection)

        // if vehicle is not selected start another menu...
//https://stackoverflow.com/questions/4038268/android-onitemclick-only-returns-first-selected-item

        val vehicles = trackerDB.getListOfVehiclesObjects()

//        vehicles.isNullOrEmpty()
        showListViewLayout(!vehicles.isNullOrEmpty())

        // view vehicle and mileage stuff
        vehicleListView.onItemClickListener= AdapterView.OnItemClickListener{parent, view, position, id ->
            val intent = Intent(this, MileageMenuActivity::class.java) // change this when selected
            intent.putExtra("vehicleIDName", id)

            Toast.makeText(this, "you selected this $id, with the short Listener", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        }
        // edit car
        vehicleListView.onItemLongClickListener =  AdapterView.OnItemLongClickListener{parent, view, position, id ->
            Toast.makeText(this, "you selected $id, with the LongCLick LIstener", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AddEditVehicleActivity::class.java)
            intent.putExtra("vehicleIDName", id)
            intent.putExtra("editORnew", "edit")
            startActivity(intent)
            finish()
            true
        }
//https://coolors.co/3e78b2-1f2041-fffce8-53d8fb-0d0628


    }
    fun showListViewLayout(showListView: Boolean){
        if(showListView){
            vehicleListView.visibility = View.VISIBLE
            noVehicleAddButton.visibility = View.GONE
            ifVehicleAddButton.visibility = View.VISIBLE
            bindVehicleListView()
        }
        else{
            vehicleListView.visibility = View.GONE
            noVehicleAddButton.visibility = View.VISIBLE
            ifVehicleAddButton.visibility = View.GONE
        }

    }
    fun bindVehicleListView(){
        val curs = trackerDB.getListOFVehiclesCursor()
        val vehicleListViewAdapt = vehicleCursorAdapter(this, curs, 0)

        vehicleListView.adapter = vehicleListViewAdapt
    }

    fun addVehicleStart(view: View) {

        val intent = Intent(this, AddEditVehicleActivity::class.java)
        intent.putExtra("editORnew", "new")
        startActivity(intent)
    }
}