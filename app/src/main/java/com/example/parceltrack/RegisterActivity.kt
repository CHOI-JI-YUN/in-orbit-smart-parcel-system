package com.example.parceltrack

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class RegisterActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        db = DatabaseHelper(this)

        val etTrackingNumber = findViewById<EditText>(R.id.etTrackingNumber)
        val etReceiver       = findViewById<EditText>(R.id.etReceiver)
        val etRegion         = findViewById<EditText>(R.id.etRegion)
        val tvZone           = findViewById<TextView>(R.id.tvZone)
        val btnSave          = findViewById<Button>(R.id.btnSave)
        val btnBack          = findViewById<Button>(R.id.btnBack)

        // 바코드 스캔으로 넘어온 송장번호 자동 입력
        intent.getStringExtra("TRACKING_NUMBER")?.let {
            etTrackingNumber.setText(it)
        }

        // 지역 입력 시 자동으로 분류 구역 표시
        etRegion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val region = s?.toString() ?: ""
                if (region.isNotEmpty()) {
                    val zone = Parcel.classifyZone(region)
                    tvZone.text = "📍 분류 구역: $zone"
                } else {
                    tvZone.text = "📍 분류 구역: -"
                }
            }
        })

        // 저장
        btnSave.setOnClickListener {
            val trackingNumber = etTrackingNumber.text.toString().trim()
            val receiver = etReceiver.text.toString().trim()
            val region = etRegion.text.toString().trim()

            if (trackingNumber.isEmpty() || receiver.isEmpty() || region.isEmpty()) {
                Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val zone = Parcel.classifyZone(region)

            // 날짜 + 시간 형식으로 저장 (자동 진행 계산용)
            val currentDateTime = Parcel.nowDateTime()

            // ⭐ 무조건 "접수 완료"로 시작 + 자동 모드 ON
            val parcel = Parcel(
                trackingNumber = trackingNumber,
                receiver = receiver,
                region = region,
                zone = zone,
                status = "접수 완료",
                createdAt = currentDateTime,
                autoMode = 1
            )

            val result = db.insertParcel(parcel)

            if (result > 0) {
                Toast.makeText(this, "등록 완료! ($zone", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)

                finish()
            } else {
                Toast.makeText(this, "등록 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener { finish() }
    }
}