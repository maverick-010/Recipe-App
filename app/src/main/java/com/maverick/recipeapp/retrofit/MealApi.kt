package com.maverick.recipeapp.retrofit

import com.maverick.recipeapp.pojo.CategoryList
import com.maverick.recipeapp.pojo.MealsByCategoryList
import com.maverick.recipeapp.pojo.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("random.php")
    fun getRandomMeal():Call<MealList>

    @GET("lookup.php?")
    fun getMealDetails(@Query("i")id:String):Call<MealList>

    @GET("filter.php?")
    fun getPopulatItems(@Query("c") categoryName:String):Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategories():Call<CategoryList>
    @GET("filter.php")
    fun getMealByCategory(@Query("c")categoryName: String):Call<MealsByCategoryList>
}