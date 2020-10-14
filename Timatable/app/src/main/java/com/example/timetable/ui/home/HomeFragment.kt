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
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

  private lateinit var homeViewModel: HomeViewModel

  private lateinit var realm: Realm

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

        val intent = Intent(activity,InputScreen::class.java)
        intent.putExtra("x",scheduleDay)
        intent.putExtra("y", time)
        intent.putExtra("scheduleEmptyFlag", 1)
        startActivityForResult(intent,1000)
      }
    })

    //スケジュールがクリックされると動作する
    t.setOnScheduleClickListener(
      object : OnScheduleClickListener {
        override fun scheduleClicked(entity: ScheduleEntity) {

          val time = entity.startTime.split(":")

          val intent = Intent(activity,InputScreen::class.java)

          intent.putExtra("x", entity.scheduleDay)
          intent.putExtra("y", time[0].toInt())
          intent.putExtra("scheduleEmptyFlag", 0)

          startActivityForResult(intent,1000)
        }
      }
    )
    return root
  }

  override fun onStart() {
    super.onStart()

    val config = RealmConfiguration.Builder()
      .name("CellDataEntity.realm")
      .schemaVersion(1)
      .build()

    realm = Realm.getInstance(config)
    val cells = realm.where<CellDataEntity>().findAll()

    scheduleList.clear()

    //時間割読み込み
    for ( cell in cells){
      val cellData = ScheduleEntity(
        cell.id,
        cell.subjectName,
        cell.classNumber,
        cell.x,
        (cell.y).toString() + ":00",
        (cell.y + 1).toString() + ":00",
        String.format("#%06X", 0xFFFFFF and cell.color),
        "#000000"
      )
      scheduleList.add(cellData)
    }

    table.initTable(day)
    table.baseSetting(30, 30, cellHeight)
    table.isFullWidth(true)
    table.updateSchedules(scheduleList)
  }

  override fun onDestroy() {
    super.onDestroy()
    realm.close()
  }
}