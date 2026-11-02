package ro.pub.cs.systems.eim.practicaltest01

import android.content.Context
import android.content.Intent
import java.util.Date
import kotlin.math.sqrt

class ProcessingThread (
    private val context: Context,
    firstNumber: Int,
    secondNumber: Int
) : Thread() {

    @Volatile private var isRunning = true
    private val arithmeticMean = (firstNumber + secondNumber) / 2.0
    private val geometricMean = sqrt((firstNumber * secondNumber).toDouble())

    override fun run() {
        while (isRunning) {
            sendMessage()
            try {
                sleep(10000)
            } catch (e: InterruptedException) {
                stopThread()
            }
        }
    }

    private fun sendMessage() {
        val intent = Intent()
        intent.action = Constants.ACTIONS.random()
        intent.putExtra(Constants.BROADCAST_EXTRA, "${Date()} $arithmeticMean $geometricMean")
        intent.setPackage(context.packageName)
        context.sendBroadcast(intent)
    }

    fun stopThread() {
        isRunning = false
        interrupt()
    }
}