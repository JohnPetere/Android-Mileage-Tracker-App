package ca.nait.dmit2504.androidmilagetracker.trackerDatabase.customObjects

import java.util.*

class Mileage {
    var mileageID: Int? = 0
    var vehicleID: Int? = 0
    var newOdometer: Double? = 0.0
    var gasFilled: Double? = 0.0
    fun getImpGas(): Double{
        if(gasFilled == null){
            return -1.0
        }
        else{
            return gasFilled!! / 3.76
        }
    }
    var distance: Double? = 0.0
    fun getImpDistance(): Double{
        if(distance == null){
            return -1.0
        }
        else{
            return distance!! * 0.62
        }
    }
    var dateString: String? = ""
    // is this best practice?
    var notes: String? = ""
    var location: String? = ""
    fun getMetricMileage(): Double {
        if(gasFilled == null || distance == null){
            return -1.0
        }
        else{

            return (gasFilled!!/ (distance!!/100))
        }
    }
    fun getImperialMileage(): Double{
        if(gasFilled == null || distance == null){
            return -1.0
        }
        else{
            // is there a better way of doing this?
            return 235.215/getMetricMileage()
        }
    }

}