package com.demo.feoperepelkaadmin.presentation

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.ActivitySecondaryBinding
import com.demo.feoperepelkaadmin.presentation.fragments.categoryDetail.CategoryDetailFragment
import com.demo.feoperepelkaadmin.presentation.fragments.login.LoginFragment
import com.demo.feoperepelkaadmin.presentation.fragments.noteDetail.NoteDetailFragment
import com.demo.feoperepelkaadmin.presentation.fragments.orderDetail.OrderDetailFragment
import com.demo.feoperepelkaadmin.server.models.CategoryModel
import com.demo.feoperepelkaadmin.server.models.OrderModel
import com.demo.feoperepelkaadmin.server.models.ProductModel
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.parse.ParseObject
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SecondaryActivity : AppCompatActivity() {

    private lateinit var launchMode: String
    private lateinit var extraCategory: CategoryModel
    private lateinit var extraNote: ProductModel
    private lateinit var extraOrder: OrderModel

    private lateinit var binding: ActivitySecondaryBinding

    @Inject
    lateinit var navHolder: NavigatorHolder
    private val navigator = AppNavigator(this, R.id.secondary_fragment_container)

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySecondaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receiveIntent()

        if (savedInstanceState == null) {
            when (launchMode) {
                EXTRA_LOGIN -> router.replaceScreen(InnerScreens.Login())
                EXTRA_NOTE_EDIT -> router.replaceScreen(InnerScreens.NoteDetail(extraNote))
                EXTRA_CATEGORY_EDIT -> router.replaceScreen(
                    InnerScreens.CategoryDetail(
                        extraCategory
                    )
                )
                EXTRA_ORDER_EDIT -> router.replaceScreen(InnerScreens.OrderDetail(extraOrder))
                EXTRA_NOTE -> router.replaceScreen(InnerScreens.NoteDetail())
                EXTRA_CATEGORY -> router.replaceScreen(InnerScreens.CategoryDetail())
                else -> throw Exception("Unknown KEY_FRAGMENT for SecondaryActivity: $launchMode")
            }
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navHolder.setNavigator(navigator)
    }


    override fun onPause() {
        navHolder.removeNavigator()
        super.onPause()
    }

    override fun onSaveInstanceState(oldInstanceState: Bundle) {
        super.onSaveInstanceState(oldInstanceState)
        oldInstanceState.clear()
    }

    private fun receiveIntent() {
        if (!intent.hasExtra(KEY_FRAGMENT))
            throw Exception("SecondaryActivity requires KEY_FRAGMENT as parameter with intent")

        val keyType = intent.getStringExtra(KEY_FRAGMENT)
        if (keyType != EXTRA_LOGIN
            && keyType != EXTRA_CATEGORY
            && keyType != EXTRA_NOTE
            && keyType != EXTRA_CATEGORY_EDIT
            && keyType != EXTRA_NOTE_EDIT
            && keyType != EXTRA_ORDER_EDIT
        )
            throw Exception("Unknown value for KEY_FRAGMENT")

        launchMode = keyType
        if (keyType == EXTRA_CATEGORY_EDIT || keyType == EXTRA_ORDER_EDIT) {
            if (!intent.hasExtra(KEY_ITEM))
                throw Exception("SecondaryActivity requires value for KEY_ITEM for $keyType")
            else
                when (keyType) {
                    EXTRA_CATEGORY_EDIT -> extraCategory =
                        intent.getParcelableExtra<CategoryModel>(KEY_ITEM)
                            ?: throw Exception("Empty EXTRA_ITEM key")
                    EXTRA_ORDER_EDIT -> extraOrder = intent.getParcelableExtra<OrderModel>(KEY_ITEM)
                        ?: throw Exception("Empty EXTRA_ITEM key")
                }
        } else if (keyType == EXTRA_NOTE_EDIT) {
            val po = intent.getParcelableExtra<ParseObject>(ProductModel.PARSE_OBJ_KEY)
            if (po != null) {
                extraNote = ProductModel(
                    intent.getStringExtra(ProductModel.TITLE_KEY) ?: "",
                    intent.getStringExtra(ProductModel.CATEGORY_KEY) ?: "",
                    intent.getBooleanExtra(ProductModel.ENABLED_KEY, true),
                    intent.getStringExtra(ProductModel.DESCRIPTION_KEY) ?: "",
                    intent.getDoubleExtra(ProductModel.WEIGHT_KEY, 0.0),
                    intent.getDoubleExtra(ProductModel.PRICE_KEY, 0.0),
                    intent.getStringExtra(ProductModel.IMG_TITLE_KEY) ?: "",
                    ProductModel.decodeImgFromByteArray(
                        po.getParseFile(ProductModel.IMG_KEY)!!.data
                    ),
                    po
                )
            } else throw Exception("ParseObject must be implemented for DetailNoteFragment")

        }

    }

    companion object {
        private const val KEY_FRAGMENT = "key_fragment"
        private const val KEY_ITEM = "key_item"

        private const val EXTRA_LOGIN = "login_fragment"
        private const val EXTRA_NOTE = "note_fragment"
        private const val EXTRA_NOTE_EDIT = "note_fragment_edit"
        private const val EXTRA_ORDER_EDIT = "order_fragment_edit"
        private const val EXTRA_CATEGORY = "category_fragment"
        private const val EXTRA_CATEGORY_EDIT = "category_fragment_edit"

        fun newLoginIntent(context: Context): Intent {
            return Intent(context, SecondaryActivity::class.java).apply {
                putExtra(KEY_FRAGMENT, EXTRA_LOGIN)
            }
        }

        fun newNoteDetailIntent(context: Context): Intent {
            return Intent(context, SecondaryActivity::class.java).apply {
                putExtra(KEY_FRAGMENT, EXTRA_NOTE)
            }
        }

        fun newNoteDetailIntent(context: Context, note: ProductModel): Intent {
            return Intent(context, SecondaryActivity::class.java).apply {
                putExtra(KEY_FRAGMENT, EXTRA_NOTE_EDIT)
                putExtra(ProductModel.TITLE_KEY, note.title)
                putExtra(ProductModel.CATEGORY_KEY, note.category)
                putExtra(ProductModel.ENABLED_KEY, note.enabled)
                putExtra(ProductModel.DESCRIPTION_KEY, note.description)
                putExtra(ProductModel.WEIGHT_KEY, note.weight)
                putExtra(ProductModel.PRICE_KEY, note.price)
                putExtra(ProductModel.IMG_TITLE_KEY, note.imgTitle)
                putExtra(ProductModel.PARSE_OBJ_KEY, note.parseObject)
            }
        }

        fun newOrderDetailIntent(context: Context, order: OrderModel): Intent {
            return Intent(context, SecondaryActivity::class.java).apply {
                putExtra(KEY_FRAGMENT, EXTRA_ORDER_EDIT)
                putExtra(KEY_ITEM, order)
            }
        }

        fun newCategoryDetailIntent(context: Context): Intent {
            return Intent(context, SecondaryActivity::class.java).apply {
                putExtra(KEY_FRAGMENT, EXTRA_CATEGORY)
            }
        }

        fun newCategoryDetailIntent(context: Context, category: CategoryModel): Intent {
            return Intent(context, SecondaryActivity::class.java).apply {
                putExtra(KEY_FRAGMENT, EXTRA_CATEGORY_EDIT)
                putExtra(KEY_ITEM, category)
            }
        }
    }
}

object InnerScreens {
    fun Login() = FragmentScreen { LoginFragment() }

    fun NoteDetail() = FragmentScreen { NoteDetailFragment.newInstance() }
    fun NoteDetail(note: ProductModel) = FragmentScreen { NoteDetailFragment.newInstance(note) }

    fun CategoryDetail() = FragmentScreen { CategoryDetailFragment.newInstance() }
    fun CategoryDetail(category: CategoryModel) =
        FragmentScreen { CategoryDetailFragment.newInstance(category) }

    fun OrderDetail(order: OrderModel) = FragmentScreen { OrderDetailFragment.newInstance(order) }
}
