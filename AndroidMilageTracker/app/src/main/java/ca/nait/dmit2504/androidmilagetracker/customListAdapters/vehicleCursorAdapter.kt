package ca.nait.dmit2504.androidmilagetracker.customListAdapters
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import ca.nait.dmit2504.androidmilagetracker.R
import ca.nait.dmit2504.androidmilagetracker.trackerDatabase.milageDBContract
import ca.nait.dmit2504.androidmilagetracker.trackerDatabase.milageTrackerDatabase
import org.w3c.dom.Text
class vehicleCursorAdapter(context: Context, cursor: Cursor, flags: Int) : androidx.cursoradapter.widget.CursorAdapter(context,cursor,flags){
    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View? {
        return LayoutInflater.from(context).inflate(R.layout.vehicle_list_view_item_row, parent, false)


    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        val vehicleTextView: TextView = view?.findViewById(R.id.vehicle_name_listview_item_textView)!!
        // why is it getting the _ID FROM???
        // Year, Make, model, trim,
        val vehicleID: Int = cursor?.getInt(cursor.getColumnIndexOrThrow(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_VEHICLE_ID_primary_key))?:-1
        val year: Int = cursor?.getInt(cursor?.getColumnIndexOrThrow(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_YEAR))?:-1
        val make: String = cursor?.getString(cursor?.getColumnIndexOrThrow(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_MAKE))?:"noMake"
        val model: String = cursor?.getString(cursor?.getColumnIndexOrThrow(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_MODEL))?:"noModel"
        val trim: String = cursor?.getString(cursor?.getColumnIndexOrThrow(milageDBContract.vehicleEntry.TABLE_COLUMN_NAME_TRIM))?:""
        vehicleTextView.text = "$year $make $model $trim"

        // result set of cursor must include



    }



}