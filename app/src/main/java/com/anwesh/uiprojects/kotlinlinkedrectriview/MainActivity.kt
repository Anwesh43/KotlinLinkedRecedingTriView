package com.anwesh.uiprojects.kotlinlinkedrectriview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.linkedtrirecview.LinkedRecTriView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LinkedRecTriView.create(this)
    }
}
