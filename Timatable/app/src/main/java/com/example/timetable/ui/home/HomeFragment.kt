package com.example.timetable.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.timetable.*
import com.example.timetable.model.CellDataEntity
import com.islandparadise14.mintable.MinTimeTableView
import com.islandparadise14.mintable.model.ScheduleEntity
import com.islandparadise14.mintable.tableinterface.OnScheduleClickListener
import com.islandparadise14.mintable.tableinterface.OnTimeCellClickListener
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

  private lateinit var homeViewModel: HomeViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    homeViewModel =
      ViewModelProviders.of(this).get(HomeViewModel::class.java)
    val root = inflater.inflate(R.layout.fragment_home, container, false)

    val t = root.findViewById<MinTimeTableView>(R.id.table)

    t.setOnTimeCellClickListener(object : OnTimeCellClickListener{
      override fun timeCellClicked(scheduleDay: Int, time: Int) {

        //入力画面に遷移
        val cellData = CellDataEntity(scheduleDay,
          time,
          "",
          "",
          "",
          "",
          "",
          "#99bdff")
        val intent = Intent(activity,InputScreen::class.java)
        intent.putExtra("cellData",cellData)
        startActivityForResult(intent,1000)
      }
    })

    //スケジュールがクリックされると動作する
    t.setOnScheduleClickListener(
      object : OnScheduleClickListener {
        override fun scheduleClicked(entity: ScheduleEntity) {
          val time = entity.startTime.split(":")

          val arrayCellData = getData(entity.scheduleDay, time[0].toInt())

          val cellData = CellDataEntity(entity.scheduleDay,
            time[0].toInt(),
            entity.scheduleName,
            entity.roomInfo,
            arrayCellData[2],
            arrayCellData[3],
            arrayCellData[4],
            arrayCellData[5]
          )

          val intent = Intent(activity,InputScreen::class.java)
          intent.putExtra("cellData",cellData)
          scheduleList.remove(entity)
          startActivityForResult(intent,1000)
        }
      }
    )
    return root
  }

  override fun onStart() {
    super.onStart()
    table.initTable(day)
    table.baseSetting(30, 30, cellHeight)
    table.isFullWidth(true)
    table.updateSchedules(scheduleList)
  }
}