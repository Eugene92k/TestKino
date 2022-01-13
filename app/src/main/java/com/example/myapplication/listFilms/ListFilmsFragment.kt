package com.example.myapplication.listFilms

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.StateView
import com.example.myapplication.data.model.Film
import com.example.myapplication.data.model.FilmItem
import com.example.myapplication.data.model.Genre
import com.example.myapplication.databinding.FragmentListFilmsBinding

import org.koin.android.ext.android.inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ListFilmsFragment : Fragment(), ListFilmsContract.View {

    private val presenter: ListFilmsContract.Presenter by inject()
    private lateinit var binding: FragmentListFilmsBinding
    private val listFilmsAdapter = ListFilmsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        presenter.attachView(mvpView = this)
        binding = FragmentListFilmsBinding.inflate(layoutInflater, container, false)

        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when(listFilmsAdapter.getItemViewType(position)) {
                    FilmItem.FILM -> 1
                    else -> 2
                }
            }

        }

        // Повторная загрузка данных
        binding.retry.setOnClickListener {
            loadData()
        }

        // Обработка нажатия
        listFilmsAdapter.attachDelegate(object: OnClickItem {
            // Фильтр фильмов
            override fun filter(genre: Genre) {
                Log.d("sdfd", "filter: ${genre.title}")
                if (genre.activate) {
                    genre.activate = false
                    listFilmsAdapter.clear()
                    return
                }
                val tempList = arrayListOf<FilmItem>()
                listFilmsAdapter.allListD.forEach { item ->
                    when (item) {
                        is Genre -> {
                            item.activate = item == genre
                            tempList.add(item)
                        }
                        is Film -> {
                            if (item.genres.contains(genre.title)) tempList.add(item)
                        }
                        else -> tempList.add(item)
                    }
                }
                listFilmsAdapter.setNew(newData = tempList, full = false)
            }

            // Переход на экран детальной информации о фильме
            override fun openFilm(film: Film) {
                val action = ListFilmsFragmentDirections.actionListFilmsFragmentToDetailFilmFragment(
                    urlImage = film.imageUrl,
                    localizedTitle = film.localizedName,
                    title = film.name,
                    rating = film.rating ?: 0f,
                    year = film.year,
                    description = film.description
                )
                findNavController().navigate(action)
            }
        })

        binding.recyclerListFilms.apply {
            layoutManager = gridLayoutManager
            adapter = listFilmsAdapter
        }
        if (savedInstanceState != null) loadDataFromState(savedInstanceState) else loadData()

        return binding.root
    }

    // Загрузка данных их сохраненного состояния
    private fun loadDataFromState(state: Bundle) {
        presenter.loadFromState(state)
    }

    private fun loadData() {
        presenter.load()
    }

    // Состояние фрагмента
    override fun changeState(state: StateView) {
        when (state) {
            StateView.LOADING -> {
                binding.progressCircular.visibility = View.VISIBLE
                binding.recyclerListFilms.visibility = View.GONE
                binding.textError.visibility = View.GONE
            }
            StateView.ERROR -> {
                binding.progressCircular.visibility = View.GONE
                binding.recyclerListFilms.visibility = View.GONE
                binding.textError.text = presenter.error
                binding.textError.visibility = View.VISIBLE
            }
            StateView.COMPLETE -> {
                listFilmsAdapter.setNew(newData = presenter.getFilmListUI())
                binding.progressCircular.visibility = View.GONE
                binding.textError.visibility = View.GONE
                binding.recyclerListFilms.visibility = View.VISIBLE
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("listFilms", presenter.getFilmList())
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListFilmsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}