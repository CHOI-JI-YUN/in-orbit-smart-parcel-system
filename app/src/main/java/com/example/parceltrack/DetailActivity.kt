package com.example.parceltrack

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

class DetailActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private var parcelId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        db = DatabaseHelper(this)
        parcelId = intent.getIntExtra("PARCEL_ID", -1)

        if (parcelId == -1) {
            Toast.makeText(this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        findViewById<Button>(R.id.btnChangeStatus).setOnClickListener { showStatusDialog() }
        findViewById<Button>(R.id.btnEdit).setOnClickListener { showEditDialog() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { showDeleteDialog() }
        findViewById<TextView>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadParcel()
    }

    private fun loadParcel() {
        val parcel = db.getParcelById(parcelId) ?: return

        findViewById<TextView>(R.id.tvDetailTrackingNumber).text = parcel.trackingNumber
        findViewById<TextView>(R.id.tvDetailReceiver).text = parcel.receiver
        findViewById<TextView>(R.id.tvDetailRegion).text = parcel.region
        findViewById<TextView>(R.id.tvDetailZone).text = parcel.zone
        findViewById<TextView>(R.id.tvDetailStatus).text = parcel.status
        val statusView = findViewById<TextView>(R.id.tvDetailStatus)

        when (parcel.status) {

            "접수 완료" -> {
                statusView.setBackgroundColor(
                    Color.parseColor("#4361EE")
                )
            }

            "분류중" -> {
                statusView.setBackgroundColor(
                    Color.parseColor("#FF9F1C")
                )
            }

            "배송중" -> {
                statusView.setBackgroundColor(
                    Color.parseColor("#06D6A0")
                )
            }

            "배송 완료" -> {
                statusView.setBackgroundColor(
                    Color.parseColor("#6C757D")
                )
            }
        }
        findViewById<TextView>(R.id.tvDetailCreatedAt).text = parcel.createdAt

        findViewById<ImageView>(R.id.ivBarcode).setImageBitmap(
            generateBarcode(parcel.trackingNumber)
        )
    }

    private fun generateBarcode(text: String): Bitmap {
        val width = 800
        val height = 250

        val bitMatrix = MultiFormatWriter().encode(
            text,
            BarcodeFormat.CODE_128,
            width,
            height
        )

        val pixels = IntArray(width * height)

        for (y in 0 until height) {
            for (x in 0 until width) {
                pixels[y * width + x] =
                    if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
            }
        }

        return Bitmap.createBitmap(
            pixels,
            width,
            height,
            Bitmap.Config.RGB_565
        )
    }

    private fun showStatusDialog() {

        val parcel = db.getParcelById(parcelId) ?: return

        if (parcel.status == "배송 완료") {
            Toast.makeText(
                this,
                "이미 배송이 완료된 택배입니다.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val nextStatus = when (parcel.status) {
            "접수 완료" -> "분류중"
            "분류중" -> "배송중"
            "배송중" -> "배송 완료"
            else -> "배송 완료"
        }

        AlertDialog.Builder(this)
            .setTitle("배송 상태 변경")
            .setMessage(
                "현재 상태: ${parcel.status}\n\n" +
                        "'$nextStatus'(으)로 변경하시겠습니까?"
            )
            .setPositiveButton("변경") { _, _ ->

                db.updateStatus(parcelId, nextStatus)

                loadParcel()

                Toast.makeText(
                    this,
                    "배송 상태가 '$nextStatus'(으)로 변경되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun showEditDialog() {
        val parcel = db.getParcelById(parcelId) ?: return

        val view = layoutInflater.inflate(R.layout.dialog_edit, null)
        val etTracking = view.findViewById<EditText>(R.id.etEditTracking)
        val etReceiver = view.findViewById<EditText>(R.id.etEditReceiver)
        val etRegion = view.findViewById<EditText>(R.id.etEditRegion)

        etTracking.setText(parcel.trackingNumber)
        etReceiver.setText(parcel.receiver)
        etRegion.setText(parcel.region)

        AlertDialog.Builder(this)
            .setTitle("택배 정보 수정")
            .setView(view)
            .setPositiveButton("저장") { _, _ ->
                val newTracking = etTracking.text.toString().trim()
                val newReceiver = etReceiver.text.toString().trim()
                val newRegion = etRegion.text.toString().trim()

                if (newTracking.isEmpty() || newReceiver.isEmpty() || newRegion.isEmpty()) {
                    Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val updated = parcel.copy(
                    trackingNumber = newTracking,
                    receiver = newReceiver,
                    region = newRegion,
                    zone = Parcel.classifyZone(newRegion)
                )
                db.updateParcel(updated)
                loadParcel()
                Toast.makeText(this, "수정 완료!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(this)
            .setTitle("택배 삭제")
            .setMessage("이 택배를 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                db.deleteParcel(parcelId)
                Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("취소", null)
            .show()
    }
}