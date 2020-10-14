package com.example.timetable.model

import android.graphics.Color
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmModule


open class CellDataEntity : RealmObject() {
    @PrimaryKey
    var id: Int = 0
    var x: Int = 0
    var y: Int = 0
    var subjectName: String = ""
    var classNumber: String = ""
    var teacherName: String = ""
    var period: Int = 0
    var syllabusLink: String = ""
    var color: Int = Color.parseColor("#ffffff")
}