package com.example.timatable

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

//入力画面処理
class InputScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_screen)

        //ボタンが押されると入力された値を取得する
        val button: Button = findViewById(R.id.input_screen_button)
        button.setOnClickListener{
            var subject_name_text = findViewById<EditText>(R.id.subject_name) as EditText
            var class_number_text = findViewById<EditText>(R.id.class_number) as EditText

            //科目名が入力されている場合はEditTextを返却、入力されていないときは返却しない
            if(subject_name_text.text.length > 0){
                val intent = Intent(this,InputScreen::class.java)
                intent.putExtra("subject_name",subject_name_text.text.toString())
                intent.putExtra("class_number",class_number_text.text.toString())
                setResult(Activity.RESULT_OK,intent)
            }else{
                setResult(Activity.RESULT_CANCELED,intent)
            }
            finish()
        }
    }
}