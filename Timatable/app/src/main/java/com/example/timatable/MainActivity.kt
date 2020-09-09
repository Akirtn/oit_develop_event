package com.example.timatable

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import java.util.prefs.Preferences

class MainActivity : AppCompatActivity() {
    private val day = arrayOf("月", "火", "水", "木", "金")
    private val scheduleList: ArrayList<ScheduleEntity> = ArrayList()

    var global_scheduleDay = 0
    var global_time = 0
    //プリファレンスの遅延初期化？
    lateinit var shardPreferences: SharedPreferences
    lateinit var shardPrefEditor : SharedPreferences.Editor

    //時間割りのデータを保持するためのインスタンス
    val timetable_data = TimetableDataManager()

    //画面の変化で動く関数
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        table.initTable(day)
        table.updateSchedules(scheduleList)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //データの保存に使用するプリファレンスの初期化
        shardPreferences = this.getPreferences(Context.MODE_PRIVATE)
        shardPrefEditor = shardPreferences.edit()

        //時間割を読み込んでスケジュールリストに追加
        scheduleList += timetable_data.loadData(shardPreferences,shardPrefEditor)

        //テーブルの初期化設定
        table.baseSetting(20, 30, 100)

        //クリックされると入力画面に遷移する
        table.setOnTimeCellClickListener(object : OnTimeCellClickListener {
            override fun timeCellClicked(scheduleDay: Int, time: Int) {
                //onActivityResultにsheduleDayとtimeを渡すためにグローバル変数に代入
                global_scheduleDay = scheduleDay
                global_time = time
                //入力画面に遷移
                val intent = Intent(this@MainActivity,InputScreen::class.java)
                startActivityForResult(intent,1000)

            }
        })
    }

    //入力画面から遷移すると動作する関数
    //科目名が入力されている場合のみスケジュール一覧に追加する
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == RESULT_OK && null != intent) {
            val subject_name = intent.getStringExtra("subject_name")
            val class_number = intent.getStringExtra("class_number")

            //科目内容設定
            val schedule = ScheduleEntity(
                global_scheduleDay * global_time, //originId
                subject_name.toString(), //scheduleName
                class_number.toString(), //roomInfo
                global_scheduleDay, //ScheduleDay object (MONDAY ~ SUNDAY)
                global_time.toString() + ":00", //startTime format: "HH:mm"
                (global_time + 1).toString()+":00", //endTime  format: "HH:mm"
                "#73fcae68", //backgroundColor (optional)
                "#000000" //textcolor (optional)
            )

            //時間割をプリファレンスに保存する
            timetable_data.setData(global_scheduleDay,global_time,subject_name.toString(),class_number.toString())
            timetable_data.saveData(shardPreferences,shardPrefEditor)

            scheduleList.add(schedule)
        }
    }


}