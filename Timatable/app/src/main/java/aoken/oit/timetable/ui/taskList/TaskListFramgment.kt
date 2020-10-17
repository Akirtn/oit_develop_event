package aoken.oit.timetable.ui.taskList

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import aoken.oit.timetable.ScheduleAdapter
import aoken.oit.timetable.ScheduleEditActivity
import aoken.oit.timetable.*
import aoken.oit.timetable.model.Schedule
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_task_list.*

class TaskListFragment : Fragment() {

    private lateinit var taskListViewModel: TaskListViewModel

    private lateinit var realm: Realm

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        taskListViewModel =
            ViewModelProviders.of(this).get(TaskListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_task_list, container, false)

        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            val intent = Intent(activity, ScheduleEditActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onStart() {
        super.onStart()

        val config = RealmConfiguration.Builder()
            .name("Schedule.realm")
            .schemaVersion(2)
            //.modules(CellDataEntity())
            .build()
        //Realm.setDefaultConfiguration(realmConfig) // 上記の設定をRealmにセット

        realm = Realm.getInstance(config)
        list.layoutManager = LinearLayoutManager(context)
        val schedules = realm.where<Schedule>().findAll()
        val adapter = ScheduleAdapter(schedules)
        list.adapter = adapter

        adapter.setOnItemClickListener { id ->
            val intent = Intent(activity, ScheduleEditActivity::class.java)
                .putExtra("schedule_id",id)
            startActivityForResult(intent,1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            Snackbar.make(nav_view, "保存しました", Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.YELLOW)
                .show()

        }else if(resultCode == -2){
            Snackbar.make(nav_view, "削除しました", Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.YELLOW)
                .show()

        }
    }*/
}