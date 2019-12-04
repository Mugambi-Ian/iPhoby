package com.iCropal.iPhobia.Ui.RecordsUi

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.iCropal.iPhobia.R
import com.iCropal.iPhobia.Utility.HeartRateMeter.HeartRateOmeter
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_record_session.*
import net.kibotu.kalmanrx.jama.Matrix
import net.kibotu.kalmanrx.jkalman.JKalman

class RecordHeartBeat : AppCompatActivity() {

    var subscription: CompositeDisposable? = null
    var heartBeatManager: HeartBeatManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_session)

    }

    override fun onBackPressed() {
        heartBeatManager?.onFinish()
    }

    fun startWithPermissionCheck() {
        if (!hasPermission(Manifest.permission.CAMERA)) {
            checkPermissions(REQUEST_CAMERA_PERMISSION, Manifest.permission.CAMERA)
            return
        }
        val kalman = JKalman(2, 1)
        val m = Matrix(1, 1)
        val tr = arrayOf(doubleArrayOf(1.0, 0.0), doubleArrayOf(0.0, 1.0))
        kalman.transition_matrix = Matrix(tr)
        kalman.error_cov_post = kalman.error_cov_post.identity()
        val h = HeartRateOmeter()
        val bpmUpdates = h.withAverageAfterSeconds(3)
                .setFingerDetectionListener(this::onFingerChange)
                .bpmUpdates(preview)
                .subscribe({
                    if (it.value == 0)
                        return@subscribe
                    m.set(0, 0, it.value.toDouble())
                    val s = kalman.Predict()
                    val c = kalman.Correct(m)

                    val bpm = it.copy(value = c.get(0, 0).toInt())
                    onBpm(bpm)
                }, Throwable::printStackTrace)
        heartBeatManager!!.setHeartRateOMeter(h)
        subscription?.add(bpmUpdates)
    }

    @SuppressLint("SetTextI18n")
    private fun onBpm(bpm: HeartRateOmeter.Bpm) {
        heartBeatManager!!.setBpm(bpm)
        label.text = "$bpm"
    }

    private fun onFingerChange(fingerDetected: Boolean) {
        finger.text = "$fingerDetected"
        heartBeatManager!!.updateVibration(fingerDetected)
    }


    override fun onResume() {
        super.onResume()
        dispose()
        subscription = CompositeDisposable()
        if (heartBeatManager == null) {
            heartBeatManager = HeartBeatManager()
            heartBeatManager!!.init(this)
        }

    }

    override fun onPause() {
        dispose()
        super.onPause()
    }

    private fun dispose() {
        if (subscription?.isDisposed == false && heartBeatManager?.stopReading == false)
            subscription?.dispose()
    }

    override fun onStop() {
        if (heartBeatManager != null) {
            heartBeatManager!!.onFinish()
            heartBeatManager = null
        }
        super.onStop()
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 123
    }

    private fun checkPermissions(callbackId: Int, vararg permissionsId: String) {
        when {
            !hasPermission(*permissionsId) -> try {
                ActivityCompat.requestPermissions(this, permissionsId, callbackId)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun hasPermission(vararg permissionsId: String): Boolean {
        var hasPermission = true

        permissionsId.forEach { permission ->
            hasPermission = hasPermission
                    && ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }

        return hasPermission
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startWithPermissionCheck()
                }
            }
        }
    }

}