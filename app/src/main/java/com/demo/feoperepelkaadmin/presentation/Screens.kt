package com.demo.feoperepelkaadmin.presentation

import com.demo.feoperepelkaadmin.presentation.fragments.categoriesList.CategoriesListFragment
import com.demo.feoperepelkaadmin.presentation.fragments.notesList.NotesListFragment
import com.demo.feoperepelkaadmin.presentation.fragments.ordersList.OrdersListFragment
import com.demo.feoperepelkaadmin.server.models.CategoryModel
import com.demo.feoperepelkaadmin.server.models.OrderModel
import com.demo.feoperepelkaadmin.server.models.ProductModel
import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {

    /**
     * Main menu navigation screens
     */
    fun CategoriesList() = FragmentScreen { CategoriesListFragment() }
    fun NotesList() = FragmentScreen { NotesListFragment() }
    fun OrdersList() = FragmentScreen { OrdersListFragment() }


    /**
     * Other screens
     */
    fun MainActivityScreen() = ActivityScreen { MainActivity.newIntent(it) }
    fun LoginActivity() = ActivityScreen { SecondaryActivity.newLoginIntent(it) }

    fun NoteDetailActivity() = ActivityScreen { SecondaryActivity.newNoteDetailIntent(it) }
    fun NoteDetailActivity(note: ProductModel) = ActivityScreen { SecondaryActivity.newNoteDetailIntent(it, note) }

    fun CategoryDetailActivity() = ActivityScreen { SecondaryActivity.newCategoryDetailIntent(it) }
    fun CategoryDetailActivity(category: CategoryModel) = ActivityScreen { SecondaryActivity.newCategoryDetailIntent(it, category) }

    fun OrderDetailActivity(order: OrderModel) = ActivityScreen { SecondaryActivity.newOrderDetailIntent(it, order) }
}
