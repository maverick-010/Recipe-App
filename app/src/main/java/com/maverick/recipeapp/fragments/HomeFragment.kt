package com.maverick.recipeapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.maverick.recipeapp.R
import com.maverick.recipeapp.activities.CategoryMealsActivity
import com.maverick.recipeapp.activities.MainActivity
import com.maverick.recipeapp.activities.MealActivity
import com.maverick.recipeapp.adapters.CategoriesAdapter
import com.maverick.recipeapp.adapters.MostPopularAdapter
import com.maverick.recipeapp.databinding.FragmentHomeBinding
import com.maverick.recipeapp.fragments.bottomsheet.MealBottomSheetFragment
import com.maverick.recipeapp.pojo.MealsByCategory
import com.maverick.recipeapp.pojo.Meal
import com.maverick.recipeapp.viewModels.HomeViewModel


class HomeFragment : Fragment() {

    private lateinit var binding:FragmentHomeBinding
    private lateinit var viewModel:HomeViewModel
    private lateinit var randomMeal:Meal
    private lateinit var popularItemAdapter:MostPopularAdapter
    private lateinit var categoriesAdapter:CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.maverick.recipeapp.fragments.idMeal"
        const val MEAL_NAME = "com.maverick.recipeapp.fragments.nameMeal"
        const val MEAL_THUMB = "com.maverick.recipeapp.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.maverick.recipeapp.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        popularItemAdapter = MostPopularAdapter()
//        homeMvvm = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecyclerView()


        observeRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observePopularItemLiveData()

        onPopularItemClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryClick()

        onPopularItemLongClick()

    
    }



    private fun onPopularItemLongClick() {
        popularItemAdapter.onLongItemClick={meal->
            val mealBottomSheetFragment=MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"Meal Info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick={category ->  
            val intent =Intent(activity,CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter=categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories->

                categoriesAdapter.setCategories(categories)


        })
    }

    private fun onPopularItemClick() {
        popularItemAdapter.onItemClick={
            meal->
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            adapter = popularItemAdapter
        }
    }

    private fun observePopularItemLiveData() {
        viewModel.observePopularItemLiveData().observe(viewLifecycleOwner
        ) { mealList->
            popularItemAdapter.setMeals(mealList = mealList as ArrayList<MealsByCategory>)
        }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID,randomMeal.idMeal)
            intent.putExtra(MEAL_NAME,randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner, { meal ->
            Glide.with(this@HomeFragment).load(meal!!.strMealThumb).into(binding.imgRandomMeal)
            this.randomMeal=meal
        })
    }

}