package com.benmohammad.nynuze.ui.science

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.benmohammad.nynuze.NyApp
import com.benmohammad.nynuze.R
import com.benmohammad.nynuze.SCIENCE_NEWS
import com.benmohammad.nynuze.dagger.modules.ViewModelFactory
import com.benmohammad.nynuze.ui.adapter.NewsAdapter
import com.benmohammad.nynuze.viewState.NewsViewEvent
import com.benmohammad.nynuze.viewState.NewsViewResult
import com.benmohammad.nynuze.viewState.NewsViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_science.*
import javax.inject.Inject

class ScienceFragment: Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private lateinit var scienceViewModel: ScienceViewModel
    private lateinit var uiDisposable: Disposable
    private lateinit var disposable: Disposable
    private val newsAdapter: NewsAdapter by lazy {NewsAdapter(requireContext(), SCIENCE_NEWS)}
    private val swipeRefresh: PublishSubject<NewsViewEvent.ScreenLoadEvent> = PublishSubject.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        NyApp.getApp(requireContext()).mainComponent.injectScience(this)
        super.onCreate(savedInstanceState)
        configureView()
        observeViewState()
    }

    private fun configureView() {
        scienceViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ScienceViewModel::class.java)
    }

    private fun observeViewState() {
        disposable = scienceViewModel.viewState
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    renderViewState(it)
                }, {
                    Log.e("ScienceFragment", "Error with viewState")
                })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_science, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }



    private fun init() {
        rv_science.adapter  =newsAdapter
        swipe_refresh_science.setOnRefreshListener(onRefreshListener)
        val screenLoadEvent: Observable<NewsViewEvent.ScreenLoadEvent> = swipeRefresh
        uiDisposable = screenLoadEvent.subscribe({
            scienceViewModel.processInput(it)
        }, {
            Log.e("ScienceFragment", "$it error processing")
        })
        swipeRefresh.onNext(NewsViewEvent.ScreenLoadEvent)
    }

    private fun renderViewState(it: NewsViewState?) {
        swipe_refresh_science.isRefreshing = it?.isLoading ?: false
        if(it?.isEmpty == true) {
            rv_science.visibility = View.INVISIBLE
            empty_view.visibility = View.INVISIBLE
        } else {
            empty_view.visibility = View.GONE
            rv_science.visibility = View.VISIBLE
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