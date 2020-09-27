package com.example.timetable

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
import android.widget.*
import com.example.timetable.model.CellDataEntity
import kotlinx.android.synthetic.main.activity_input_screen.*

//入力画面処理
class InputScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_screen)

        var subject_name_text = findViewById<AutoCompleteTextView>(R.id.subject_name) as AutoCompleteTextView
        var class_number_text = findViewById<AutoCompleteTextView>(R.id.class_number) as AutoCompleteTextView
        var teacher_name_text = findViewById<AutoCompleteTextView>(R.id.teacher_name) as AutoCompleteTextView
        var period_text = findViewById<AutoCompleteTextView>(R.id.period) as AutoCompleteTextView
        var syllabus_link_text = findViewById<TextView>(R.id.syllabus_link) as TextView

        val intent = getIntent()
        val receiveCellData: CellDataEntity? = intent.getSerializableExtra("cellData") as CellDataEntity

        subject_name_text.setText(receiveCellData?.subjectName)
        class_number_text.setText(receiveCellData?.classNumber)
        teacher_name_text.setText(receiveCellData?.teacherName)
        period_text.setText(receiveCellData?.period)
        syllabus_link_text.setText(receiveCellData?.syllabus_link)

        val subject_name_adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, getColumn(0))
        subject_name_text.setAdapter(subject_name_adapter)
        subject_name_text.setThreshold(1)
        val teacher_name_adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, getColumn(4))
        teacher_name_text.setAdapter(teacher_name_adapter)
        teacher_name_text.setThreshold(1)

        Linkify.addLinks(syllabus_link_text, Linkify.ALL)

        //シラバス検索ボタン
        val find_syllabus_button = findViewById<Button>(R.id.find_syllabus_button)
        find_syllabus_button.setOnClickListener{
            syllabus_link_text.text = findLink(subject_name_text.text.toString(),
                teacher_name_text.text.toString(), period_text.text.toString())
            Linkify.addLinks(syllabus_link_text, Linkify.ALL)
        }

        intent.putExtra("cellData", receiveCellData)
        setResult(Activity.RESULT_OK,intent)

        //ボタンが押されると入力された値を取得する
        val save_button: Button = findViewById(R.id.save_button)
        save_button.setOnClickListener{

            //科目名が入力されている場合はEditTextを返却、入力されていないときは返却しない
            if(subject_name_text.text.length > 0){
                if(receiveCellData != null){
                    val sendCellData = CellDataEntity(receiveCellData.x,
                        receiveCellData.y,
                        subject_name_text.text.toString(),
                        class_number_text.text.toString(),
                        teacher_name_text.text.toString(),
                        period_text.text.toString(),
                        syllabus_link.text.toString()
                    )

                    intent.putExtra("cellData", sendCellData)
                    setResult(Activity.RESULT_OK,intent)
                }

                //val intent = Intent(this,InputScreen::class.java)

            }else{
                setResult(Activity.RESULT_CANCELED,intent)
            }
            finish()
        }

        //削除ボタン
        val delete_button: Button = findViewById(R.id.delete_button)
        delete_button.setOnClickListener{
            setResult(Activity.RESULT_CANCELED,intent)
            finish()
        }
    }
}