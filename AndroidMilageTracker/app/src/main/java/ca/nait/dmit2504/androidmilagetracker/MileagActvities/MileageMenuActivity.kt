package ca.nait.dmit2504.androidmilagetracker.MileagActvities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import ca.nait.dmit2504.androidmilagetracker.R
import ca.nait.dmit2504.androidmilagetracker.customListAdapters.MileageListCursorAdapter
import ca.nait.dmit2504.androidmilagetracker.trackerDatabase.milageTrackerDatabase

/*Things
1) Finish Add/Edit Mileage Feature
2) Location Feature
3) View Location Feature On Google Maps*/
class MileageMenuActivity : AppCompatActivity() {
    private val mileageListView : ListView by lazy{findViewById(R.id.mileage_list_view_MileageMenu_activity)}
    private val addFuelRecord: Button by lazy{findViewById(R.id.add_fuel_record_button_mmileage_menu_actvity)}
    private val metricImperialSwitch: Switch by lazy{findViewById(R.id.metric_imperial_switch_view_mileage_activity)}
    private var vehicleID: Int = -1
    val trackerdb = milageTrackerDatabase(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mileage_menu)

       bindMileage()
        metricImperialSwitch.setOnCheckedChangeListener {
            _, isChecked->
            mileageListView
            bindMileage()

        }
        // only selecting first item
        mileageListView.onItemLongClickListener = AdapterView.OnItemLongClickListener{
                parent, view, position, id ->
            // edit Mileage
            intent = Intent(this, AddEdiMileagetActivity::class.java) // CHANGE THIS
            intent.putExtra("mileageIDName", id)
            intent.putExtra("mileageEditMode", "edit")
            // if any selected, make this the vehicle
            startActivity(intent)
            Toast.makeText(this,"You Selected Milage: $id", Toast.LENGTH_SHORT).show()
            true
        }
        mileageListView.onItemClickListener = AdapterView.OnItemClickListener{
                parent, view, position, id ->
            // view Mileage
            /*WOrks and is able to get first index in and carry database if the first one is selected,
            * my guess it is a problem with the query database. It's selecting only a non existent row*/
            intent = Intent(this, AddEdiMileagetActivity::class.java)
            intent.putExtra("mileageIDName", id)
            // if any selected, make this the vehicle
            intent.putExtra("mileageEditMode", "view")
            startActivity(intent)
            Toast.makeText(this,"You Selected Milage: $id", Toast.LENGTH_SHORT).show()
            true
        }

        // add mileage button to mileage screen
    }
    fun bindMileage(){
            vehicleID = intent.getLongExtra("vehicleIDName", -1).toInt()
        if(vehicleID > 0){
            /*Provlem I was using getMileageEntryCursor
            * I "fixed it by selecting by vehicleID in the defintions
            * I should have been using getMileageEntrysByVehicleIDCursor instead, and not touched the getMileageEntry*/
            val curs = trackerdb.getMileageEntrysByVehicleIDCursor(vehicleID)
                // WHY only selecting one mileage?
                var mileageCursorAdapter = MileageListCursorAdapter(this, curs, 0)
                MileageListCursorAdapter.isMetric.switchState = metricImperialSwitch.isChecked
             mileageListView.adapter = MileageListCursorAdapter(this, curs, 0)
            Toast.makeText(this, "vehicleID $vehicleID", Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(this, "vehicle Not Selected from main page", Toast.LENGTH_LONG).show()

        }
    }

    fun goToAddMileageButtonOnClick(view: View) {
        intent = Intent(this, AddEdiMileagetActivity::class.java) // CHANGE THIS
        intent.putExtra("vehicleIDName", vehicleID)
        intent.putExtra("mileageEditMode", "new")
        startActivity(intent)


    }

}