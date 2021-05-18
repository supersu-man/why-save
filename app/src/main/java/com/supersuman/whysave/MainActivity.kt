package com.supersuman.whysave

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.text.FieldPosition
import kotlin.concurrent.thread
import kotlin.math.atan2


class MainActivity : AppCompatActivity() {
    private lateinit var knob : ImageView
    private lateinit var phonenumberEditText: TextInputEditText
    private lateinit var button: ImageButton
    private lateinit var  textview1 : TextView
    private lateinit var  textview2 : TextView
    private lateinit var  textview3 : TextView
    private lateinit var  textview4 : TextView
    private lateinit var  textview5 : TextView
    private lateinit var  textview6 : TextView
    private lateinit var  textview7 : TextView
    private lateinit var  textview8 : TextView
    var countryCode = "91"
    private var mCurrAngle = 0.0
    private var mPrevAngle = 0.0
    private var countryCodes : MutableList<List<String>> = mutableListOf(listOf())
    private var textViewIds : MutableList<TextView> = mutableListOf()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        addCountryCodes()
        addTextViewIds()
        setCountryNamesCodesToViews()
        setKnobTouchListener()

        button.setOnClickListener {
            closeKeyboard()
            val countryCodeTemp = countryCode
            if (phonenumberEditText.text?.contains("+") == true){
                countryCode=""
            }
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send/?phone=%2B" + countryCode + phonenumberEditText.text.toString() + "&text&app_absent=0"))
            startActivity(browserIntent)
            countryCode = countryCodeTemp
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setKnobTouchListener() {
        knob.setOnTouchListener { _, event ->
            val xc: Int = knob.width / 2
            val yc: Int = knob.height / 2

            val x = event.x
            val y = event.y

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mCurrAngle =
                        Math.toDegrees(atan2((x - xc).toDouble(), (yc - y).toDouble()))
                }
                MotionEvent.ACTION_MOVE -> {
                    mPrevAngle = mCurrAngle
                    mCurrAngle =
                        Math.toDegrees(atan2((x - xc).toDouble(), (yc - y).toDouble()))
                    animate(mPrevAngle, mCurrAngle)
                    higlightCoutryText(mCurrAngle)
                }
                MotionEvent.ACTION_UP -> {
                    mCurrAngle = 0.0
                    mPrevAngle = mCurrAngle
                }
            }
            return@setOnTouchListener true
        }
    }

    private fun higlightCoutryText(mCurrAngle: Double) {
        if (mCurrAngle>-22.5 && mCurrAngle<22.5){
            increaseTextSize(0)
            countryCode = countryCodes[0][1]
        } else if (mCurrAngle>22.5 && mCurrAngle<=67.5){
            increaseTextSize(1)
            countryCode = countryCodes[1][1]
        } else if (mCurrAngle>67.5 && mCurrAngle<=112.5){
            increaseTextSize(2)
            countryCode = countryCodes[2][1]
        } else if (mCurrAngle>112.5 && mCurrAngle<=157.5){
            increaseTextSize(3)
            countryCode = countryCodes[3][1]
        } else if (mCurrAngle>157.5 || mCurrAngle<-157.5){
            increaseTextSize(4)
            countryCode = countryCodes[4][1]
        } else if (mCurrAngle>-157.5 && mCurrAngle<=-112.5){
            increaseTextSize(5)
            countryCode = countryCodes[5][1]
        } else if (mCurrAngle>-112.5 && mCurrAngle<=-67.5){
            increaseTextSize(6)
            countryCode = countryCodes[6][1]
        } else if (mCurrAngle>-67.5 && mCurrAngle<=-22.5){
            increaseTextSize(7)
            countryCode = countryCodes[7][1]
        }
    }

    private fun increaseTextSize(position : Int){
        for(i in textViewIds.indices){
            if(position == i){
                textViewIds[i].textSize = 20.0F
                textViewIds[i].typeface = Typeface.DEFAULT_BOLD
            }else{
                textViewIds[i].textSize = 14.0F
                textViewIds[i].typeface = Typeface.DEFAULT
            }
        }
    }

    private fun addTextViewIds() {
        textViewIds = mutableListOf(textview1,textview2,textview3,textview4,textview5,textview6,textview7,textview8)
    }

    @SuppressLint("SetTextI18n")
    private fun setCountryNamesCodesToViews() {
        for (i in textViewIds.indices){
            textViewIds[i].text = countryCodes[i][0]+"\n+"+countryCodes[i][1]
        }
    }

    private fun initViews(){
        knob= findViewById(R.id.knob)
        phonenumberEditText = findViewById(R.id.phonenumberEditText)
        button = findViewById(R.id.myButton)
        textview1 =findViewById(R.id.textView1)
        textview1.textSize = 20.0F
        textview1.typeface = Typeface.DEFAULT_BOLD
        textview2 =findViewById(R.id.textView2)
        textview3 =findViewById(R.id.textView3)
        textview4 =findViewById(R.id.textView4)
        textview5 =findViewById(R.id.textView5)
        textview6 =findViewById(R.id.textView6)
        textview7 =findViewById(R.id.textView7)
        textview8 =findViewById(R.id.textView8)
    }

    private  fun addCountryCodes(){
        countryCodes = mutableListOf(
            listOf("India","91"),
            listOf("Brazil","55"),
            listOf("USA","1"),
            listOf("Indonesia","62"),
            listOf("Russia","7"),
            listOf("Mexico","52"),
            listOf("Germany","49"),
            listOf("Italy","39"))
    }

    private fun animate(fromDegrees: Double, toDegrees: Double) {
        val rotate = RotateAnimation(
            fromDegrees.toFloat(), toDegrees.toFloat(),
        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
        RotateAnimation.RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 0
        rotate.isFillEnabled = true
        rotate.fillAfter = true
        knob.startAnimation(rotate)
    }

    private fun closeKeyboard() {
        val view: View? = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}