package com.example.parceltrack

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ParcelAdapter(
    private val parcels: MutableList<Parcel>,
    private val onItemClick: (Parcel) -> Unit
) : RecyclerView.Adapter<ParcelAdapter.ParcelViewHolder>() {

    inner class ParcelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTrackingNumber: TextView = view.findViewById(R.id.tvTrackingNumber)
        val tvRegion: TextView         = view.findViewById(R.id.tvRegion)
        val tvZone: TextView           = view.findViewById(R.id.tvZone)
        val tvStatus: TextView         = view.findViewById(R.id.tvStatus)

        val tvCreatedAt: TextView      = view.findViewById(R.id.tvCreatedAt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParcelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_parcel, parent, false)
        return ParcelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParcelViewHolder, position: Int) {
        val parcel = parcels[position]
        holder.tvTrackingNumber.text = parcel.trackingNumber
        holder.tvRegion.text         = parcel.region
        holder.tvZone.text           = parcel.zone
        holder.tvStatus.text         = parcel.status
        holder.tvCreatedAt.text      = "등록일: ${parcel.createdAt}"

        // 분류 구역별 배지 색상
        val zoneColor = when (parcel.zone) {
            "A구역" -> "#4361EE"
            "B구역" -> "#3A86FF"
            "C구역" -> "#06D6A0"
            "D구역" -> "#FFB703"
            "E구역" -> "#FB5607"
            "F구역" -> "#8338EC"
            "G구역" -> "#EF233C"
            else    -> "#888888"
        }
        holder.tvZone.setBackgroundColor(Color.parseColor(zoneColor))

        // 배송 상태 텍스트 색상
        val statusColorRes = when (parcel.status) {
            "접수 완료" -> android.R.color.holo_blue_dark
            "분류중"    -> android.R.color.holo_orange_dark
            "배송중"    -> android.R.color.holo_green_dark
            "배송 완료" -> android.R.color.darker_gray
            else        -> android.R.color.black
        }
        holder.tvStatus.setTextColor(holder.itemView.context.getColor(statusColorRes))

        holder.itemView.setOnClickListener { onItemClick(parcel) }
    }

    override fun getItemCount() = parcels.size

    fun updateList(newList: List<Parcel>) {
        parcels.clear()
        parcels.addAll(newList)
        notifyDataSetChanged()
    }
}
