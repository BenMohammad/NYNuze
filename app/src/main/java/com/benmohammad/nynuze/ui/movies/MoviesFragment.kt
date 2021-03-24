package com.benmohammad.nynuze.ui.movies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.benmohammad.nynuze.MOVIES_NEWS
import com.benmohammad.nynuze.NyApp
import com.benmohammad.nynuze.R
import com.benmohammad.nynuze.dagger.modules.ViewModelFactory
import com.benmohammad.nynuze.ui.adapter.NewsAdapter
import com.benmohammad.nynuze.viewState.NewsViewEvent
import com.benmohammad.nynuze.viewState.NewsViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_movies.*
import javax.inject.Inject

class MoviesFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var uiDisposable: Disposable
    private lateinit var disposable: Disposable
    private val newsAdapter: NewsAdapter by lazy {NewsAdapter(requireContext(), MOVIES_NEWS)}
    private val swipeRefresh: PublishSubject<NewsViewEvent.ScreenLoadEvent> = PublishSubject.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        NyApp.getApp(requireContext()).mainComponent.injectMovies(this)
        super.onCreate(savedInstanceState)
        configureViewModel()
        observeViewState()
    }

    private fun configureViewModel() {
        movieViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MovieViewModel::class.java)
    }

    private fun observeViewState() {
        disposable  = movieViewModel.viewState
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                renderViewState(it)
            }, {
                Log.e("MoviesFragment", "Error with viewState")
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        insetWindow()
        init()
    }

    private fun insetWindow() {
        cl_movies.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_FULLSCREEN)
        cl_movies.setOnApplyWindowInsetsListener{v, insets ->
            rv_movies.setPadding(0, insets.systemWindowInsetTop, 0, 0)
            cl_movies.setOnApplyWindowInsetsListener(null)
            insets.consumeSystemWindowInsets()
        }
    }

    private fun init() {
        rv_movies.adapter = newsAdapter
        swipe_refresh_movies.setOnRefreshListener(onRefreshListener)
        val screenLoadEvent: Observable<NewsViewEvent.ScreenLoadEvent> = swipeRefresh
        uiDisposable = screenLoadEvent.subscribe({
            movieViewModel.processInput(it)
        }, {
            Log.e("MoviesFragment", "$it error processing")
        })
    }

    private fun renderViewState(it: NewsViewState?) {
        swipe_refresh_movies.isRefreshing = it?.isLoading ?: false
        if(it?.isEmpty == true) {
            rv_movies.visibility = View.INVISIBLE
            empty_view_movies.visibility = View.VISIBLE
        } else {
            empty_view_movies.visibility = View.GONE
            rv_movies.visibility = View.VISIBLE
            newsAdapter.submitList(it?.adapterList)
        }
        it?.error.let {
            if(!it.isNullOrEmpty()) Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        swipeRefresh.onNext(NewsViewEvent.ScreenLoadEvent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        uiDisposable.dispose()
    }
}