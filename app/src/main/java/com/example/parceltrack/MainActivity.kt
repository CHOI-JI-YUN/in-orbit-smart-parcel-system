package com.example.parceltrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ⭐ 앱 시작 시 자동으로 더미 데이터 삽입 (중복은 건너뜀)
        try {
            val db = DatabaseHelper(this)
            db.seedDummyData()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 택배 등록 버튼
        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // 바코드 스캔 버튼
        findViewById<Button>(R.id.btnScan).setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }

        // 택배 목록 버튼
        findViewById<Button>(R.id.btnList).setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
        }

        // ⭐ 테스트 데이터 채우기 버튼
        findViewById<Button>(R.id.btnSeedDummy).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("테스트 데이터 채우기")
                .setMessage("5/7(53개) + 5/8(38개) + 5/27(51개) + 5/28(47개) + 5/29(51개) + 5/30(17개) = 총 257개의 더미 택배를 추가합니다.\n\n기존 데이터는 유지되고, 송장번호가 중복된 것만 건너뜁니다.")
                .setPositiveButton("추가") { _, _ ->
                    val db = DatabaseHelper(this)
                    val count = db.seedDummyData()
                    Toast.makeText(this, "${count}개의 테스트 데이터가 추가되었습니다", Toast.LENGTH_LONG).show()
                }
                .setNegativeButton("취소", null)
                .show()
        }
    }
}