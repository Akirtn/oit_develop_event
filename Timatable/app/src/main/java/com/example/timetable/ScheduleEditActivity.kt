package com.example.timetable

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat.format
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.timetable.R
import com.example.timetable.Schedule
import com.example.timetable.model.CellDataEntity
import com.example.timetable.ui.home.HomeFragment
import com.example.timetable.ui.taskList.TaskListFragment
import com.example.timetable.ui.taskList.TaskListViewModel
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_schedule_edit.*
import java.lang.IllegalArgumentException
import java.lang.String.format
import java.text.DateFormat
import java.text.MessageFormat.format
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ScheduleEditActivity : AppCompatActivity() {
    private lateinit var realm: Realm

    private var scheduleId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_edit)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        realm = Realm.getDefaultInstance()

        scheduleId = intent?.getLongExtra("schedule_id", -1L)?:0L
        if(scheduleId != -1L){
            val schedule = realm.where<Schedule>()
                .equalTo("id",scheduleId).findFirst()
            dateEdit.setText(android.text.format.DateFormat.format("yyyy/MM/dd", schedule?.date))
            titleEdit.setText(schedule?.title)
            detailEdit.setText(schedule?.detail)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_delete -> {
        // User chose the "Settings" item, show the app settings UI...
            realm.executeTransaction{ db: Realm ->
                db.where<Schedule>().equalTo("id", scheduleId)
                    ?.findFirst()
                    ?.deleteFromRealm()
            }
            setResult(-2,intent)
            finish()
            true
        }

        R.id.action_save -> {
            // User chose the "Settings" item, show the app settings UI...
            when(scheduleId){
                -1L -> {
                    realm.executeTransaction{ db: Realm ->
                        val maxId = db.where<Schedule>().max("id")
                        val nextId = (maxId?.toLong() ?: 0L) + 1
                        val schedule = db.createObject<Schedule>(nextId)
                        val date = dateEdit.text.toString().toDate("yyyy/MM/dd")
                        if (date != null) schedule.date = date
                        schedule.title = titleEdit.text.toString()
                        schedule.detail = detailEdit.text.toString()
                        schedule.detail = "test"
                    }
                }
                else -> {
                    realm.executeTransaction{ db: Realm ->
                        val schedule = db.where<Schedule>()
                            .equalTo("id",scheduleId).findFirst()
                        val date = dateEdit.text.toString()
                            .toDate("yyyy/MM/dd")
                        if(date != null) schedule?.date = date
                        schedule?.title = titleEdit.text.toString()
                        schedule?.detail = detailEdit.text.toString()
                    }
                }
            }
            //val intent = Intent(this,MainActivity::class.java)
            setResult(100,intent)
            finish()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.

            setResult(-3,intent)
            finish()
            super.onOptionsItemSelected(item)
        }
    }

    private fun String.toDate(pattern: String = "yyyy/MM/dd HH:mm"): Date? {
        return try {
            SimpleDateFormat(pattern).parse(this)
        }catch (e: IllegalArgumentException){
            return null
        }catch (e: ParseException){
            return null
        }
    }
}