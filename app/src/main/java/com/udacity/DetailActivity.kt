package com.udacity

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import org.w3c.dom.Text

class DetailActivity : AppCompatActivity() {
lateinit var Ok_Button : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
      val status_text = findViewById<TextView>(R.id.status_text)
      val file_name = intent.getStringExtra("FileName")
      Log.d("Detail" , file_name!!)
      findViewById<TextView>(R.id.file_name).text = file_name.toString()
      status_text.text= intent.getStringExtra("Status")
      Ok_Button = findViewById<Button>(R.id.ok_button)
      setOnClickListener()
      setSupportActionBar(toolbar)
      fader()
      scaler()
    }

  private fun setOnClickListener() {
    Ok_Button.setOnClickListener {
      val intent = Intent(applicationContext, MainActivity::class.java)
      startActivity(intent)


    }
  }

  private fun fader() {
    val animator = ObjectAnimator.ofFloat(status_text, View.ALPHA, 0f)
    animator.repeatCount = 1
    animator.repeatMode = ObjectAnimator.REVERSE
    animator.start()
  }

  private fun scaler() {
    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
    val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)
    val animator = ObjectAnimator.ofPropertyValuesHolder(
      status_text , scaleX, scaleY , scaleX, scaleY)
    animator.repeatCount = 1
    animator.repeatMode = ObjectAnimator.REVERSE
    animator.start()
  }

}
