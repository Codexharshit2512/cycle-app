package com.example.cycletrackingapp.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cycle_runs")
data class Run(
   @ColumnInfo(name = "previewImage") var previewImage:String?=null,
   @ColumnInfo(name = "timestamp") var timestamp:Long=0L,
   @ColumnInfo(name = "distance") var distance:Double=0.0,
   @ColumnInfo(name = "time") var time:Long=0L,
   @ColumnInfo(name = "calories") var calories:Double=0.0,
   @ColumnInfo(name = "averageSpeed") var averageSpeed:Double = 0.0,
   @ColumnInfo(name = "maxSpeed") var maxSpeed:Double = 0.0,
   @ColumnInfo(name = "title") var title:String = "No Title"
):Parcelable{
    @PrimaryKey(autoGenerate = true)
    var id:Int=0

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        previewImage = parcel.readString()
        timestamp=parcel.readLong()
        distance=parcel.readDouble()
        time=parcel.readLong()
        calories=parcel.readDouble()
        averageSpeed=parcel.readDouble()
        maxSpeed=parcel.readDouble()
        title=parcel.readString()!!
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(previewImage)
        parcel.writeLong(timestamp)
        parcel.writeDouble(distance)
        parcel.writeLong(time)
        parcel.writeDouble(calories)
        parcel.writeDouble(averageSpeed)
        parcel.writeDouble(maxSpeed)
        parcel.writeString(title)
    }

    companion object CREATOR : Parcelable.Creator<Run> {
        override fun createFromParcel(parcel: Parcel): Run {
            return Run(parcel)
        }

        override fun newArray(size: Int): Array<Run?> {
            return arrayOfNulls(size)
        }
    }
}





