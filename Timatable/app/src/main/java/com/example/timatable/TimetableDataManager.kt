package com.example.timatable

import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.islandparadise14.mintable.model.ScheduleEntity
import org.json.JSONArray
import java.lang.Exception

class TimetableDataManager : AppCompatActivity() {

    private var tableX = 5
    private var tableY = 6
    private val cellSize = 2
    //時間割のデータを格納する三次元リスト
    private var tableDataArray = ArrayList<ArrayList<ArrayList<String>>>()


    fun setData(x: Int, y:Int, subject_name: String, class_number: String) : Int{

        if (x < tableX && y < tableY){
            this.tableDataArray[x][y-1][0] = subject_name
            this.tableDataArray[x][y-1][1] = class_number
            return 1
        }else{
            return 0
        }
    }

    fun deleteData(x:Int, y:Int){
        if(x < tableX && y < tableY){
            tableDataArray[x][y-1][0] = ""
            tableDataArray[x][y-1][1] = ""
        }
    }

    fun getData(x: Int, y:Int) : ArrayList<String>{
        if(x < tableX && y < tableY){
            return tableDataArray[x][y-1]
        }else{
            return arrayListOf()
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

        ///var load_array = ArrayList<ArrayList<ArrayList<String>>>()
        for (i in 0 until tableY){
            var loadArrayTwoDimension = ArrayList<ArrayList<String>>()
            for (j in 0 until tableX){
                var loadArrayOneDimension = ArrayList<String>()
                val jsonArrayLoad = JSONArray(sharedPreferences.getString("$j,$i", "[]"));
                for (k in 0 until jsonArrayLoad.length()){
                    loadArrayOneDimension.add(jsonArrayLoad.get(k).toString())
                }
                loadArrayTwoDimension.add(loadArrayOneDimension)
            }
            this.tableDataArray.add(loadArrayTwoDimension)
        }

        for (i in 0 until tableY){
            for (j in 0 until tableX){
                if (tableDataArray[i][j][0] != "" && tableDataArray[i][j][0] != "null"){
                    Log.v("table", this.tableDataArray[i][j][0].toString())
                    val schedule = ScheduleEntity(
                        j * i, //originId
                        tableDataArray[i][j][0], //scheduleName
                        tableDataArray[i][j][1], //roomInfo
                        i, //ScheduleDay object (MONDAY ~ SUNDAY)
                        (j+1).toString() + ":00", //startTime format: "HH:mm"
                        (j + 2).toString()+":00", //endTime  format: "HH:mm"
                        "#73fcae68", //backgroundColor (optional)
                        "#000000" //textcolor (optional)
                    )
                    scheduleList.add(schedule)
                }
            }
        }


        Log.v("table_array", this.tableDataArray.toString())
        return scheduleList
    }
}