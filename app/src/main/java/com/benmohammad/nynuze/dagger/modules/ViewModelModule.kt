package com.benmohammad.nynuze.dagger.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.benmohammad.nynuze.ui.details.DetailViewModel
import com.benmohammad.nynuze.ui.home.HomeViewModel
import com.benmohammad.nynuze.ui.movies.MovieViewModel
import com.benmohammad.nynuze.ui.science.ScienceViewModel
import com.benmohammad.nynuze.ui.sports.SportsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun  bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    abstract fun  bindDetailViewModel(detailViewModel: DetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieViewModel::class)
    abstract fun  bindMovieViewModel(movieViewModel: MovieViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(ScienceViewModel::class)
    abstract fun  bindScienceViewModel(scienceViewModel: ScienceViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(SportsViewModel::class)
    abstract fun  bindSportsViewModel(sportsViewModel: SportsViewModel): ViewModel

}