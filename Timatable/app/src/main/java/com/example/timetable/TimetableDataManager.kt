package com.example.timetable

import android.content.SharedPreferences
import android.content.res.AssetManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.islandparadise14.mintable.model.ScheduleEntity
import org.json.JSONArray
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList

private var tableX = 5
private var tableY = 6
private val cellSize = 5
//時間割のデータを格納する三次元リスト
private var tableDataArray = ArrayList<ArrayList<ArrayList<String>>>()

var csv_array = ArrayList<ArrayList<String>>()

val scheduleList: ArrayList<ScheduleEntity> = ArrayList()
val day = arrayOf("月", "火", "水", "木", "金")


//プリファレンスの遅延初期化
lateinit var shardPreferences: SharedPreferences
lateinit var shardPrefEditor : SharedPreferences.Editor

fun getColumn(index:Int):ArrayList<String>{
    var ret = ArrayList<String>()
    for (row in csv_array){
        ret.add(row[index])
    }
    val set = ret.toSet()
    ret = ArrayList(set)
    return ret
}


fun findLink(subject_name:String, teacher_name: String, period: String): String {
    Log.v("hikisu",subject_name)
    for (row in csv_array){
        Log.v("row",row.toString())
        if (row[0] == subject_name && row[2] == period && row[4] == teacher_name){
            return row[5]
        }
    }
    return "not found"
}

fun setData(x: Int, y:Int, subject_name: String, class_number: String,
            teacher_name: String, period: String, syllabus_link: String) : Int{

    if (x < tableX && y < tableY){
        tableDataArray[x][y-1][0] = subject_name
        tableDataArray[x][y-1][1] = class_number
        tableDataArray[x][y-1][2] = teacher_name
        tableDataArray[x][y-1][3] = period
        tableDataArray[x][y-1][4] = syllabus_link
        Log.v("tableDataArray", tableDataArray[x][y-1].toString())
        return 0
    }else{
        return 1
    }
}

fun getData(x: Int, y:Int) : ArrayList<String>{
    if(x < tableX && y < tableY){
        return tableDataArray[x][y-1]
    }else{
        return arrayListOf()
    }
}

fun deleteData(x:Int, y:Int){
    if(x < tableX && y < tableY){
        for(i in 0 until  cellSize){
            tableDataArray[x][y-1][i] = ""
        }
    }
}

fun saveData(sharedPreferences: SharedPreferences, sharedPrefEditor: SharedPreferences.Editor){
    for (i in 0 until tableY){
        for (j in 0 until tableX){
            val table_x_y_data = tableDataArray[i][j]
            val json_array_save = JSONArray(table_x_y_data)
            sharedPrefEditor.putString("$j,$i", json_array_save.toString())
        }
    }
    sharedPrefEditor.apply()
}

//プリファレンスからデータを読み込みスケジュール型のリストを返す
fun loadData(sharedPreferences: SharedPreferences, sharedPrefEditor: SharedPreferences.Editor):ArrayList<ScheduleEntity>{
    val scheduleList: ArrayList<ScheduleEntity> = ArrayList()

    for (i in 0 until tableY){
        val loadArrayTwoDimension = ArrayList<ArrayList<String>>()
        for (j in 0 until tableX){
            val loadArrayOneDimension = arrayListOf("","","","","")
            val jsonArrayLoad = JSONArray(sharedPreferences.getString("$j,$i", "[]"))
            for (k in 0 until jsonArrayLoad.length()){
                loadArrayOneDimension[k] = jsonArrayLoad.get(k).toString()
            }
            loadArrayTwoDimension.add(loadArrayOneDimension)
        }
        tableDataArray.add(loadArrayTwoDimension)
    }


    for (i in 0 until tableY){
        for (j in 0 until tableX){
            if (tableDataArray[i][j][0] != "" && tableDataArray[i][j][0] != "null"){
                val schedule = ScheduleEntity(
                    j * i, //originId
                    tableDataArray[i][j][0], //scheduleName
                    tableDataArray[i][j][1], //roomInfo
                    i, //ScheduleDay object (MONDAY ~ SUNDAY)
                    (j+1).toString() + ":00", //startTime format: "HH:mm"
                    (j + 2).toString()+":00", //endTime  format: "HH:mm"
                    "#99bdff", //backgroundColor (optional)
                    "#000000" //textcolor (optional)
                )
                scheduleList.add(schedule)
            }
        }
    }

    return scheduleList
}
