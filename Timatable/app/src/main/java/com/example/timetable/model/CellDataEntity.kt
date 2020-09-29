package com.example.timetable.model

import java.io.Serializable

class CellDataEntity (
    var x: Int,
    var y: Int,
    var subjectName: String,
    var classNumber: String,
    var teacherName: String,
    var period: String,
    var syllabus_link: String,
    var color: String
) : Serializable