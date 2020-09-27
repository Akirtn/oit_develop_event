package com.example.timetable

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.util.Linkify
import android.widget.*
import com.example.timetable.model.CellDataEntity
import kotlinx.android.synthetic.main.activity_input_screen.*

//入力画面処理
class InputScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_screen)

        val subjectNameText = findViewById(R.id.subject_name) as AutoCompleteTextView
        val classNumberText = findViewById(R.id.class_number) as AutoCompleteTextView
        val teacherNameText = findViewById(R.id.teacher_name) as AutoCompleteTextView
        val periodText = findViewById(R.id.period) as AutoCompleteTextView
        val syllabusLinkText = findViewById(R.id.syllabus_link) as TextView

        val intent = getIntent()
        val receiveCellData: CellDataEntity? = intent.getSerializableExtra("cellData") as CellDataEntity

        //インテントの中身をテキストエリアに代入
        subjectNameText.setText(receiveCellData?.subjectName)
        classNumberText.setText(receiveCellData?.classNumber)
        teacherNameText.setText(receiveCellData?.teacherName)
        periodText.setText(receiveCellData?.period)
        syllabusLinkText.setText(receiveCellData?.syllabus_link)

        //科目名の入力サジェスト設定
        val subjectNameAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, getColumn(0))
        subjectNameText.setAdapter(subjectNameAdapter)
        subjectNameText.setThreshold(1)

        //教師名の入力サジェスト設定
        val teacherNameAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, getColumn(4))
        teacherNameText.setAdapter(teacherNameAdapter)
        teacherNameText.setThreshold(1)

        //シラバスをリンク化
        Linkify.addLinks(syllabusLinkText, Linkify.ALL)

        //シラバス検索ボタン
        val findSyllabusButton = findViewById<Button>(R.id.find_syllabus_button)
        findSyllabusButton.setOnClickListener{
            syllabusLinkText.text = findLink(subjectNameText.text.toString(),
                teacherNameText.text.toString(), periodText.text.toString())
            Linkify.addLinks(syllabusLinkText, Linkify.ALL)
        }

        //戻るボタンが押されたときに時間割が消えるのを防ぐための処理
        intent.putExtra("cellData", receiveCellData)
        setResult(Activity.RESULT_OK,intent)

        //ボタンが押されると入力された値を取得する
        val save_button: Button = findViewById(R.id.save_button)
        save_button.setOnClickListener{

            //科目名が入力されている場合はEditTextを返却、入力されていないときは返却しない
            if(subjectNameText.text.length > 0){
                if(receiveCellData != null){
                    val sendCellData = CellDataEntity(receiveCellData.x,
                        receiveCellData.y,
                        subjectNameText.text.toString(),
                        classNumberText.text.toString(),
                        teacherNameText.text.toString(),
                        periodText.text.toString(),
                        syllabus_link.text.toString()
                    )

                    intent.putExtra("cellData", sendCellData)
                    setResult(Activity.RESULT_OK,intent)
                }

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