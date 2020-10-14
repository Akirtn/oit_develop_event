package com.example.timetable


import android.app.Application
import android.content.Context
import io.realm.Realm
import java.io.File

class SchedulerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //deleteRealm(this)
        Realm.init(this)
    }

    fun deleteRealm(context: Context) {
        val dir = context.filesDir
        dir.list().forEach {
            if (it.contains("realm")) {
                File(dir, it).deleteRecursively()
            }
        }
    }

}