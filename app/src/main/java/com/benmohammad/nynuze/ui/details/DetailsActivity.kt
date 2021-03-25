package com.benmohammad.nynuze.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.benmohammad.nynuze.*
import com.benmohammad.nynuze.dagger.modules.ViewModelFactory
import com.benmohammad.nynuze.viewState.DetailViewEffect
import com.benmohammad.nynuze.viewState.DetailViewEvent
import com.benmohammad.nynuze.viewState.DetailViewResult
import com.benmohammad.nynuze.viewState.DetailViewState
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.coroutines.newSingleThreadContext
import javax.inject.Inject

class DetailsActivity: AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private lateinit var detailViewModel: DetailViewModel
    private val disposable: CompositeDisposable by lazy { CompositeDisposable() }
    private lateinit var uiDisposable: Disposable
    private val openLinkEvent : PublishSubject<String> = PublishSubject.create()

    companion object {
        fun getNewIntent(id: String, context: Context, type: String): Intent {
            val newIntent = Intent(context, DetailsActivity::class.java)
            newIntent.putExtra(NEWS_ID, id)
            newIntent.putExtra(NEWS_TYPE, type)
            return newIntent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        NyApp.getApp(this).mainComponent.injectDetails(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        configureViewModel()
        init()
        attachStateObserver()
    }



    override fun onResume() {
        super.onResume()
        val loadDetailEvent = Observable.just(DetailViewEvent.LoadDetailEvent(
            intent.getStringExtra(NEWS_ID)!!,
            intent.getStringExtra(NEWS_TYPE)!!
        ))

        val openLinkEvent: Observable<DetailViewEvent.OpenChromeEvent> = openLinkEvent
            .map {DetailViewEvent.OpenChromeEvent(it)}
        uiDisposable = Observable.merge(
            openLinkEvent,
            loadDetailEvent
        ).subscribe({
            detailViewModel.processInput(it)
        },
            {
                Log.e("DetailsActivity", "Error processing input")
            }
            )
    }

    private fun configureViewModel() {
        detailViewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)
    }

    private fun attachStateObserver() {
        disposable.add(
            detailViewModel.viewState
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    renderViewState(it)
                }, {
                    error("Something wrong happened set viewState")
                })
        )
        disposable.add(
            detailViewModel.viewEffects
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    trigger(it)
                }, {
                    error("Something wrong in the view State")
                })
        )
    }

    private fun trigger(effect: DetailViewEffect?) {
        effect ?: return
        when(effect) {
            is DetailViewEffect.OpenChromeEffect -> {
                openChrome(effect.uri) {
                    Toast.makeText(this, "Can't open browser", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun renderViewState(it: DetailViewState?) {
        if(it?.isLoading == true) progress.visibility = View.VISIBLE
        else progress.visibility = View.INVISIBLE
        if(!it?.coverPhoto.isNullOrEmpty()) {
            Picasso.get()
                .load(it?.coverPhoto)
                .fit()
                .centerCrop()
                .into(iv_cover)
        }
        tv_title.text = it?.title
        tv_abstract.text = it?.abstract
        author.text = it?.author
        tv_link.text = it?.link
        published.text = String.format("Published:", it?.dateToFormat(it.published ?: ""))
    }

    private fun init() {
        tv_link.setOnClickListener {
            openLinkEvent.onNext(tv_link.text.toString())
        }

    }

    override fun onPause() {
        super.onPause()
        uiDisposable.dispose()
        disposable.dispose()
    }
}