package com.example.timetable.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.timetable.*
import com.islandparadise14.mintable.MinTimeTableView
import com.islandparadise14.mintable.model.ScheduleEntity
import com.islandparadise14.mintable.tableinterface.OnScheduleClickListener
import com.islandparadise14.mintable.tableinterface.OnTimeCellClickListener
import kotlinx.android.synthetic.*
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
    Log.v("home","home")

    val t = root.findViewById<MinTimeTableView>(R.id.table)

    t.setOnTimeCellClickListener(object : OnTimeCellClickListener{
      override fun timeCellClicked(scheduleDay: Int, time: Int) {
        Log.v("tag",time.toString())
        //onActivityResultにsheduleDayとtimeを渡すためにグローバル変数に代入
        global_scheduleDay = scheduleDay
        global_time = time
        //入力画面に遷移
        val intent = Intent(activity,InputScreen::class.java)
        startActivityForResult(intent,1000)
        Log.v("cellClick",scheduleDay.toString())
      }
    })

    //スケジュールがクリックされると動作する
    t.setOnScheduleClickListener(
      object : OnScheduleClickListener {
        override fun scheduleClicked(entity: ScheduleEntity) {
          val time = entity.startTime.split(":")
          global_scheduleDay = entity.scheduleDay
          global_time = time[0].toInt()

          val intent = Intent(activity,InputScreen::class.java)
          intent.putExtra("subject_name",entity.scheduleName.toString())
          intent.putExtra("class_number",entity.roomInfo.toString())
          scheduleList.remove(entity)
          startActivityForResult(intent,1000)
        }
      }
    )
    return root
  }

  override fun onStart() {
    super.onStart()
    Log.v("onStart","onStart")

    table.initTable(day)
    //テーブルの初期化設定
    table.baseSetting(20, 30, 100)
    table.isFullWidth(true)
    table.updateSchedules(scheduleList)


    Log.v("onResume","onResume")
  }


}