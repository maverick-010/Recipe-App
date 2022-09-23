package com.maverick.recipeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maverick.recipeapp.databinding.PopularItemsBinding
import com.maverick.recipeapp.pojo.MealsByCategory

class MostPopularAdapter(): RecyclerView.Adapter<MostPopularAdapter.PopularViewHolder>() {
    lateinit var onItemClick:((MealsByCategory)->Unit)
    var onLongItemClick:((MealsByCategory)->Unit)?=null

    private var mealList = ArrayList<MealsByCategory>()

    fun setMeals(mealList:ArrayList<MealsByCategory>){
        this.mealList=mealList
        notifyDataSetChanged()
    }

    class PopularViewHolder(val binding:PopularItemsBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        Glide.with(holder.itemView).load(mealList[position].strMealThumb).into(holder.binding.popularMealItem)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealList[position])
        }

        holder.itemView.setOnLongClickListener {
            onLongItemClick?.invoke(mealList[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }
}