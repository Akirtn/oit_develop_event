package com.example.timetable
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timetable.model.CellDataEntity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.islandparadise14.mintable.model.ScheduleEntity
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_task_list.*
import java.io.Serializable


var cellHeight = 0

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
            R.id.nav_home, R.id.nav_nagao, R.id.nav_kitayama, R.id.nav_task), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //データの保存に使用するプリファレンスの初期化
        shardPreferences = getPreferences(MODE_PRIVATE)
        shardPrefEditor = shardPreferences.edit()

        //csv読み込み
        loadCSV(resources.assets)

        //時間割を読み込んでスケジュールリストに追加
        scheduleList += loadSyllabusData(shardPreferences,shardPrefEditor)

    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        val dp = resources.displayMetrics.density
        val drawerLayoutHeight = findViewById<DrawerLayout>(R.id.drawer_layout).height / dp
        val toolbarHeight = (findViewById<Toolbar>(R.id.toolbar)).height / dp
        cellHeight = ((drawerLayoutHeight - toolbarHeight - 50) / 6).toInt()

        table.initTable(day)
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

        val serializable: Serializable? = intent?.getSerializableExtra("cellData")
        if (serializable != null){
            val cellData: CellDataEntity? = serializable as CellDataEntity
            if (resultCode == 100) {
                if (cellData!= null && cellData.subjectName.isNotEmpty()){
                    //科目内容設定
                    val schedule = ScheduleEntity(
                        cellData.x * cellData.y, //originId
                        cellData.subjectName, //scheduleName
                        cellData.classNumber, //roomInfo
                        cellData.x, //ScheduleDay object (MONDAY ~ SUNDAY)
                        cellData.y.toString() + ":00", //startTime format: "HH:mm"
                        (cellData.y + 1).toString()+":00", //endTime  format: "HH:mm"
                        cellData.color, //backgroundColor (optional)
                        "#000000" //textcolor (optional)
                    )

                    //時間割をプリファレンスに保存する
                    setData(cellData)
                    scheduleList.add(schedule)
                }

                Snackbar.make(nav_view, "保存しました", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.YELLOW)
                    .show()

            }else if(resultCode == -2){
                Snackbar.make(nav_view, "削除しました", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.YELLOW)
                    .show()

            }
            saveData(shardPreferences,shardPrefEditor)
        }else{
            if (resultCode == 100) {

                Snackbar.make(nav_view, "保存しました", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.YELLOW)
                    .show()
            }else if(resultCode == -2){
                Snackbar.make(nav_view, "削除しました", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.YELLOW)
                    .show()
            }
        }

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}