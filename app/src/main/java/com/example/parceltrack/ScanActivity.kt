package com.example.parceltrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScanActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        db = DatabaseHelper(this)

        findViewById<Button>(R.id.btnStartScan).setOnClickListener { startScan() }
        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }
    }

    private fun startScan() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt("송장 바코드를 스캔하세요")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(false)
        integrator.initiateScan()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents != null) {
                handleScannedNumber(result.contents)
            } else {
                Toast.makeText(this, "스캔이 취소되었습니다.", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleScannedNumber(trackingNumber: String) {
        val existing = db.findByTrackingNumber(trackingNumber)

        if (existing != null) {
            Toast.makeText(this, "택배 정보를 찾았습니다.", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("PARCEL_ID", existing.id)
            startActivity(intent)
            finish()
        } else {
            showQuickRegisterDialog(trackingNumber)
        }
    }

    private fun showQuickRegisterDialog(trackingNumber: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_register, null)

        val etReceiver = dialogView.findViewById<EditText>(R.id.etReceiver)
        val etRegion = dialogView.findViewById<EditText>(R.id.etRegion)

        AlertDialog.Builder(this)
            .setTitle("새 택배 등록")
            .setMessage("송장번호: $trackingNumber")
            .setView(dialogView)
            .setPositiveButton("등록") { _, _ ->
                val receiver = etReceiver.text.toString().trim()
                val region = etRegion.text.toString().trim()

                if (receiver.isEmpty() || region.isEmpty()) {
                    Toast.makeText(this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val zone = Parcel.classifyZone(region)

                val currentDate = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(Date())

                val parcel = Parcel(
                    trackingNumber = trackingNumber,
                    receiver = receiver,
                    region = region,
                    zone = zone,
                    status = "접수 완료",
                    createdAt = currentDate
                )

                db.insertParcel(parcel)

                Toast.makeText(this, "택배가 자동 등록되었습니다.", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("취소", null)
            .show()
    }
}