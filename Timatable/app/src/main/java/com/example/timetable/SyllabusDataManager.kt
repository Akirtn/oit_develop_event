package com.example.timetable

import android.content.res.AssetManager
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader


private  var csvArray = ArrayList<ArrayList<String>>()


fun getColumn(index:Int):ArrayList<String>{
    var ret = ArrayList<String>()
    for (row in csvArray){
        ret.add(row[index])
    }
    val set = ret.toSet()
    ret = ArrayList(set)
    return ret
}


fun findLink(subject_name:String, teacher_name: String, period: String): String {
    for (row in csvArray){
        Log.v("row",row.toString())
        if (row[0] == subject_name && row[2] == period && row[4] == teacher_name){
            return row[5]
        }
    }
    return "not found"
}

fun loadCSV(assetManager: AssetManager){
    //csv読み込み
    val inputStream= assetManager.open("syllabus_database_3.csv")
    val inputStreamReader = InputStreamReader(inputStream)
    var bufferedReader = BufferedReader(inputStreamReader)
    var line: String? = bufferedReader.readLine()
    while (line != null){
        val rowData = line.split(',')
        csvArray.add(ArrayList(rowData))
        line = bufferedReader.readLine()
    }
}
