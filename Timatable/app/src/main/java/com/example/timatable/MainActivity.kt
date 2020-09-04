package com.example.timatable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.islandparadise14.mintable.model.ScheduleDay
import com.islandparadise14.mintable.model.ScheduleEntity
import com.islandparadise14.mintable.tableinterface.OnScheduleClickListener
import com.islandparadise14.mintable.tableinterface.OnScheduleLongClickListener
import com.islandparadise14.mintable.tableinterface.OnTimeCellClickListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val day = arrayOf("月", "火", "水", "木", "金")
    private val scheduleList: ArrayList<ScheduleEntity> = ArrayList()
    val schedule = ScheduleEntity(
        32, //originId
        "データベース", //scheduleName
        "1401", //roomInfo
        ScheduleDay.MONDAY, //ScheduleDay object (MONDAY ~ SUNDAY)
        "1:00", //startTime format: "HH:mm"
        "2:00", //endTime  format: "HH:mm"
        "#73fcae68", //backgroundColor (optional)
        "#000000" //textcolor (optional)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        table.setOnTimeCellClickListener(object : OnTimeCellClickListener {
            override fun timeCellClicked(scheduleDay: Int, time: Int) {
                //do something
                Log.v("test","test")
                Log.v("scheduleDay",scheduleDay.toString())
                Log.v("time",time.toString())


                val schedule2 = ScheduleEntity(
                    scheduleDay * time, //originId
                    "データベース", //scheduleName
                    "1401", //roomInfo
                    scheduleDay, //ScheduleDay object (MONDAY ~ SUNDAY)
                    time.toString() + ":00", //startTime format: "HH:mm"
                    (time + 1).toString()+":00", //endTime  format: "HH:mm"
                    "#73fcae68", //backgroundColor (optional)
                    "#000000" //textcolor (optional)
                )

                scheduleList.add(schedule2)
                //onWindowFocusChanged(hasFocus)
                table.updateSchedules(scheduleList)
            }
        })
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        scheduleList.add(schedule)
        table.baseSetting(20, 30, 100)
        super.onWindowFocusChanged(hasFocus)
        table.initTable(day)
        table.updateSchedules(scheduleList)

        Log.v("test","test")

        schedule.setOnClickListener(View.OnClickListener {
            //do something
            Log.v("test","test")
        })

    }
}