package com.example.timetable.ui.taskList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.timetable.R
import com.example.timetable.ui.nagaoBus.NagaoBusViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class NagaoBusFragment : Fragment() {

    private lateinit var taskListViewModel: TaskListViewModel

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
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        return root
    }
}