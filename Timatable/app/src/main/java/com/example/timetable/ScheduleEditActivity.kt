package com.example.timetable

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.util.Linkify
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import com.example.timetable.model.Schedule
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_schedule_edit.*
import java.lang.IllegalArgumentException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ScheduleEditActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var realm: Realm

    private var scheduleId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_edit)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val config = RealmConfiguration.Builder()
            .name("Schedule.realm")
            .schemaVersion(2)
            .build()

        realm = Realm.getInstance(config)

        scheduleId = intent?.getLongExtra("schedule_id", -1L)?:0L
        if(scheduleId != -1L){
            val schedule = realm.where<Schedule>()
                .equalTo("id",scheduleId).findFirst()
            dateEdit.setText(android.text.format.DateFormat.format("yyyy/MM/dd", schedule?.date))
            titleEdit.setText(schedule?.title)
            detailEdit.setText(schedule?.detail)
            Linkify.addLinks(detailEdit, Linkify.ALL)
        }

        //科目名の入力サジェスト設定
        val subjectNameAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, getColumn(0))
        titleEdit.setAdapter(subjectNameAdapter)
        titleEdit.threshold = 1

        val dateButton = findViewById<Button>(R.id.schedule_date_button)
        dateButton.setOnClickListener{
            val datePickerFragment = DatePick()
            datePickerFragment.show(supportFragmentManager, "datePicker")
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

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val str = String.format(Locale.US, "%d/%d/%d", year, month+1, dayOfMonth)
        dateEdit.setText(str)
    }
}