package ca.nait.dmit2504.androidmilagetracker.customListAdapters

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import kotlin.math.round

import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import ca.nait.dmit2504.androidmilagetracker.R
import ca.nait.dmit2504.androidmilagetracker.R.layout.mileage_list_item_row
import ca.nait.dmit2504.androidmilagetracker.trackerDatabase.customObjects.Mileage
import ca.nait.dmit2504.androidmilagetracker.trackerDatabase.milageDBContract
import kotlin.math.roundToInt
import kotlin.math.roundToLong

/*to do If time: Build out feeature where database goes to json file you can download or save on to phone and load :D*/
class MileageListCursorAdapter(context: Context, cursor: Cursor, flags: Int) : androidx.cursoradapter.widget.CursorAdapter(context,cursor,flags) {
    object isMetric {
        var switchState: Boolean = true
    }

    var _isMetric: Boolean = true

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        _isMetric = _isMetric
        return LayoutInflater.from(context).inflate(mileage_list_item_row, parent, false)
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        // find out if it's imperial or metric then change accordingnly


        val mileageDateTextView: TextView = view?.findViewById(R.id.mileage_list_item_date_row)!!
        val mileageGasFilledTextView: TextView = view?.findViewById(R.id.mileage_list_item_liters)!!
        val mileageDistanceTextView: TextView = view?.findViewById(R.id.mileage_list_item_distance)!!
        val mileageMileageTextView: TextView = view?.findViewById(R.id.mileage_list_item_mileage)!!
//        val mileageImperialMetricSwitch: Switch = view?.findViewById(R.id.metric_imperial_switch_view_mileage_activity)!!
        var currentMileage = Mileage()

        currentMileage.vehicleID = cursor?.getInt(cursor.getColumnIndexOrThrow( milageDBContract.milageEntry.TABLE_COLUMN_MILAGE_ID_primary_key))
       // fix this bandaid
        currentMileage.gasFilled = cursor?.getDouble( cursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_GAS_FILLED))
        currentMileage.distance = cursor?.getDouble(cursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_DISTANCE))

        //https://www.datetimeformatter.com/how-to-format-date-time-in-kotlin/


        currentMileage.dateString = cursor?.getString(cursor.getColumnIndexOrThrow(milageDBContract.milageEntry.TABLE_COLUMN_DATE))

//        val dateFormatter = DateTimeFormatter.ofLocalizedDate()
//        val dateFormatted = LocalDate.parse(currentMileage.dateString, dateFormatter).toString()


//        currentMileage.dateString = dateFormatted

        mileageDateTextView.text = currentMileage.dateString
        currentMileage.gasFilled = currentMileage.gasFilled!! * 1.0000000000
        mileageGasFilledTextView.text = currentMileage.gasFilled.toString().take(4) + "L"

        currentMileage.distance = currentMileage.distance!! * 1.000000000

        mileageDistanceTextView.text = currentMileage.distance.toString().take(4) + "Km"


            mileageDistanceTextView.text = currentMileage.distance.toString().take(4) + "Km"



        mileageMileageTextView.text = currentMileage.getMetricMileage().toString().take(4) + " L/100km"


            mileageMileageTextView.text = currentMileage.getMetricMileage().toString().take(4) + " L/100km"


        if(isMetric.switchState){ // test checked

            mileageDistanceTextView.text = currentMileage.getImpDistance().toString().take(4) + "M" // used to be 4
//            mileageGasFilledTextView.text = currentMileage.impGas.toString()
            mileageGasFilledTextView.text = currentMileage.getImpGas().toString().take(4) + " G" /// wtf

            mileageMileageTextView.text = (currentMileage.getImperialMileage().toString()).take(4) + " MPG"
        }

    }
}