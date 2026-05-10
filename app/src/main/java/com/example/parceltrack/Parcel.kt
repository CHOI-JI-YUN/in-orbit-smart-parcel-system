package com.example.parceltrack

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Parcel(
    val id: Int = 0,
    val trackingNumber: String,
    val receiver: String,
    val region: String,
    val zone: String,        // 자동 분류 구역
    val status: String,
    val createdAt: String,
    val autoMode: Int = 1    // 1 = 자동 진행 ON, 0 = 수동 모드 (사용자가 직접 변경 시)
) {
    companion object {
        // 배송 상태 목록
        val STATUS_LIST = listOf(
            "접수 완료",
            "분류중",
            "배송중",
            "배송 완료"
        )

        // 자동 진행 시간 기준 (분 단위)
        const val MINUTES_TO_SORTING = 30L      // 30분 후 → 분류중
        const val MINUTES_TO_SHIPPING = 120L    // 2시간 후 → 배송중
        const val MINUTES_TO_COMPLETE = 1440L   // 24시간 후 → 배송 완료

        // 지역 → 분류 구역 자동 매핑
        fun classifyZone(region: String): String {
            return when {
                region.contains("서울") -> "A구역"
                region.contains("경기") || region.contains("인천") -> "B구역"
                region.contains("대전") || region.contains("충청") || region.contains("충남") || region.contains("충북") -> "C구역"
                region.contains("부산") || region.contains("울산") || region.contains("경남") || region.contains("경북") || region.contains("대구") -> "D구역"
                region.contains("광주") || region.contains("전남") || region.contains("전북") -> "E구역"
                region.contains("강원") -> "F구역"
                region.contains("제주") -> "G구역"
                else -> "기타"
            }
        }

        // 등록 시 사용할 날짜+시간 형식
        fun nowDateTime(): String {
            return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        }

        // 등록 시간으로부터 경과한 분 수 계산
        fun minutesSince(createdAt: String): Long {
            return try {
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val createdDate = format.parse(createdAt) ?: return 0
                val nowMs = System.currentTimeMillis()
                val createdMs = createdDate.time
                (nowMs - createdMs) / (60 * 1000)
            } catch (e: Exception) {
                // 옛날 형식 (yyyy-MM-dd)도 호환되게 시도
                try {
                    val format2 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val createdDate = format2.parse(createdAt) ?: return 0
                    val nowMs = System.currentTimeMillis()
                    val createdMs = createdDate.time
                    (nowMs - createdMs) / (60 * 1000)
                } catch (e2: Exception) {
                    0
                }
            }
        }

        // 경과 시간 기반으로 "있어야 할" 상태 계산
        fun expectedStatus(createdAt: String): String {
            val minutes = minutesSince(createdAt)
            return when {
                minutes >= MINUTES_TO_COMPLETE -> "배송 완료"
                minutes >= MINUTES_TO_SHIPPING -> "배송중"
                minutes >= MINUTES_TO_SORTING -> "분류중"
                else -> "접수 완료"
            }
        }
    }
}