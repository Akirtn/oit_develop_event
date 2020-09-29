package com.example.timetable
import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.timetable.model.CellDataEntity
import com.google.android.material.navigation.NavigationView
import com.islandparadise14.mintable.model.ScheduleEntity
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

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

        //csv読み込み
        val assetManager = resources.assets
        val inputStream= assetManager.open("syllabus_database_3.csv")
        //val csvString = inputStream.bufferedReader().use { it.readText() }
        //Log.v("csv",csvString)
        val inputStreamReader = InputStreamReader(inputStream)
        var bufferedReader = BufferedReader(inputStreamReader)
        var line: String? = bufferedReader.readLine()
        while (line != null){
            val rowData = line.split(',')
            csv_array.add(ArrayList(rowData))
            line = bufferedReader.readLine()
        }

        //時間割を読み込んでスケジュールリストに追加
        scheduleList += loadData(shardPreferences,shardPrefEditor)

    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        table.initTable(day)
        val dp = resources.displayMetrics.density
        val drawerLayoutHeight = findViewById<DrawerLayout>(R.id.drawer_layout).height / dp
        val toolbarHeight = (findViewById<Toolbar>(R.id.toolbar)).height / dp

        val cellHeight = ((drawerLayoutHeight - toolbarHeight - 52) / 6).toInt()

        table.baseSetting(30, 30, cellHeight)
        table.isFullWidth(true)
        table.updateSchedules(scheduleList)
    }

    override fun onResume() {
        super.onResume()
        onWindowFocusChanged(false)
    }

    //入力画面から遷移すると動作する関数
    //科目名が入力されている場合のみスケジュール一覧に追加する
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        val cellData: CellDataEntity? = intent?.getSerializableExtra("cellData") as CellDataEntity
        if (resultCode == Activity.RESULT_OK) {

            if (cellData != null && cellData.subjectName.length > 0){
                val subject_name = cellData.subjectName
                val class_number = cellData.classNumber
                val teacher_name = cellData.teacherName
                val period = cellData.period
                val syllabus_link = cellData.syllabus_link

                //科目内容設定
                val schedule = ScheduleEntity(
                    cellData.x * cellData.y, //originId
                    cellData.subjectName, //scheduleName
                    cellData.classNumber, //roomInfo
                    cellData.x, //ScheduleDay object (MONDAY ~ SUNDAY)
                    cellData.y.toString() + ":00", //startTime format: "HH:mm"
                    (cellData.y + 1).toString()+":00", //endTime  format: "HH:mm"
                    "#99bdff", //backgroundColor (optional)
                    "#000000" //textcolor (optional)
                )

                //時間割をプリファレンスに保存する
                setData(cellData.x,cellData.y,subject_name.toString(),class_number.toString(),
                    teacher_name.toString(),period.toString(),syllabus_link.toString())
                scheduleList.add(schedule)
            }

        }else if(resultCode == Activity.RESULT_CANCELED){
            deleteData(cellData?.x?:0,cellData?.y?:0)
        }
        saveData(shardPreferences,shardPrefEditor)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}