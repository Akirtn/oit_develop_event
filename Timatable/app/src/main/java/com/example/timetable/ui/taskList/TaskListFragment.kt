package com.example.timetable.ui.taskList

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timetable.R
import com.example.timetable.Schedule
import com.example.timetable.ScheduleAdapter
import com.example.timetable.ScheduleEditActivity
import com.example.timetable.ui.nagaoBus.NagaoBusViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_task_list.*

class NagaoBusFragment : Fragment() {

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
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}