package com.maverick.recipeapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.maverick.recipeapp.R
import com.maverick.recipeapp.databinding.ActivityMealBinding
import com.maverick.recipeapp.db.MealDatabase
import com.maverick.recipeapp.fragments.HomeFragment
import com.maverick.recipeapp.pojo.Meal
import com.maverick.recipeapp.viewModels.MealViewModel
import com.maverick.recipeapp.viewModels.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var mealId: String
    private lateinit var meanName: String
    private lateinit var mealThumb: String
    lateinit var binding: ActivityMealBinding
    private lateinit var mealMvvm:MealViewModel
    private lateinit var youtubeLink:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase=MealDatabase.getInstance(this)
        val viewModelFactory=MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this,viewModelFactory)[MealViewModel::class.java]


        getMealInformationFromIntent()
        setInformationInViews()

        loadingCase()
        mealMvvm.getMealDetail(mealId)
        observeMealDetailLiveData()
        onYoutubeImageClick()

        onFavoritesClick()
    }

    private fun onFavoritesClick() {
        binding.btnAddToFav.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this,"Meal saved",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave:Meal?=null
    private fun observeMealDetailLiveData() {
        mealMvvm.observeMealDetailLiveData().observe(this,object :Observer<Meal>{
            override fun onChanged(t: Meal?) {
                onResponseCase()
                val meal = t

                mealToSave=meal

                binding.tvCategoryInfo.text="Category : ${meal!!.strCategory}"
                binding.tvAreaInfo.text="Area : ${meal.strArea}"
                binding.tvInstructions.text=meal.strInstructions

                youtubeLink= meal.strYoutube.toString()
            }
        })
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext).load(mealThumb).into(binding.imgMealDetail)

        binding.collapsingToolbar.title = meanName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        meanName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }
    private fun loadingCase(){
            binding.progressBar.visibility=View.VISIBLE
            binding.btnAddToFav.visibility=View.INVISIBLE
            binding.tvInstructions.visibility=View.INVISIBLE
            binding.tvCategoryInfo.visibility=View.INVISIBLE
            binding.tvAreaInfo.visibility=View.INVISIBLE
            binding.imgYoutube.visibility=View.INVISIBLE
    }
    private fun onResponseCase(){
        binding.progressBar.visibility=View.INVISIBLE
        binding.btnAddToFav.visibility=View.VISIBLE
        binding.tvInstructions.visibility=View.VISIBLE
        binding.tvCategoryInfo.visibility=View.VISIBLE
        binding.tvAreaInfo.visibility=View.VISIBLE
        binding.imgYoutube.visibility=View.VISIBLE
    }
}