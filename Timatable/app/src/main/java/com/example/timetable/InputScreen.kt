package com.example.timetable

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.util.Linkify
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

//入力画面処理
class InputScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_screen)

        var subject_name_text = findViewById<EditText>(R.id.subject_name) as EditText
        var class_number_text = findViewById<EditText>(R.id.class_number) as EditText
        var teacher_name_text = findViewById<EditText>(R.id.teacher_name) as EditText
        var period_text = findViewById<EditText>(R.id.period) as EditText
        var syllabus_link_text = findViewById<TextView>(R.id.syllabus_link) as TextView

        val intent = getIntent()

        subject_name_text.setText(intent.extras?.getString("subject_name")?:"")
        class_number_text.setText(intent.extras?.getString("class_number")?:"")
        teacher_name_text.setText(intent.extras?.getString("teacher_name")?:"")
        period_text.setText(intent.extras?.getString("period")?:"")
        syllabus_link_text.setText(intent.extras?.getString("syllabus_link")?:"")

        Linkify.addLinks(syllabus_link_text, Linkify.ALL)

       //シラバス検索ボタン
        val find_syllabus_button = findViewById<Button>(R.id.find_syllabus_button)
        find_syllabus_button.setOnClickListener{
            syllabus_link_text.text = findLink(subject_name_text.text.toString(),
                    teacher_name_text.text.toString(), period_text.text.toString())
            Linkify.addLinks(syllabus_link_text, Linkify.ALL)
        }

        //ボタンが押されると入力された値を取得する
        val save_button: Button = findViewById(R.id.save_button)
        save_button.setOnClickListener{

            //科目名が入力されている場合はEditTextを返却、入力されていないときは返却しない
            if(subject_name_text.text.length > 0){
                val intent = Intent(this,InputScreen::class.java)
                intent.putExtra("subject_name",subject_name_text.text.toString())
                intent.putExtra("class_number",class_number_text.text.toString())
                intent.putExtra("teacher_name",teacher_name_text.text.toString())
                intent.putExtra("period", period_text.text.toString())
                intent.putExtra("syllabus_link", syllabus_link_text.text.toString())
                setResult(Activity.RESULT_OK,intent)
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