package com.example.parceltrack

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random

data class Parcel(
    val id: Int = 0,
    val trackingNumber: String,
    val receiver: String,
    val region: String,
    val zone: String,
    val status: String,
    val createdAt: String,
    val autoMode: Int = 1,
    val sortingAt: String = "",
    val shippingAt: String = "",
    val completeAt: String = ""
) {
    companion object {
        val STATUS_LIST = listOf("접수 완료", "분류중", "배송중", "배송 완료")

        private val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        private val displayDf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        private val random = Random()

        // 각 단계 지속 시간 범위 (분)
        const val SORTING_DELAY_MIN = 25L     // 접수 완료 → 분류중: 25~45분
        const val SORTING_DELAY_MAX = 45L
        const val SHIPPING_DELAY_MIN = 90L    // 분류중 → 배송중: 1.5hr ~ 약3hr
        const val SHIPPING_DELAY_MAX = 170L
        const val COMPLETE_DELAY_MIN = 1080L  // 배송중 → 배송 완료: 18hr ~ 28hr
        const val COMPLETE_DELAY_MAX = 1680L

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

        fun nowDateTime(): String = df.format(Date())

        fun formatDisplay(dateStr: String): String {
            val d = parseDate(dateStr) ?: return dateStr
            return displayDf.format(d)
        }

        fun randomMinutes(min: Long, max: Long): Long {
            return min + (random.nextDouble() * (max - min + 1)).toLong()
        }

        fun addMinutes(dateStr: String, minutes: Long): String {
            val date = parseDate(dateStr) ?: return dateStr
            return df.format(Date(date.time + minutes * 60 * 1000))
        }

        // 접수 시점 기준으로 랜덤 전환 시각 3개 생성
        fun generateTransitionTimes(createdAt: String): Triple<String, String, String> {
            val sortingAt = addMinutes(createdAt, randomMinutes(SORTING_DELAY_MIN, SORTING_DELAY_MAX))
            val shippingAt = addMinutes(sortingAt, randomMinutes(SHIPPING_DELAY_MIN, SHIPPING_DELAY_MAX))
            val completeAt = addMinutes(shippingAt, randomMinutes(COMPLETE_DELAY_MIN, COMPLETE_DELAY_MAX))
            return Triple(sortingAt, shippingAt, completeAt)
        }

        // 수동 상태 변경 시: 해당 시점부터 이후 전환 시각 재생성
        fun regenerateFromStatus(newStatus: String, changedAt: String,
                                 oldSortingAt: String, oldShippingAt: String): Triple<String, String, String> {
            return when (newStatus) {
                "분류중" -> {
                    val shippingAt = addMinutes(changedAt, randomMinutes(SHIPPING_DELAY_MIN, SHIPPING_DELAY_MAX))
                    val completeAt = addMinutes(shippingAt, randomMinutes(COMPLETE_DELAY_MIN, COMPLETE_DELAY_MAX))
                    Triple(changedAt, shippingAt, completeAt)
                }
                "배송중" -> {
                    val completeAt = addMinutes(changedAt, randomMinutes(COMPLETE_DELAY_MIN, COMPLETE_DELAY_MAX))
                    Triple(oldSortingAt, changedAt, completeAt)
                }
                "배송 완료" -> {
                    Triple(oldSortingAt, oldShippingAt, changedAt)
                }
                else -> Triple(oldSortingAt, oldShippingAt, "")
            }
        }

        // 저장된 전환 시각 기반으로 현재 있어야 할 상태 계산
        fun expectedStatusFromTimes(sortingAt: String, shippingAt: String, completeAt: String): String {
            val now = System.currentTimeMillis()
            val sortingMs = parseDate(sortingAt)?.time ?: Long.MAX_VALUE
            val shippingMs = parseDate(shippingAt)?.time ?: Long.MAX_VALUE
            val completeMs = parseDate(completeAt)?.time ?: Long.MAX_VALUE

            return when {
                now >= completeMs -> "배송 완료"
                now >= shippingMs -> "배송중"
                now >= sortingMs -> "분류중"
                else -> "접수 완료"
            }
        }

        fun parseDate(dateStr: String): Date? {
            if (dateStr.isEmpty()) return null
            return try {
                df.parse(dateStr)
            } catch (e: Exception) {
                try {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr)
                } catch (e2: Exception) {
                    null
                }
            }
        }
    }
}
