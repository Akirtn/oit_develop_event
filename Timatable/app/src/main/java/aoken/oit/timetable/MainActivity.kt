package aoken.oit.timetable
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import aoken.oit.timetable.model.CellData
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.islandparadise14.mintable.model.ScheduleEntity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.Serializable

val scheduleList: ArrayList<ScheduleEntity> = ArrayList()
val day = arrayOf("月", "火", "水", "木", "金")
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

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_nagao, R.id.nav_kitayama, R.id.nav_task, R.id.nav_license), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        //csv読み込み
        loadCSV(resources.assets)

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
            val cellData: CellData? = serializable as CellData
            if (resultCode == 100) {

                Snackbar.make(nav_view, "保存しました", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.YELLOW)
                    .show()

            }else if(resultCode == -2){
                Snackbar.make(nav_view, "削除しました", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.YELLOW)
                    .show()

            }
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