package com.example.parceltrack

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
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
            "접수 완료" -> statusView.setBackgroundColor(Color.parseColor("#4361EE"))
            "분류중" -> statusView.setBackgroundColor(Color.parseColor("#FF9F1C"))
            "배송중" -> statusView.setBackgroundColor(Color.parseColor("#06D6A0"))
            "배송 완료" -> statusView.setBackgroundColor(Color.parseColor("#6C757D"))
        }
        findViewById<TextView>(R.id.tvDetailCreatedAt).text = parcel.createdAt

        findViewById<ImageView>(R.id.ivBarcode).setImageBitmap(
            generateBarcode(parcel.trackingNumber)
        )

        // 배송 추적 타임라인 표시
        buildTimeline(parcel)
    }

    private fun buildTimeline(parcel: Parcel) {
        val container = findViewById<LinearLayout>(R.id.timelineContainer)
        container.removeAllViews()

        val now = System.currentTimeMillis()
        val statusIndex = Parcel.STATUS_LIST.indexOf(parcel.status)

        // 각 단계별 시각과 정보
        data class TimelineStep(
            val label: String,
            val time: String,
            val color: String,
            val isPassed: Boolean
        )

        val steps = mutableListOf<TimelineStep>()

        // 1. 접수 완료 - 항상 있음
        steps.add(TimelineStep(
            "접수 완료",
            Parcel.formatDisplay(parcel.createdAt),
            "#4361EE",
            statusIndex >= 0
        ))

        // 2. 분류중
        val sortingPassed = statusIndex >= 1
        val sortingTime = if (parcel.sortingAt.isNotEmpty()) {
            Parcel.formatDisplay(parcel.sortingAt)
        } else ""
        steps.add(TimelineStep(
            "분류중",
            if (sortingPassed) sortingTime else if (sortingTime.isNotEmpty()) "$sortingTime (예정)" else "",
            "#FF9F1C",
            sortingPassed
        ))

        // 3. 배송중
        val shippingPassed = statusIndex >= 2
        val shippingTime = if (parcel.shippingAt.isNotEmpty()) {
            Parcel.formatDisplay(parcel.shippingAt)
        } else ""
        steps.add(TimelineStep(
            "배송중",
            if (shippingPassed) shippingTime else if (shippingTime.isNotEmpty()) "$shippingTime (예정)" else "",
            "#06D6A0",
            shippingPassed
        ))

        // 4. 배송 완료
        val completePassed = statusIndex >= 3
        val completeTime = if (parcel.completeAt.isNotEmpty()) {
            Parcel.formatDisplay(parcel.completeAt)
        } else ""
        steps.add(TimelineStep(
            "배송 완료",
            if (completePassed) completeTime else if (completeTime.isNotEmpty()) "$completeTime (예정)" else "",
            "#6C757D",
            completePassed
        ))

        for (i in steps.indices) {
            val step = steps[i]
            val isLast = i == steps.size - 1

            // 한 줄: ● 상태명     시각
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(0, dpToPx(2), 0, dpToPx(2))
            }

            // 원형 아이콘
            val dot = TextView(this).apply {
                text = if (step.isPassed) "●" else "○"
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                setTextColor(if (step.isPassed) Color.parseColor(step.color) else Color.parseColor("#CCCCCC"))
                setPadding(0, 0, dpToPx(10), 0)
            }
            row.addView(dot)

            // 상태명
            val label = TextView(this).apply {
                text = step.label
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                setTextColor(if (step.isPassed) Color.parseColor("#1A1A2E") else Color.parseColor("#BBBBBB"))
                if (step.isPassed) setTypeface(null, Typeface.BOLD)
                layoutParams = LinearLayout.LayoutParams(dpToPx(80), LinearLayout.LayoutParams.WRAP_CONTENT)
            }
            row.addView(label)

            // 시각
            val time = TextView(this).apply {
                text = step.time
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                setTextColor(if (step.isPassed) Color.parseColor("#555555") else Color.parseColor("#BBBBBB"))
            }
            row.addView(time)

            container.addView(row)

            // 연결선 (마지막 아이템 제외)
            if (!isLast) {
                val line = TextView(this).apply {
                    text = "│"
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                    setTextColor(
                        if (steps[i + 1].isPassed) Color.parseColor(steps[i + 1].color)
                        else Color.parseColor("#DDDDDD")
                    )
                    setPadding(dpToPx(1), 0, 0, 0)
                }
                container.addView(line)
            }
        }
    }

    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
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

                // 수동 변경: 현재 시각 기록 + 이후 전환 시각 재생성
                db.updateStatusManual(parcelId, nextStatus)

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
