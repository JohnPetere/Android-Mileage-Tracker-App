package ca.nait.dmit2504.androidmilagetracker.VehicleActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import ca.nait.dmit2504.androidmilagetracker.R
import ca.nait.dmit2504.androidmilagetracker.trackerDatabase.customObjects.Vehicle
import ca.nait.dmit2504.androidmilagetracker.trackerDatabase.milageTrackerDatabase

class AddEditVehicleActivity : AppCompatActivity() {

    private var userInputWrongMessages:List<String> = emptyList()
    private val textViewTitle: TextView by lazy{findViewById(R.id.add_edit_text_title_textview)}
    private val yearEditText: EditText by lazy{findViewById(R.id.year_add_edit_edittext)}
    private val odoMeterEditText: EditText by lazy{findViewById(R.id.odometer_add_edit_edittext)}
    private val makeEditText: EditText by lazy{findViewById(R.id.make_add_edit_EditText)}
    private val modelEditText: EditText by lazy{findViewById(R.id.model_add_edit_Edittext)}
    private val trimEditText: EditText by lazy { findViewById(R.id.trim_add_edit_EditText) }
    private val addVehicleButton: Button by  lazy {findViewById(R.id.create_car_button_add_edit_button_vehicle)}
    private val updateCarButton: Button by lazy{findViewById(R.id.update_car_button_activity_add_edit_vehicle)}
    private val deleteCarButton: Button by lazy{findViewById(R.id.delete_car_button_activity_add_edit_vehicle)}
    private val db = milageTrackerDatabase(this)
    private var vehicleID  = -1
    private var vehicleObject: Vehicle = Vehicle()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_vehicle)
        val editOrNewVehicle =intent.getStringExtra("editORnew")
        ifEditMode(editOrNewVehicle == "edit")
              vehicleID = intent.getLongExtra("vehicleIDName",0).toInt()
            if(vehicleID != 0) {
                vehicleObject = db.getVehiclObjectByID(vehicleID)!!
                bindVehicleObj()

            }
        }

    fun bindVehicleObj() {
        odoMeterEditText.setText(vehicleObject.firstOdometerReading.toString())
        yearEditText.setText(vehicleObject.year.toString())
        makeEditText.setText(vehicleObject.make)
        modelEditText.setText(vehicleObject.model)
        trimEditText.setText(vehicleObject.trim)

    }
    fun ifEditMode(editMode: Boolean){
        // show edit/delete button if there is that mode
        if(editMode){
            textViewTitle.setText("Update Car Info")
           addVehicleButton.visibility = View.GONE
           deleteCarButton.visibility = View.VISIBLE
           updateCarButton.visibility = View.VISIBLE
        }
        else {
            textViewTitle.setText("Enter Car information")
            addVehicleButton.visibility = View.VISIBLE
            deleteCarButton.visibility = View.GONE
            updateCarButton.visibility = View.GONE
        }

    }
    fun checkInfoEntered(): Boolean{
        userInputWrongMessages = emptyList()
        val checkYear: Boolean = yearEditText.text.length != 4
        val checkOdo: Boolean = odoMeterEditText.text.length < 1
        val checkMake: Boolean = makeEditText.text.length < 2
        val checkModel: Boolean = modelEditText.text.length < 1
        if(checkYear){
            yearEditText.setError("Year Must be 4 digits long")
        }
        if (checkOdo){

            odoMeterEditText.setError("Odometer has to be greater then 1 or have input")
        }
        if (checkMake){
            makeEditText.setError("the car make has to be more then 2 letters")
        }
        if (checkModel){

            modelEditText.setError("the Car model has to be more then 1 letter")

        }
        if( checkYear || checkMake || checkModel || checkOdo ){
            return false
        }
        else{
            vehicleObject.firstOdometerReading = odoMeterEditText.text.toString().toDouble()
            vehicleObject.make = makeEditText.text.toString()
            vehicleObject.year  = yearEditText.text.toString().toInt()?:-1
            vehicleObject.model = modelEditText.text.toString()
            vehicleObject.trim = trimEditText.text.toString()
            return true
        }

    }

    fun updateVehicleOnClick(view: View) {
     Toast.makeText(this, "you clicked Save", Toast.LENGTH_SHORT).show()
        if(!checkInfoEntered()){
            Toast.makeText(this, "INFO IS BAD", Toast.LENGTH_SHORT).show()

        }
        else{
            //updateVehicle(vehicleID: Int, ODoReading:Double, year:Int, make:String, model:String, Trim: String): Int{
            val update = db.updateVehicle(vehicleObject.vehicleID?:-1,
                                            vehicleObject.firstOdometerReading,
                                            vehicleObject.year?:-1,
                                            vehicleObject.make?:"",
                                            vehicleObject.model?:"",
                                                    vehicleObject.trim?:"")
            val intent = Intent(this, VehicleSelectionActivity::class.java)
            startActivity(intent)
        }

    }
    fun onClickDeleteVehicle(view: View) {
        val del = milageTrackerDatabase(this).deleteVehicleByID(vehicleID)
        Toast.makeText(this, "you deleted vehicle: ${vehicleObject.year} ${vehicleObject.make} ${vehicleObject.model} ", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, VehicleSelectionActivity::class.java)
        startActivity(intent)
    }

    fun addVehicleClick(view: View) {
        if(checkInfoEntered()){
        //vehicleID: Int, ODoReading:Double, year:Int, make:String, model:String, Trim: String
            val create = db.addVehicle(vehicleObject.vehicleID?:-1,
                vehicleObject.firstOdometerReading,
                vehicleObject.year?:-1,
                vehicleObject.make?:"",
                vehicleObject.model?:"",
                vehicleObject.trim?:""

           )

            Toast.makeText(this, "you created vehicle: ${vehicleObject.year} ${vehicleObject.make} ${vehicleObject.model} ", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, VehicleSelectionActivity::class.java)
            startActivity(intent)

        }

    }


}