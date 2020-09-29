package com.example.timetable

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.util.Linkify
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.timetable.model.CellDataEntity
import petrov.kristiyan.colorpicker.ColorPicker
import petrov.kristiyan.colorpicker.ColorPicker.OnChooseColorListener


//入力画面処理
class InputScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_screen)

        val subjectNameText = findViewById<AutoCompleteTextView>(R.id.subject_name)
        val classNumberText = findViewById<AutoCompleteTextView>(R.id.class_number)
        val teacherNameText = findViewById<AutoCompleteTextView>(R.id.teacher_name)
        val periodText = findViewById<Spinner>(R.id.period)
        val syllabusLinkText = findViewById<TextView>(R.id.syllabus_link)
        val colorChangeBotton = findViewById<Button>(R.id.color_change_button)

        //スピリットの設定
        val periodAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerArray, R.layout.period_spinner)
        periodAdapter.setDropDownViewResource(R.layout.period_spinner_dropdown)
        periodText.setAdapter(periodAdapter)


        val intent = getIntent()
        val receiveCellData: CellDataEntity? = intent.getSerializableExtra("cellData") as CellDataEntity

        //インテントの中身をテキストエリアに代入
        subjectNameText.setText(receiveCellData?.subjectName)
        classNumberText.setText(receiveCellData?.classNumber)
        teacherNameText.setText(receiveCellData?.teacherName)
        periodText.setSelection((receiveCellData?.period?:"0").toIntOrNull()?:0)
        syllabusLinkText.setText(receiveCellData?.syllabusLink)
        colorChangeBotton.setBackgroundColor(Color.parseColor(receiveCellData?.color))

        var colorChangeButtonBackColor = Color.parseColor(receiveCellData?.color)

        //科目名の入力サジェスト設定
        val subjectNameAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, getColumn(0))
        subjectNameText.setAdapter(subjectNameAdapter)
        subjectNameText.threshold = 1

        //教師名の入力サジェスト設定
        val teacherNameAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, getColumn(4))
        teacherNameText.setAdapter(teacherNameAdapter)
        teacherNameText.threshold = 1

        //シラバスをリンク化
        Linkify.addLinks(syllabusLinkText, Linkify.ALL)

        //戻るボタンが押されたときに時間割が消えるのを防ぐための処理
        intent.putExtra("cellData", receiveCellData)
        setResult(Activity.RESULT_OK,intent)

        //シラバス検索ボタン
        val findSyllabusButton = findViewById<Button>(R.id.find_syllabus_button)
        findSyllabusButton.setOnClickListener{
            syllabusLinkText.text = findLink(subjectNameText.text.toString(),
                teacherNameText.text.toString(), periodText.getSelectedItem().toString())
            Linkify.addLinks(syllabusLinkText, Linkify.ALL)
        }

        //ボタンが押されると入力された値を取得する
        val saveButton: Button = findViewById(R.id.save_button)
        saveButton.setOnClickListener{

            //科目名が入力されている場合はEditTextを返却、入力されていないときは返却しない
            if(subjectNameText.text.isNotEmpty()){
                if(receiveCellData != null){
                    val sendCellData = CellDataEntity(receiveCellData.x,
                        receiveCellData.y,
                        subjectNameText.text.toString(),
                        classNumberText.text.toString(),
                        teacherNameText.text.toString(),
                        periodText.selectedItemPosition.toString(),
                        syllabusLinkText.text.toString(),
                        String.format("#%06X", 0xFFFFFF and colorChangeButtonBackColor)
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
        val deleteButton: Button = findViewById(R.id.delete_button)
        deleteButton.setOnClickListener{
            setResult(Activity.RESULT_CANCELED,intent)
            finish()
        }

        //shapeの動的生成に利用する
        val radius = 36f
        val drawable = GradientDrawable()

        //色検索ボタン
        val colorChangeButton: Button = findViewById(R.id.color_change_button)
        colorChangeButton.setOnClickListener{
            val colorPicker = ColorPicker(this)
            colorPicker.setRoundColorButton(true)
            colorPicker.setDefaultColorButton(Color.parseColor("#7f7fff"))
            colorPicker.show()

            colorPicker.setOnChooseColorListener(object : OnChooseColorListener {
                override fun onChooseColor(position: Int, color: Int) {
                    if (color != 0){
                        //ボタンのshapeを動的に生成
                        drawable.cornerRadius = radius
                        drawable.setColor(color)

                        colorChangeButtonBackColor = color

                        colorChangeButton.setBackgroundDrawable(drawable)
                    }
                }
                override fun onCancel() {

                }
            })
        }

        //ボタンのshapeを動的に生成
        drawable.cornerRadius = radius
        drawable.setColor(colorChangeButtonBackColor)
        colorChangeButton.setBackgroundDrawable(drawable)
    }
}