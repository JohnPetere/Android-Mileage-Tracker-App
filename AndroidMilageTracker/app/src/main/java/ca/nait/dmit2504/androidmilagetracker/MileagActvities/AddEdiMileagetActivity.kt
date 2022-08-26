package ca.nait.dmit2504.androidmilagetracker.MileagActvities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import ca.nait.dmit2504.androidmilagetracker.R
import ca.nait.dmit2504.androidmilagetracker.trackerDatabase.customObjects.Mileage
import ca.nait.dmit2504.androidmilagetracker.trackerDatabase.milageTrackerDatabase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

/*Auto set Date to NOW, alsos says "lazy NOt initilized yet*/
class AddEdiMileagetActivity : AppCompatActivity() {
    private val activityTitleTextVIew: TextView by lazy { findViewById(R.id.add_edit_text_title_textview_mileage) }
    private val gasFilledEditTxt: EditText by lazy { findViewById(R.id.gas_filled_Edit_text_Mileage) }
    private val distanceEditText: EditText by lazy { findViewById(R.id.distance_EditText_mileage) }
    private val editTextDateEditText: EditText by lazy { findViewById(R.id.editTextDate) }
    private val currentVehicleTextView: TextView by lazy { findViewById(R.id.current_vehicle_Add_edit_mileage_text_view) }
    private val mileageNoteEditText: EditText by lazy { findViewById(R.id.add_edit_mileaage_note_edit_Text) }
    private val currentLocationTextView: TextView by lazy { findViewById(R.id.current_location_Add_edit_mileage_text_view) }
    private val newOdom: EditText by lazy { findViewById(R.id.updated_Odometer_Add_Edit_Mileage_EditText) }
    private val addMileageButton: Button by lazy { findViewById(R.id.add_mileage_button_mileage_view) }
    private val updateMileageButton: Button by lazy { findViewById(R.id.update_mileage_Button_Mileaege_view) }
    private val deleteMileage: Button by lazy { findViewById(R.id.delete_mileage_Button_Mileaege_view) }
    private val ButtonbackToMilageMenu: Button by lazy {findViewById(R.id.back_to_Mileage_menu_button_Add_Edit_mileage)}
    private var dateCheckState: Boolean = true

    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var currentLocation : Location? = null
    lateinit var locationRequest: LocationRequest

    val REQUEST_CODE = 101
    var PERMISSION_ID = 1000

    private var mileageID: Int = -1
    private var vehicleID: Int = -1
    private var currentMileage: Mileage = Mileage()

    private var db = milageTrackerDatabase(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_mileage)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val editOrNew = intent.getStringExtra("mileageEditMode")


        ifEditMode(editOrNew?:"-1")
        mileageID = intent.getLongExtra("mileageIDName", 0).toInt()
        if (mileageID > 0) {
            currentMileage = db.getMileageEntryObject(mileageID)
            val vehicleName = db.getVehiclObjectByID(currentMileage.vehicleID ?: -1)
            if (vehicleName != null) {
                currentVehicleTextView.text =
                    "${vehicleName.year ?: ""}  ${vehicleName.make ?: ""} ${vehicleName.model ?: ""}"
            } else {
                currentVehicleTextView.text = "vehicle Selection failed"
            }
            bindMileageObj()

        }

    }

    private fun checkPermission():Boolean{
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
        return true
        }
        return false
    }
    fun fetchLocation(){
        if(checkPermission()){
            if(isLocationEnabled()){
                fusedLocationProviderClient!!.lastLocation.addOnCompleteListener {
                    task ->
                        var location:Location? = task.result
                        if(location == null){

                        }else{
                            currentLocationTextView.setText("LAt: ${location.latitude} Long: ${location.longitude}")
                            Toast.makeText(this, "${location.latitude} Long: ${location.longitude}", Toast.LENGTH_LONG).show()
                            currentMileage.location = "${location.latitude}, ${location.longitude}"
                        }
                }

            }else{
                Toast.makeText(this, "Please allow location Permission TO record Location", Toast.LENGTH_SHORT).show()
            }

        }else{
            requestPermission()
        }

    }
    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION ), PERMISSION_ID

        )
    }

    private fun isLocationEnabled(): Boolean{
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    )
    {
        when(requestCode){
            REQUEST_CODE ->{
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "you have permission!", Toast.LENGTH_LONG).show()
                    fetchLocation()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    fun ifEditMode(editMode: String) {

        // "edit"
        if (editMode == "edit") {
            activityTitleTextVIew.text = "Edit Mileage Record"
            addMileageButton.visibility = View.GONE
            updateMileageButton.visibility = View.VISIBLE
            deleteMileage.visibility = View.VISIBLE
            ButtonbackToMilageMenu.visibility = View.GONE


        } else if(editMode=="new") {
            activityTitleTextVIew.text = "Add Mileage Record"
            addMileageButton.visibility = View.VISIBLE
            updateMileageButton.visibility = View.GONE
            deleteMileage.visibility = View.GONE
            ButtonbackToMilageMenu.visibility = View.GONE
        //intent.putExtra("vehicleIDName", vehicleID)
            currentMileage.vehicleID = intent.getIntExtra("vehicleIDName", -1)

        }
        else{
            activityTitleTextVIew.text = "View Mileage Record"
            addMileageButton.visibility = View.GONE
            updateMileageButton.visibility = View.GONE
            deleteMileage.visibility = View.GONE
            ButtonbackToMilageMenu.visibility = View.VISIBLE
            //intent.putExtra("vehicleIDName", vehicleID)
            currentMileage.vehicleID = intent.getIntExtra("vehicleIDName", -1)

        }
    }
    fun bindMileageObj() {
        gasFilledEditTxt.setText(currentMileage.gasFilled.toString())
        distanceEditText.setText(currentMileage.distance.toString())

        editTextDateEditText.setText(currentMileage.dateString.toString())

        mileageNoteEditText.setText(currentMileage.notes.toString() ?: "")
        newOdom.setText(currentMileage.newOdometer.toString() ?: "")


    }
    fun checkDateFormat(dateCheck: String):Boolean { //yyyy-mm-dd
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val dateCheck: Date = formatter.parse(dateCheck)
            dateCheckState = true
            return true
        } catch (e: ParseException) {
            dateCheckState = false
            return false
        }
    }
    fun checkInfoEntered(): Boolean {
        val checkGas: Boolean = gasFilledEditTxt.text.isEmpty() || gasFilledEditTxt.text.toString().toDouble() <= 0
        val checkDistance: Boolean = distanceEditText.text.isEmpty() || distanceEditText.text.toString().toDouble() <= 0
        val checkDate: Boolean = checkDateFormat(editTextDateEditText.text.toString()) || editTextDateEditText.text.isEmpty()
        if (checkGas) {
            gasFilledEditTxt.setError("Enter gas > 0")
        }
        if (checkDistance) {
            distanceEditText.setError("Enter Distance > 0")
        }

        if (!checkDate) {
            // accepts 2099-99-99
            editTextDateEditText.setError("Enter Date: yyyy-mm-dd")
        }
        return !checkGas && !checkDistance  && checkDate// && dateCheckState
    }
    fun bindNewInfo(){
        currentMileage.notes = mileageNoteEditText.text.toString()
        currentMileage.gasFilled = gasFilledEditTxt.text.toString().toDouble()
        currentMileage.distance = distanceEditText.text.toString().toDouble()
        currentMileage.dateString = editTextDateEditText.text.toString()
        if(editTextDateEditText.text.isEmpty()){
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val timeStamp = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneOffset.UTC).format(Instant.now())
            currentMileage.dateString = timeStamp.toString()
        }
        else {
            currentMileage.dateString = editTextDateEditText.text.toString()
        }
            currentMileage.location = currentLocationTextView.text.toString()
        if (!newOdom.text.isEmpty()){
            currentMileage.newOdometer = newOdom.text.toString().toDouble()

        }

    }
    fun AddMileageOnClick(view: View) {
        if(checkInfoEntered()){
            bindNewInfo()
            val create = db.createMilageEntry(currentMileage)
            // why is vehicleID being sent in as 0?
            val intent = Intent(this, MileageMenuActivity::class.java)
            val vehicleIDSent = (currentMileage.vehicleID?:-2).toLong()
            Toast.makeText(this, "Added Mileage", Toast.LENGTH_LONG)
            intent.putExtra("vehicleIDName", vehicleIDSent)
            startActivity(intent)

        }
        else{
            Toast.makeText(this, "INput Invalid", Toast.LENGTH_LONG).show()
        }
    }
    fun updateMileageOnClick(view: View) {
        if(checkInfoEntered()){
            bindNewInfo()
            val update = db.updateMilageEntry(currentMileage)
            val intent = Intent(this, MileageMenuActivity::class.java)
            val vehicleIDSent = (currentMileage.vehicleID?:-2).toLong()
            Toast.makeText(this, "Updated Mileage", Toast.LENGTH_LONG)
            intent.putExtra("vehicleIDName", vehicleIDSent)
            startActivity(intent)


        }
        else{
            Toast.makeText(this, "Cannot Save", Toast.LENGTH_LONG).show()
        }
    }
    fun onClickDeleteMileage(view: View) {
        val del = db.deleteMileageEntryByMileageID(currentMileage.mileageID?:-1)
        //vehicleID = intent.getLongExtra("vehicleIDName", -1).toInt()

        val intent = Intent(this, MileageMenuActivity::class.java)
        val vehicleIDSent = (currentMileage.vehicleID?:-2).toLong()
        Toast.makeText(this, "Deleted Mileage", Toast.LENGTH_LONG).show()
        intent.putExtra("vehicleIDName", vehicleIDSent)
        startActivity(intent)

    }
    fun addCurrentLocationOnClock(view: View) {
        var curruntLocation: Location? = null
        var fusedLocationProvider:FusedLocationProviderClient? = null
        val Request_Code = 101
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()

    }

    fun onClickBackToViewButton(view: View) {
        val intent = Intent(this, MileageMenuActivity::class.java)
        val vehicleIDSent = (currentMileage.vehicleID?:-2).toLong()

        intent.putExtra("vehicleIDName", vehicleIDSent)
        startActivity(intent)

    }


}