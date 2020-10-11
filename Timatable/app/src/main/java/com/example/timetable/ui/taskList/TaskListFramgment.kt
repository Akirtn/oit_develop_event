package com.example.timetable.ui.taskList

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timetable.*
import com.example.timetable.ui.nagaoBus.NagaoBusViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.islandparadise14.mintable.model.ScheduleEntity
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
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

        realm = Realm.getDefaultInstance()
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