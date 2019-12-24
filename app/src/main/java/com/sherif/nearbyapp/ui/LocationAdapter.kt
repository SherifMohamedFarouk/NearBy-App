package com.example.kotlinkoinmvp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sherif.nearbyapp.MyApplication
import com.sherif.nearbyapp.R
import com.sherif.nearbyapp.model.locations.Item


class LocationAdapter ( var dataList : List<Item>):
    RecyclerView.Adapter<LocationAdapter.ViewHolder>(){

    override fun getItemCount(): Int {
        return dataList.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(MyApplication.appContext).inflate(R.layout.location_item, parent, false))
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
      holder.nameview.text = dataList[position].venue.name
        holder.addressview.text = dataList[position].venue.location.formattedAddress[0]


    }

    fun updatelist(list:List<Item>){
        dataList = list
        notifyDataSetChanged()

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameview: TextView =itemView.findViewById(R.id.locationname)
        var addressview: TextView =itemView.findViewById(R.id.locationaddress)
    }
}