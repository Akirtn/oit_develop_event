package com.example.timetable.model

import java.io.Serializable

val cellDataEntitySize = 8

class CellDataEntity (
    var x: Int,
    var y: Int,
    var subjectName: String,
    var classNumber: String,
    var teacherName: String,
    var period: String,
    var syllabusLink: String,
    var color: String
) : Serializable