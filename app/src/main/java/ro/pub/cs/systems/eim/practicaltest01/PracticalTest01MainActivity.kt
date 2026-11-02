package ro.pub.cs.systems.eim.practicaltest01

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PracticalTest01MainActivity : AppCompatActivity() {
    private lateinit var editTextLeft: EditText
    private lateinit var editTextRight: EditText

    private lateinit var buttonLeft: Button
    private lateinit var buttonRight: Button

    private lateinit var buttonNavigate: Button

    private val buttonClickListener = ButtonClickListener()

    private val messageBrpadcastReceiver = MessageBroadcastReciever()

    private val intentFilter = IntentFilter()

    private val secondaryActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result -> Toast.makeText(this, "The activity returned with the result ${result.resultCode}",
            Toast.LENGTH_LONG).show()
        }

    private var serviceStarted = false



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
        buttonNavigate = findViewById(R.id.ButtonNavigate)

        buttonLeft.setOnClickListener(buttonClickListener)
        buttonRight.setOnClickListener(buttonClickListener)
        buttonNavigate.setOnClickListener(buttonClickListener)

        for (action in Constants.ACTIONS)
            intentFilter.addAction(action)


    }

    private inner class ButtonClickListener: View.OnClickListener {
        override fun onClick(view: View) {
            when (view.id) {
                R.id.ButtonLeft -> {
                    val count = editTextLeft.text.toString().toInt() + 1
                    editTextLeft.setText(count.toString())
                    startServiceIfNeeded()
                }
                R.id.ButtonRight -> {
                    val count = editTextRight.text.toString().toInt() + 1
                    editTextRight.setText(count.toString())
                    startServiceIfNeeded()
                }
                R.id.ButtonNavigate -> {
                    val intent = Intent(this@PracticalTest01MainActivity,
                        PracticalTest01SecondaryActivity::class.java)
                    val total = editTextLeft.text.toString().toInt() + editTextRight.text.toString().toInt()
                    intent.putExtra(Constants.NUMBER_OF_CLICKS, total)
                    secondaryActivityLauncher.launch(intent)
                }
            }

        }
    }

    private inner class MessageBroadcastReciever: BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val message = intent.getStringExtra(Constants.BROADCAST_EXTRA)
            Log.d(Constants.TAG, "Received [${intent.action}]: $message")
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

    private fun startServiceIfNeeded() {
        val left = editTextLeft.text.toString().toInt()
        val right = editTextRight.text.toString().toInt()

        if (left + right > Constants.NUMBER_OF_CLICKS_THRESHOLD && !serviceStarted) {
            val intent = Intent(this, PracticalTest01Service::class.java)
            intent.putExtra(Constants.FIRST_NUMBER, left)
            intent.putExtra(Constants.SECOND_NUMBER, right)
            startService(intent)
            serviceStarted = true
        }
    }

    override fun onDestroy() {
        val intent = Intent(this, PracticalTest01Service::class.java)
        stopService(intent)
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        ContextCompat.registerReceiver(
            this,
            messageBrpadcastReceiver,
            intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onPause() {
        unregisterReceiver(messageBrpadcastReceiver)
        super.onPause()
    }
}