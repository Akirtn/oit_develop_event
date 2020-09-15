package com.example.timetable
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.timetable.ui.home.HomeFragment
import com.islandparadise14.mintable.MinTimeTableView
import com.islandparadise14.mintable.model.ScheduleEntity
import com.islandparadise14.mintable.tableinterface.OnScheduleClickListener
import com.islandparadise14.mintable.tableinterface.OnTimeCellClickListener
import kotlinx.android.synthetic.main.fragment_home.table

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    val day = arrayOf("月", "火", "水", "木", "金")
    val scheduleList: ArrayList<ScheduleEntity> = ArrayList()

    var global_scheduleDay = 0
    var global_time = 0
    //プリファレンスの遅延初期化？
    lateinit var shardPreferences: SharedPreferences
    lateinit var shardPrefEditor : SharedPreferences.Editor

    //時間割りのデータを保持するためのインスタンス
    private var timetable_data = TimetableDataManager()

    //画面の変化で動く関数
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        table.initTable(day)
        table.updateSchedules(scheduleList)
        Log.v("onWindowFocusChanged","onWindowFocusChanged")
        //createFlag = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //データの保存に使用するプリファレンスの初期化
        shardPreferences = getPreferences(MODE_PRIVATE)
        shardPrefEditor = shardPreferences.edit()

        //時間割を読み込んでスケジュールリストに追加
        scheduleList += timetable_data.loadData(shardPreferences,shardPrefEditor)

        Log.v("main","end")

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
            scheduleList.add(schedule)
        }else{
            timetable_data.deleteData(global_scheduleDay,global_time)
        }
        timetable_data.saveData(shardPreferences,shardPrefEditor)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        Log.v("onCreateOption","onCreateOption")


        //テーブルの初期化設定
        table.baseSetting(20, 30, 100)

        table.initTable(day)
        table.updateSchedules(scheduleList)

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

        //スケジュールがクリックされると動作する
        table.setOnScheduleClickListener(
            object : OnScheduleClickListener {
                override fun scheduleClicked(entity: ScheduleEntity) {
                    val time = entity.startTime.split(":")
                    global_scheduleDay = entity.scheduleDay
                    global_time = time[0].toInt()

                    val intent = Intent(this@MainActivity,InputScreen::class.java)
                    intent.putExtra("subject_name",entity.scheduleName.toString())
                    intent.putExtra("class_number",entity.roomInfo.toString())
                    scheduleList.remove(entity)
                    startActivityForResult(intent,1000)
                }
            }
        )

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        Log.v("onSupportNavigate","onSupportNavigate")
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}