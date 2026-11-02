package ro.pub.cs.systems.eim.practicaltest01

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class PracticalTest01Service : Service() {

    private var processingThread: ProcessingThread? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val firstNumber = intent?.getIntExtra(Constants.FIRST_NUMBER, 0) ?: 0
        val secondNumber = intent?.getIntExtra(Constants.SECOND_NUMBER, 0) ?: 0
        Log.d(Constants.TAG, "Service started with $firstNumber and $secondNumber")

        processingThread = ProcessingThread(this, firstNumber, secondNumber)
        processingThread?.start()

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        processingThread?.stopThread()
        Log.d(Constants.TAG, "Service stopped")
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
