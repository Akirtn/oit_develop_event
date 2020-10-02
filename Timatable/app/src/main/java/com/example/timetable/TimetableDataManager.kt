package com.example.timetable

import android.content.SharedPreferences
import android.util.Log
import com.example.timetable.model.CellDataEntity
import com.example.timetable.model.cellDataEntitySize
import com.islandparadise14.mintable.model.ScheduleEntity
import org.json.JSONArray
import kotlin.collections.ArrayList

private var tableX = 5
private var tableY = 6

//時間割のデータを格納する三次元リスト
private var tableDataArray = ArrayList<ArrayList<ArrayList<String>>>()

val scheduleList: ArrayList<ScheduleEntity> = ArrayList()
val day = arrayOf("月", "火", "水", "木", "金")

//プリファレンスの遅延初期化
lateinit var shardPreferences: SharedPreferences
lateinit var shardPrefEditor : SharedPreferences.Editor


fun setData(cellData: CellDataEntity) : Int{
    return if (cellData.x < tableX && cellData.y < tableY){
        tableDataArray[cellData.x][cellData.y-1][0] = cellData.subjectName
        tableDataArray[cellData.x][cellData.y-1][1] = cellData.classNumber
        tableDataArray[cellData.x][cellData.y-1][2] = cellData.teacherName
        tableDataArray[cellData.x][cellData.y-1][3] = cellData.period
        tableDataArray[cellData.x][cellData.y-1][4] = cellData.syllabusLink
        tableDataArray[cellData.x][cellData.y-1][5] = cellData.color
        0
    }else{
        1
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
        for(i in 0 until  cellDataEntitySize){
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
fun loadSyllabusData(sharedPreferences: SharedPreferences, sharedPrefEditor: SharedPreferences.Editor):ArrayList<ScheduleEntity>{
    val scheduleList: ArrayList<ScheduleEntity> = ArrayList()

    for (i in 0 until tableY){
        val loadArrayTwoDimension = ArrayList<ArrayList<String>>()
        for (j in 0 until tableX){
            val loadArrayOneDimension = arrayListOf<String>()
            for (k in 0 until cellDataEntitySize){ loadArrayOneDimension.add("") }
            val jsonArrayLoad = JSONArray(sharedPreferences.getString("$j,$i", "[]"))
            for (l in 0 until jsonArrayLoad.length()){
                loadArrayOneDimension[l] = jsonArrayLoad.get(l).toString()
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
                    (j + 1).toString() + ":00", //startTime format: "HH:mm"
                    (j + 2).toString()+":00", //endTime  format: "HH:mm"
                    tableDataArray[i][j][5], //backgroundColor (optional)
                    "#000000" //textcolor (optional)
                )
                scheduleList.add(schedule)
            }
        }
    }

    return scheduleList
}
