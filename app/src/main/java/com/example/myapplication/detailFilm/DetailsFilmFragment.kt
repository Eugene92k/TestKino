package com.example.myapplication.detailFilm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDetailFilmBinding
import com.example.myapplication.loadImage

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DetailsFilmFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private val args: DetailsFilmFragmentArgs by navArgs()
    lateinit var binding: FragmentDetailFilmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDetailFilmBinding.inflate(inflater, container, false)

        binding.toolbarFilmDetail.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imageFilm.loadImage(args.urlImage)
        binding.titleFilm.text = args.title
        binding.yearFilm.text = getString(R.string.film_detail_year, args.year)
        binding.ratingFilm.text = getString(R.string.film_detail_rating, args.rating)
        binding.descriptionFilm.text = args.description

        binding.toolbarFilmDetail.title = args.localizedTitle

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailsFilmFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}