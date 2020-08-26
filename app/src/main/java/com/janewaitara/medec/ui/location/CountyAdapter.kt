package com.janewaitara.medec.ui.location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.janewaitara.medec.R
import com.janewaitara.medec.common.extensions.onClick
import com.janewaitara.medec.model.County

class CountyAdapter(private val countyClickListener: CountyClickListener): RecyclerView.Adapter<CountyAdapter.CountyListViewHolder> (){

    /**Cached copy of counties*/
    private var countyList = emptyList<County>()

    interface CountyClickListener{
        fun countyItemClicked(countyName: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountyListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.location_counties,parent,false)
        return CountyListViewHolder(view)
    }

    override fun getItemCount(): Int  = countyList.size

    override fun onBindViewHolder(holder: CountyListViewHolder, position: Int) {
        holder.countyName.text = countyList[position].name
        holder.itemView.onClick {
            countyClickListener.countyItemClicked(countyList[position].name)
        }
    }

    internal fun setCounties(counties: List<County>){
        this.countyList = counties
        notifyDataSetChanged()
    }


    class CountyListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var countyName = itemView.findViewById(R.id.county_name) as TextView

    }
}