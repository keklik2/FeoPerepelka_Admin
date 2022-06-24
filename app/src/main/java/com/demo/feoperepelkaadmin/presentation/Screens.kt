package com.demo.feoperepelkaadmin.presentation

import com.demo.feoperepelkaadmin.presentation.fragments.categoriesList.CategoriesListFragment
import com.demo.feoperepelkaadmin.presentation.fragments.login.LoginFragment
import com.demo.feoperepelkaadmin.presentation.fragments.noInternet.NoInternetFragment
import com.demo.feoperepelkaadmin.presentation.fragments.noteDetail.NoteDetailFragment
import com.demo.feoperepelkaadmin.presentation.fragments.notesList.NotesListFragment
import com.demo.feoperepelkaadmin.presentation.fragments.orderDetail.OrderDetailFragment
import com.demo.feoperepelkaadmin.presentation.fragments.ordersList.OrdersListFragment
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
    fun Login() = FragmentScreen { LoginFragment() }
    fun NoInternet() = FragmentScreen { NoInternetFragment() }

    fun NoteDatail() = FragmentScreen { NoteDetailFragment.newInstance() }
    fun NoteDatail(note: ProductModel) = FragmentScreen { NoteDetailFragment.newInstance(note) }
    fun OrderDatail(order: OrderModel) = FragmentScreen { OrderDetailFragment.newInstance(order) }
}
