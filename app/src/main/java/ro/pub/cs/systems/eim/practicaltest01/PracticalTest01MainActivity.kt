package ro.pub.cs.systems.eim.practicaltest01

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PracticalTest01MainActivity : AppCompatActivity() {
    private lateinit var editTextLeft: EditText
    private lateinit var editTextRight: EditText

    private lateinit var buttonLeft: Button
    private lateinit var buttonRight: Button

    private val buttonClickListener = ButtonClickListener()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editTextLeft = findViewById(R.id.EditTextLeft)
        editTextRight = findViewById(R.id.EditTextRight)
        buttonLeft = findViewById(R.id.ButtonLeft)
        buttonRight = findViewById(R.id.ButtonRight)

        buttonLeft.setOnClickListener(buttonClickListener)
        buttonRight.setOnClickListener(buttonClickListener)


    }

    private inner class ButtonClickListener: View.OnClickListener {
        override fun onClick(view: View) {
            when (view.id) {
                R.id.ButtonLeft -> {
                    val count = editTextLeft.text.toString().toInt() + 1
                    editTextLeft.setText(count.toString())
                }
                R.id.ButtonRight -> {
                    val count = editTextRight.text.toString().toInt() + 1
                    editTextRight.setText(count.toString())
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.LEFT_COUNT, editTextLeft.text.toString())
        outState.putString(Constants.RIGHT_COUNT, editTextRight.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.containsKey(Constants.LEFT_COUNT)) {
            editTextLeft.setText(savedInstanceState.getString(Constants.LEFT_COUNT))
        }
        if (savedInstanceState.containsKey(Constants.RIGHT_COUNT)) {
            editTextRight.setText(savedInstanceState.getString(Constants.RIGHT_COUNT))
        }
    }
}