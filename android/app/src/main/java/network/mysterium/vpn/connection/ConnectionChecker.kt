package network.mysterium.vpn.connection

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log

/**
 * Starts ConnectionCheckerService periodically at a given interval.
 */
class ConnectionChecker(private val context: Context, private val interval: Long) {
  private var running: Boolean = false

  fun start() {
    if (running) {
      return
    }

    Log.d(TAG, "Starting ConnectionChecker")
    running = true
    this.loopService(ignoreLastStatus = true)
  }

  fun stop() {
    if (!running) {
      return
    }

    Log.d(TAG, "Stopping ConnectionChecker")
    running = false
  }

  private fun loopService(ignoreLastStatus: Boolean) {
    if (!running) {
      return
    }
    startService(ignoreLastStatus)

    Handler().postDelayed({ this.loopService(ignoreLastStatus = false) }, interval)
  }

  private fun startService(ignoreLastStatus: Boolean) {
    Log.d(TAG, "Starting ConnectionCheckerService")
    val service = Intent(context, ConnectionCheckerService::class.java)
    val bundle = Bundle()
    bundle.putBoolean("ignoreLastStatus", ignoreLastStatus)
    service.putExtras(bundle)
    context.startService(service)
  }

  companion object {
    private const val TAG = "ConnectionChecker"
  }
}