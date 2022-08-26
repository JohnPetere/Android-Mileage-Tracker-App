package ca.nait.dmit2504.androidmilagetracker.trackerDatabase

import android.content.Context
import android.content.ContentValues

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

import ca.nait.dmit2504.androidmilagetracker.trackerDatabase.customObjects.Mileage
import ca.nait.dmit2504.androidmilagetracker.trackerDatabase.customObjects.Vehicle
import ca.nait.dmit2504.androidmilagetracker.trackerDatabase.customObjects.VehicleNameAndID

object milageDBContract{
    object vehicleEntry{
        const val TABLE_NAME_VEHICLE = "vehicles"
        const val TABLE_COLUMN_NAME_VEHICLE_ID_primary_key = "_id" // was vehicleID
        const val TABLE_COLUMN_NAME_FIRST_ODOMETER_READING = "firstOdometerReading"
        const val TABLE_COLUMN_NAME_YEAR = "year"
        const val TABLE_COLUMN_NAME_MAKE = "make"
        const val TABLE_COLUMN_NAME_MODEL = "model"
        const val TABLE_COLUMN_NAME_TRIM = "trim"
    }
    object milageEntry{
        const val TABLE_NAME_milage = "milage"
        const val TABLE_COLUMN_MILAGE_ID_primary_key = "_id" // was milageID
        const val TABLE_COLUMN_VEHICLE_ID_foreign = "vehicleID"
        const val TABLE_COLUMN_NEW_ODOMETER = "newOdometer"
        const val TABLE_COLUMN_GAS_FILLED = "gasFilled"
        const val TABLE_COLUMN_DISTANCE = "distance"
        const val TABLE_COLUMN_DATE = "date"
        const val TABLE_COLUMN_NAME_NOTES = "notes"
        const val TABLE_COLUMN_LOCATION = "location"


    }

}
/*feature to add if time
* add a feature where it checks the last Odeometer reading, so getLastMilageOdemeter, and if it is bigger then the one entered, it will throw an error
* add a Locations table
*add queries instead of going through a list
* */
class milageTrackerDatabase(context: Context)  : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)  {
    companion object{
        private const val DATABASE_NAME = "Lists.db"
        private const val DATABASE_VERSION = 1
        private const val COMMA_SEPARATOR = ","
        private const val TEXT_TYPE = " TEXT"
        private const val INT_TYPE = " INTEGER"
        private const val NULL = " NULL"
        private const val NOT_NULL = " NOT NULL"
//INSERT INTO milage(vehicleID, newOdometer, gasFilled, date, notes) values( 2,192000, 12,DATE('now'), "SPed Lots" )
        private const val SQL_CREATE_TABLE_MILAGE =
            // add KM travelled and also subtract from the previous one
            "CREATE TABLE ${milageDBContract.milageEntry.TABLE_NAME_milage} (" +
             "${milageDBContract.milageEntry.TABLE_COLUMN_MILAGE_ID_primary_key}" + INT_TYPE + NOT_NULL+ COMMA_SEPARATOR +
             "${milageDBContract.milageEntry.TABLE_COLUMN_VEHICLE_ID_foreign}" +  INT_TYPE+ NOT_NULL+ COMMA_SEPARATOR +
             "${milageDBContract.milageEntry.TABLE_COLUMN_NEW_ODOMETER} DECIMAL(9,1)" + NULL +  COMMA_SEPARATOR +
             "${milageDBContract.milageEntry.TABLE_COLUMN_GAS_FILLED} DECIMAL(9,2)" + NOT_NULL + COMMA_SEPARATOR +
             "${milageDBContract.milageEntry.TABLE_COLUMN_DATE} DATE" + NOT_NULL+ COMMA_SEPARATOR +
             "${milageDBContract.milageEntry.TABLE_COLUMN_NAME_NOTES}"+ TEXT_TYPE + NULL + COMMA_SEPARATOR +
             "${milageDBContract.milageEntry.TABLE_COLUMN_LOCATION}" + TEXT_TYPE + NULL + COMMA_SEPARATOR +
             "${milageDBContract.milageEntry.TABLE_COLUMN_DISTANCE} DECIMAL(6,2)" + NOT_NULL + COMMA_SEPARATOR+
             "PRIMARY KEY (${milageDBContract.milageEntry.TABLE_COLUMN_MILAGE_ID_primary_key})"+ COMMA_SEPARATOR +
             "FOREIGN KEY (${milageDBContract.milageEntry.TABLE_COLUMN_VEHICLE_ID_foreign})" +
             "REFERENCES ${milageDBContract.vehicleEntry.TABLE_NAME_VEHICLE}(${milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_VEHICLE_ID_primary_key}) "+
             ");"

        private const val SQL_CREATE_TABLE_VEHICLE = " CREATE TABLE ${milageDBContract.vehicleEntry.TABLE_NAME_VEHICLE} ("+
                "${milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_VEHICLE_ID_primary_key}" + INT_TYPE + NOT_NULL + " PRIMARY KEY" + COMMA_SEPARATOR +
                "${milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_FIRST_ODOMETER_READING} DECIMAL(9,1)" + NOT_NULL + COMMA_SEPARATOR +
                "${milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_YEAR} DECIMAL(4,0)" + NOT_NULL + COMMA_SEPARATOR +
                "${milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_MAKE}" + TEXT_TYPE + NOT_NULL + COMMA_SEPARATOR +
                "${milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_MODEL}" + TEXT_TYPE + NOT_NULL + COMMA_SEPARATOR +
                "${milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_TRIM}" + TEXT_TYPE + NULL +
               ");"
        private val SQL_DROP_TABLE_MILAGE = "DROP TABLE ${milageDBContract.milageEntry.TABLE_NAME_milage}"
        private val SQL_DROP_TABLE_VEHICEL = "DROP TABLE ${milageDBContract.vehicleEntry.TABLE_NAME_VEHICLE}"


    }
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_VEHICLE)
        db?.execSQL(SQL_CREATE_TABLE_MILAGE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DROP_TABLE_VEHICEL)
        db?.execSQL(SQL_CREATE_TABLE_MILAGE)
        onCreate(db)
    }

    fun getListOFVehiclesCursor(): Cursor {
        var projection = arrayOf(
                milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_VEHICLE_ID_primary_key,

                milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_FIRST_ODOMETER_READING,
                milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_YEAR,
                milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_MAKE,
                milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_MODEL,
                milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_TRIM

            )
//
        val selection: String? = null
        val selectionArgs: Array<String>? = null
        val groupBy: String? = null
        val having: String? = null
        val sortLists: String? = null
        val db = writableDatabase

        var resultCursor = db.query(
            milageDBContract.vehicleEntry.TABLE_NAME_VEHICLE,
            projection,
            selection,
            selectionArgs,
            groupBy,
            having,
            sortLists
        )

        return resultCursor
    }
    fun getListOfVehiclesObjects():List<Vehicle>?{
        val queryResultList = mutableListOf<Vehicle>()
        val queryResultCursor = getListOFVehiclesCursor()
        queryResultCursor.moveToFirst()
        while(!queryResultCursor.isAfterLast){
            val currentVehicle = Vehicle()
            currentVehicle.vehicleID = queryResultCursor.getInt(queryResultCursor.getColumnIndexOrThrow(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_VEHICLE_ID_primary_key))
            currentVehicle.firstOdometerReading = queryResultCursor.getDouble(queryResultCursor.getColumnIndexOrThrow(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_FIRST_ODOMETER_READING))
            currentVehicle.year = queryResultCursor.getInt(queryResultCursor.getColumnIndexOrThrow(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_YEAR))
            currentVehicle.make = queryResultCursor.getString(queryResultCursor.getColumnIndexOrThrow(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_MAKE))
            currentVehicle.model = queryResultCursor.getString(queryResultCursor.getColumnIndexOrThrow(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_MODEL))
            currentVehicle.trim = queryResultCursor.getString(queryResultCursor.getColumnIndexOrThrow(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_TRIM))
            queryResultList.add(currentVehicle)
            queryResultCursor.moveToNext()
            // infinite loop...
        }
        queryResultCursor.close()
        return queryResultList
    }
    fun getListOfVehicleNames(): List<String> {
        var vehicleNameList: List<String> = emptyList()

        getListOfVehiclesObjects()?.forEach {
            vehicleNameList += "${it.year.toString() ?: ""} ${it.make.toString() ?: ""} ${it.model.toString() ?: ""} + ${it.trim.toString() ?: ""}"
        }
        return vehicleNameList
    }
    fun getListOfVehiclesNamesAndID(): List<VehicleNameAndID>{
        var vehicleNameAndIDList = mutableListOf<VehicleNameAndID>()

        getListOfVehiclesObjects()?.forEach {
            var nameAndID = VehicleNameAndID()
            nameAndID.vehicleID = it.vehicleID?:-1
            nameAndID.vehicleName = "${it.year.toString() ?: ""} ${it.make.toString() ?: ""} ${it.model.toString() ?: ""} + ${it.trim.toString() ?: ""}"
            vehicleNameAndIDList.add(nameAndID)
        }
        return vehicleNameAndIDList

    }
    fun getVehicleNameByID(vehicleID: Int): String{
        TODO("Refrence getVehicleBYID")
        /**
         * Returns list of vehicle Name in a string
         * Name: String = Year + Make + Model + Trim
         *   EX: 2002 Dodge Dakota Sport
         *  @param vehicleID vehicleID to pass in
         *  @return Year + Make + Model + Trim
         */
        val listOfVehicles = getListOfVehicleNames()
            // this won't work
        return listOfVehicles[vehicleID]
    }
    fun getVehiclObjectByID(vehicleID:Int): Vehicle? {

        val vehicleEntries = getListOfVehiclesObjects()
        var refVehicleID = 0
        var count = 0;

        val vehicleOBJ = vehicleEntries?.filter { it.vehicleID == vehicleID }

        return vehicleOBJ?.first()

    }
    fun deleteVehicleByID(vehicleID: Int): Int{
    // delete Mileage byVehicle First
        deleteMileageByVehicle(vehicleID)
        val selection = "${milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_VEHICLE_ID_primary_key} = ?"
        val selectionArgs = arrayOf(vehicleID.toString())

        return writableDatabase.delete(milageDBContract.vehicleEntry.TABLE_NAME_VEHICLE, selection, selectionArgs)

    }
    fun deleteMileageByVehicle(vehicleID: Int): Int{
        val selection = "${milageDBContract.milageEntry.TABLE_COLUMN_VEHICLE_ID_foreign} = ?"
        val selectionArgs = arrayOf(vehicleID.toString())

        return writableDatabase.delete(milageDBContract.milageEntry.TABLE_NAME_milage, selection, selectionArgs)
    }
    fun deleteMileageByMileageID(mileageID: Int): Int{
        val selection = "${milageDBContract.milageEntry.TABLE_COLUMN_VEHICLE_ID_foreign} = ?"
        val selectionArgs = arrayOf(mileageID.toString())

        return writableDatabase.delete(milageDBContract.milageEntry.TABLE_NAME_milage, selection, selectionArgs)

    }
    fun addVehicle(vehicleID: Int, ODoReading:Double, year:Int, make:String, model:String, Trim: String): Long{
        var db = writableDatabase
        val values = ContentValues().apply {
            put(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_FIRST_ODOMETER_READING, ODoReading)
            put(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_YEAR, year)
            put(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_MAKE, make)
            put(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_MODEL, model)
            put(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_TRIM, Trim)
        }
        return db.insert(milageDBContract.vehicleEntry.TABLE_NAME_VEHICLE, null, values)
    }
    fun addVehicle(ODoReading: Double, year:Int, make:String, model:String): Long{
        var db = writableDatabase
        val values = ContentValues().apply {
            put(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_FIRST_ODOMETER_READING, ODoReading)
            put(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_YEAR, year)
            put(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_MAKE, make)
            put(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_MODEL, model)
        }
        return db.insert(milageDBContract.vehicleEntry.TABLE_NAME_VEHICLE, null, values)
    }

    fun updateVehicle(vehicleID: Int, ODoReading:Double, year:Int, make:String, model:String, Trim: String): Int{
        var db = writableDatabase
        val values = ContentValues().apply {
            put(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_VEHICLE_ID_primary_key,vehicleID)
            put(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_FIRST_ODOMETER_READING, ODoReading)
            put(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_YEAR, year)
            put(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_MAKE, make)
            put(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_MODEL, model)
            put(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_TRIM, Trim)
        }
        val whereClause = milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_VEHICLE_ID_primary_key + " =?"
        return db.update(milageDBContract.vehicleEntry.TABLE_NAME_VEHICLE, values, whereClause, arrayOf(vehicleID.toString()))
    }
    fun updateVehicle(vehicleID: Int, ODoReading:Double, year:Int, make:String, model:String): Int{
        var db = writableDatabase
        val values = ContentValues().apply {
            put(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_VEHICLE_ID_primary_key,vehicleID)
            put(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_FIRST_ODOMETER_READING, ODoReading)
            put(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_YEAR, year)
            put(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_MAKE, make)
            put(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_MODEL, model)
        }
        val whereClause = milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_VEHICLE_ID_primary_key + " =?"
        return db.update(milageDBContract.vehicleEntry.TABLE_NAME_VEHICLE, values, whereClause, arrayOf(vehicleID.toString()))
    }
    fun getMileageEntrysByVehicleIDCursor(vehicleID: Int): Cursor{
        var queryResults: Mileage

        var projection = arrayOf(
            milageDBContract.milageEntry.TABLE_COLUMN_VEHICLE_ID_foreign,
            milageDBContract.milageEntry.TABLE_COLUMN_MILAGE_ID_primary_key,
            milageDBContract.milageEntry.TABLE_COLUMN_DISTANCE,
            milageDBContract.milageEntry.TABLE_COLUMN_GAS_FILLED,
            milageDBContract.milageEntry.TABLE_COLUMN_LOCATION,
            milageDBContract.milageEntry.TABLE_COLUMN_DATE,
            milageDBContract.milageEntry.TABLE_COLUMN_NEW_ODOMETER,
            milageDBContract.milageEntry.TABLE_COLUMN_NAME_NOTES

        )
        val selection: String? = "${milageDBContract.milageEntry.TABLE_COLUMN_VEHICLE_ID_foreign} = ?"
        // where CLAUSE
        val selectionArgs: Array<String>? = arrayOf(vehicleID.toString())
        val groupBy: String? = null
        val having: String? = null
        val sortLists: String? = null
        val db = writableDatabase
        var resultCursor: Cursor =
            db.query(
                milageDBContract.milageEntry.TABLE_NAME_milage,
                projection,
                selection,
                selectionArgs,
                groupBy,
                having,
                sortLists
            )
        return resultCursor
    }
    fun getMileageEntrysByVehicleObject(_ID: Int): List<Mileage>{
        val mileageResultsCursor = getMileageEntrysByVehicleIDCursor(_ID)
        val queryResultsMileageEntries = mutableListOf<Mileage>()
        mileageResultsCursor.moveToFirst()

        while(!mileageResultsCursor.isAfterLast){
            var currentMileage = Mileage()
            currentMileage.mileageID = mileageResultsCursor.getInt(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_MILAGE_ID_primary_key))
            currentMileage.vehicleID = mileageResultsCursor.getInt(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_VEHICLE_ID_foreign))
            currentMileage.distance = mileageResultsCursor.getDouble(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_DISTANCE))
            currentMileage.gasFilled = mileageResultsCursor.getDouble(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_GAS_FILLED))
            currentMileage.location = mileageResultsCursor.getString(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_LOCATION))
            currentMileage.dateString =mileageResultsCursor.getString(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_DATE))
            currentMileage.newOdometer = mileageResultsCursor.getDouble(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_NEW_ODOMETER))
            currentMileage.notes = mileageResultsCursor.getString(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_NAME_NOTES))
            queryResultsMileageEntries.add(currentMileage)
            mileageResultsCursor.moveToNext()
        }
        mileageResultsCursor.close()
        return queryResultsMileageEntries
    }
    fun getMileageEntryCursor( mileageID: Int): Cursor{
        var queryResults: Mileage
        // only get one result lol...
        var projection = arrayOf(
            milageDBContract.milageEntry.TABLE_COLUMN_VEHICLE_ID_foreign,
            milageDBContract.milageEntry.TABLE_COLUMN_MILAGE_ID_primary_key,
            milageDBContract.milageEntry.TABLE_COLUMN_DISTANCE,
            milageDBContract.milageEntry.TABLE_COLUMN_GAS_FILLED,
            milageDBContract.milageEntry.TABLE_COLUMN_LOCATION,
            milageDBContract.milageEntry.TABLE_COLUMN_DATE,
            milageDBContract.milageEntry.TABLE_COLUMN_NEW_ODOMETER,
            milageDBContract.milageEntry.TABLE_COLUMN_NAME_NOTES

        )
        val selection: String? = "${BaseColumns._ID} = ?"
        // where CLAUSE
        val selectionArgs: Array<String>? = arrayOf(mileageID.toString())
        val groupBy: String? = null
        val having: String? = null
        val sortLists: String? = null
        val db = writableDatabase
        var resultCursor: Cursor =
            db.query(
                milageDBContract.milageEntry.TABLE_NAME_milage,
                projection,
                selection,
                selectionArgs,
                groupBy,
                having,
                sortLists
            )
        // return only 1.....
        return resultCursor
    }
    fun getMileageEntryObject( mileageID: Int): Mileage{
        // ERROR  I need
        val mileageResultsCursor = getMileageEntryCursor(mileageID)

        mileageResultsCursor.moveToFirst()


            var gettingMileage = Mileage()
            gettingMileage.mileageID = mileageResultsCursor.getInt(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_MILAGE_ID_primary_key))
            gettingMileage.vehicleID = mileageResultsCursor.getInt(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_VEHICLE_ID_foreign))
            gettingMileage.distance = mileageResultsCursor.getDouble(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_DISTANCE))
            gettingMileage.gasFilled = mileageResultsCursor.getDouble(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_GAS_FILLED))
            gettingMileage.location = mileageResultsCursor.getString(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_LOCATION))
            gettingMileage.dateString =mileageResultsCursor.getString(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_DATE))
            gettingMileage.newOdometer = mileageResultsCursor.getDouble(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_NEW_ODOMETER))
            gettingMileage.notes = mileageResultsCursor.getString(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_NAME_NOTES))

            mileageResultsCursor.moveToNext()

        mileageResultsCursor.close()
        return gettingMileage

    }
    // test this FUnction
    fun getLatestMileageEntryByDateCursor(vehicleID: Int): Cursor{
        var queryResults: Mileage

        var projection = arrayOf(
            milageDBContract.milageEntry.TABLE_COLUMN_VEHICLE_ID_foreign,
            milageDBContract.milageEntry.TABLE_COLUMN_MILAGE_ID_primary_key,
            milageDBContract.milageEntry.TABLE_COLUMN_DISTANCE,
            milageDBContract.milageEntry.TABLE_COLUMN_GAS_FILLED,
            milageDBContract.milageEntry.TABLE_COLUMN_LOCATION,
            milageDBContract.milageEntry.TABLE_COLUMN_DATE,
            milageDBContract.milageEntry.TABLE_COLUMN_NEW_ODOMETER,
            milageDBContract.milageEntry.TABLE_COLUMN_NAME_NOTES

        )
        val selection: String? = "${milageDBContract.milageEntry.TABLE_COLUMN_VEHICLE_ID_foreign} = ?"
        //How to do this....
        val selectionArgs: Array<String>? = arrayOf(vehicleID.toString())
        val groupBy: String? = null
        val having: String? = null
        val sortLists: String? = "date(${milageDBContract.milageEntry.TABLE_COLUMN_DATE})"
        val db = writableDatabase
        var resultCursor: Cursor =
            db.query(
                milageDBContract.milageEntry.TABLE_NAME_milage,
                projection,
                selection,
                selectionArgs,
                groupBy,
                having,
                sortLists
            )
        return resultCursor
    }
    fun getLatestMileageEntryByDateObject(vehicleID: Int): List<Mileage>{
        val mileageResultsCursor = getMileageEntrysByVehicleIDCursor(vehicleID)
        val queryResultsMileageEntries = mutableListOf<Mileage>()
        mileageResultsCursor.moveToFirst()

        while(!mileageResultsCursor.isAfterLast){
            var currentMileage = Mileage()
            currentMileage.mileageID = mileageResultsCursor.getInt(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_MILAGE_ID_primary_key))
            currentMileage.vehicleID = mileageResultsCursor.getInt(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_VEHICLE_ID_foreign))
            currentMileage.distance = mileageResultsCursor.getDouble(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_DISTANCE))
            currentMileage.gasFilled = mileageResultsCursor.getDouble(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_GAS_FILLED))
            currentMileage.location = mileageResultsCursor.getString(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_LOCATION))
            currentMileage.dateString =mileageResultsCursor.getString(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_DATE))
            currentMileage.newOdometer = mileageResultsCursor.getDouble(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_NEW_ODOMETER))
            currentMileage.notes = mileageResultsCursor.getString(mileageResultsCursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_NAME_NOTES))
            queryResultsMileageEntries.add(currentMileage)
            mileageResultsCursor.moveToNext()
        }
        mileageResultsCursor.close()
        return queryResultsMileageEntries
    }

    fun createMilageEntry(newMileage: Mileage): Long{
        // test this..
        val vehicle = getVehiclObjectByID(newMileage.vehicleID?: -1)

        if(vehicle == null){

            return -1
        }
        else{

            val values = ContentValues().apply {
                put(milageDBContract.milageEntry.TABLE_COLUMN_VEHICLE_ID_foreign,newMileage.vehicleID)
                put(milageDBContract.milageEntry.TABLE_COLUMN_NEW_ODOMETER, newMileage.newOdometer)
                put(milageDBContract.milageEntry.TABLE_COLUMN_NAME_NOTES, newMileage.notes)
                put(milageDBContract.milageEntry.TABLE_COLUMN_DATE, newMileage.dateString)
                put(milageDBContract.milageEntry.TABLE_COLUMN_GAS_FILLED, newMileage.gasFilled)
                put(milageDBContract.milageEntry.TABLE_COLUMN_DISTANCE, newMileage.distance)
                put(milageDBContract.milageEntry.TABLE_COLUMN_LOCATION, newMileage.location)

            }
             val db = writableDatabase
            return db.insert(milageDBContract.milageEntry.TABLE_NAME_milage, null, values)
        }

    }
    fun updateMilageEntry(newMileage: Mileage): Int{
        // test this..
        val vehicle = getVehiclObjectByID(newMileage.vehicleID?: -1)
        if(vehicle == null || newMileage.mileageID == null){

            return -1
        }
        else{

            val values = ContentValues().apply {
                put(milageDBContract.milageEntry.TABLE_COLUMN_MILAGE_ID_primary_key, newMileage.mileageID)
                put(milageDBContract.milageEntry.TABLE_COLUMN_VEHICLE_ID_foreign,newMileage.vehicleID)
                put(milageDBContract.milageEntry.TABLE_COLUMN_NEW_ODOMETER, newMileage.newOdometer)
                put(milageDBContract.milageEntry.TABLE_COLUMN_NAME_NOTES, newMileage.notes)
                put(milageDBContract.milageEntry.TABLE_COLUMN_DATE, newMileage.dateString)
                put(milageDBContract.milageEntry.TABLE_COLUMN_GAS_FILLED, newMileage.gasFilled)
                put(milageDBContract.milageEntry.TABLE_COLUMN_DISTANCE, newMileage.distance)
                put(milageDBContract.milageEntry.TABLE_COLUMN_LOCATION, newMileage.location)

            }
            val db = writableDatabase
            return db.update(milageDBContract.milageEntry.TABLE_NAME_milage, values,
                milageDBContract.milageEntry.TABLE_COLUMN_MILAGE_ID_primary_key + "= ?", arrayOf(newMileage.mileageID.toString()))
        }

    }
    fun deleteMileageEntryByMileageID(mileageID: Int): Int{
        val selection = "${milageDBContract.milageEntry.TABLE_COLUMN_MILAGE_ID_primary_key} = ?"
        val selectionArgs = arrayOf(mileageID.toString())

        return writableDatabase.delete(milageDBContract.milageEntry.TABLE_NAME_milage, selection, selectionArgs)
    }




}
