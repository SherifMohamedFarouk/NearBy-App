package com.example.kotlinkoinmvp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sherif.nearbyapp.MyApplication
import com.sherif.nearbyapp.MyApplication.Companion.appContext
import com.sherif.nearbyapp.R
import com.sherif.nearbyapp.model.locations.Venue


class LocationAdapter ( var dataList : ArrayList<Venue>):
    RecyclerView.Adapter<LocationAdapter.ViewHolder>(){

    override fun getItemCount(): Int {
        return dataList.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(appContext).inflate(R.layout.location_item, parent, false))
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        holder.tvName.text = dataList[position].name
        holder.tvAddress.text = dataList[position].location!!.formattedAddress[0]

        Glide.with(holder.itemView.context).load(dataList[position].imageUrl)
            .placeholder(R.drawable.mapss).into(holder.ivIcon)


    }

    fun updatelist(list: List<Venue>){
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()

    }
    fun updateID(venue: Venue?) {

        for (i in 0..dataList.size-1) {
            val tempVenue = dataList.get(i)
            if (tempVenue.id == venue?.id) {
                dataList.removeAt(i)
                dataList.add(i, venue!!)
                notifyItemChanged(i)
                break
            }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tv_location_name)
        var tvAddress: TextView = itemView.findViewById(R.id.tv_location_address)
        var ivIcon: ImageView = itemView.findViewById(R.id.iv_icon)
    }
}