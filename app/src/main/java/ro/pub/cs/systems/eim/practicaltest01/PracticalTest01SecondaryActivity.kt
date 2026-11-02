package ro.pub.cs.systems.eim.practicaltest01

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PracticalTest01SecondaryActivity : AppCompatActivity() {
    private lateinit var textViewSecondary: TextView
    private lateinit var buttonOK: Button
    private lateinit var buttonCancel: Button

    private val buttonClickListener = ButtonClickListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_secondary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textViewSecondary = findViewById(R.id.TextViewSecondary)
        buttonOK = findViewById(R.id.ButtonOK)
        buttonCancel = findViewById(R.id.ButtonCancel)

        val numberOfClicks = intent.getIntExtra(Constants.NUMBER_OF_CLICKS, 0)
        textViewSecondary.text = numberOfClicks.toString()

        buttonOK.setOnClickListener(buttonClickListener)
        buttonCancel.setOnClickListener(buttonClickListener)
    }

    private inner class ButtonClickListener: View.OnClickListener {
        override fun onClick(view: View) {
            when (view.id) {
                R.id.ButtonOK -> setResult(RESULT_OK)
                R.id.ButtonCancel -> setResult(RESULT_CANCELED)
            }
            finish()
        }
    }
}