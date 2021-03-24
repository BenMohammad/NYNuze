package com.benmohammad.nynuze.dagger

import com.benmohammad.nynuze.dagger.modules.ApplicationModule
import com.benmohammad.nynuze.dagger.modules.ContextModule
import com.benmohammad.nynuze.dagger.modules.DatabaseModule
import com.benmohammad.nynuze.dagger.modules.ViewModelModule
import com.benmohammad.nynuze.dagger.scope.ApplicationScope
import com.benmohammad.nynuze.ui.details.DetailsActivity
import com.benmohammad.nynuze.ui.home.HomeFragment
import com.benmohammad.nynuze.ui.movies.MoviesFragment
import com.benmohammad.nynuze.ui.science.ScienceFragment
import com.benmohammad.nynuze.ui.sports.SportsFragment
import dagger.Component

@ApplicationScope
@Component(modules = [ContextModule::class, ApplicationModule::class, DatabaseModule::class, ViewModelModule::class])
interface MainComponent {

    fun inject(homeFragment: HomeFragment)
    fun inject(detailsActivity: DetailsActivity)
    fun inject(moviesFragment: MoviesFragment)
    fun inject(scienceFragment: ScienceFragment)
    fun inject(sportsFragment: SportsFragment)

}