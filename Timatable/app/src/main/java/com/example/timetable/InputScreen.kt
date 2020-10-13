package com.example.timetable

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.timetable.model.CellDataEntity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import petrov.kristiyan.colorpicker.ColorPicker
import petrov.kristiyan.colorpicker.ColorPicker.OnChooseColorListener


//入力画面処理
class InputScreen : AppCompatActivity() {

    private lateinit var subjectNameText: AutoCompleteTextView
    private lateinit var classNumberText: AutoCompleteTextView
    private lateinit var teacherNameText: AutoCompleteTextView
    private lateinit var periodText: Spinner
    private lateinit var syllabusLinkText: TextView
    private lateinit var colorChangeButton: Button
    private lateinit var receiveCellData: CellDataEntity

    private var link:String = ""


    private var colorChangeButtonBackColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_screen)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        subjectNameText = findViewById(R.id.subject_name)
        classNumberText = findViewById(R.id.class_number)
        teacherNameText = findViewById(R.id.teacher_name)
        periodText = findViewById(R.id.period)
        syllabusLinkText = findViewById(R.id.syllabus_link)
        colorChangeButton = findViewById(R.id.color_change_button)

        //スピリットの設定
        val periodAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerArray, R.layout.period_spinner)
        periodAdapter.setDropDownViewResource(R.layout.period_spinner_dropdown)
        periodText.adapter = periodAdapter


        receiveCellData = intent.getSerializableExtra("cellData") as CellDataEntity


        //インテントの中身をテキストエリアに代入
        subjectNameText.setText(receiveCellData.subjectName)
        classNumberText.setText(receiveCellData.classNumber)
        teacherNameText.setText(receiveCellData.teacherName)
        periodText.setSelection((receiveCellData.period?:"0").toIntOrNull()?:0)
        syllabusLinkText.text = receiveCellData.syllabusLink

        colorChangeButtonBackColor = Color.parseColor(receiveCellData.color)

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
        if(syllabusLinkText.text.isNotEmpty()){
            syllabusLinkText.linksClickable = true
            syllabusLinkText.text = Html.fromHtml("<a href=${syllabusLinkText.text}>${subjectNameText.text}</a>")
            syllabusLinkText.movementMethod = LinkMovementMethod.getInstance()
        }

        //戻るボタンが押されたときに時間割が消えるのを防ぐための処理
        intent.putExtra("cellData", receiveCellData)
        setResult(Activity.RESULT_OK,intent)

        //シラバス検索ボタン
        val findSyllabusButton = findViewById<Button>(R.id.find_syllabus_button)
        findSyllabusButton.setOnClickListener{
            link = findLink(subjectNameText.text.toString(),
                teacherNameText.text.toString(), periodText.getSelectedItem().toString())
            if(link != "not found"){
                syllabusLinkText.linksClickable = true
                syllabusLinkText.text = Html.fromHtml("<a href=$link>${subjectNameText.text}</a>")
                syllabusLinkText.movementMethod = LinkMovementMethod.getInstance()
            }else{
                Snackbar.make(syllabusLinkText, "科目名　教員名（カナ）　学期を入力してください", Snackbar.LENGTH_LONG)
                    .show()
            }
        }


        //色検索ボタン
        this.colorChangeButton.setOnClickListener{
            val colorPicker = ColorPicker(this)
                .setColors(
                    Color.parseColor("#ff7f7f"),
                    Color.parseColor("#ff7fbf"),
                    Color.parseColor("#ff7fff"),
                    Color.parseColor("#bf7fff"),
                    Color.parseColor("#7f7fff"),
                    Color.parseColor("#7fbfff"),
                    Color.parseColor("#7fffff"),
                    Color.parseColor("#7fffbf"),
                    Color.parseColor("#7fff7f"),
                    Color.parseColor("#bfff7f"),
                    Color.parseColor("#ffff7f"),
                    Color.parseColor("#ffbf7f"),
                    Color.parseColor("#e6e6fa"),
                    Color.parseColor("#cccccc"),
                    Color.parseColor("#999999")
                )
            colorPicker.setRoundColorButton(true)
            colorPicker.setDefaultColorButton(Color.parseColor("#7f7fff"))
            colorPicker.show()

            colorPicker.setOnChooseColorListener(object : OnChooseColorListener {
                override fun onChooseColor(position: Int, color: Int) {
                    if (color != 0){
                        colorChangeButtonBackColor = color
                    }
                }
                override fun onCancel() {

                }
            })
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_delete -> {
            // User chose the "Settings" item, show the app settings UI...
            setResult(-2,intent)
            if(scheduleEntity == null){
                finish()
            }
            scheduleList.remove(scheduleEntity)
            deleteData(receiveCellData.x,receiveCellData.y)
            finish()
            true
        }

        R.id.action_save -> {
            // User chose the "Settings" item, show the app settings UI...
            if(subjectNameText.text.isNotEmpty()){
                val sendCellData = CellDataEntity(receiveCellData.x,
                    receiveCellData.y,
                    subjectNameText.text.toString(),
                    classNumberText.text.toString(),
                    teacherNameText.text.toString(),
                    periodText.selectedItemPosition.toString(),
                    link,
                    String.format("#%06X", 0xFFFFFF and colorChangeButtonBackColor)
                )
                intent.putExtra("cellData", sendCellData)
                setResult(100,intent)

            }else{
                setResult(Activity.RESULT_CANCELED,intent)
            }
            finish()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            setResult(-3,intent)
            finish()
            super.onOptionsItemSelected(item)
        }
    }

}
