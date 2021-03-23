package com.benmohammad.nynuze.data

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class SessionManager @Inject constructor(context: Context) {
    private val prefs: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        val PRIVATE_MODE = 0
        prefs  =context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = prefs.edit()
    }

    var lastFetchTimeHomeNews: Long?
    get() = prefs.getLong(LAST_FETCH_TIME_HOME_NEWS, 0L)
    set(time) {
        editor.putLong(LAST_FETCH_TIME_HOME_NEWS, time ?: 0L)
        editor.apply()
    }

    var lastFetchTimeMovieNews: Long?
    get() = prefs.getLong(LAST_FETCH_TIME_MOVIE_NEWS, 0L)
    set(time) {
        editor.putLong(LAST_FETCH_TIME_MOVIE_NEWS, time ?: 0L)
        editor.apply()
    }

    var lastFetchTimeScienceNews: Long?
    get() = prefs.getLong(LAST_FETCH_TIME_SCIENCE_NEWS, 0L)
    set(time) {
        editor.putLong(LAST_FETCH_TIME_SCIENCE_NEWS, time ?: 0L)
        editor.apply()
    }

    var lastFetchTimeSportsNews: Long?
    get() = prefs.getLong(LAST_FETCH_TIME_SPORTS_NEWS, 0L)
    set(time) {
        editor.putLong(LAST_FETCH_TIME_SPORTS_NEWS, time ?: 0L)
        editor.apply()
    }

    companion object {
        private const val PREF_NAME = "NY_PREFS"
        private const val LAST_FETCH_TIME_HOME_NEWS = "lastFetchTimeHome"
        private const val LAST_FETCH_TIME_MOVIE_NEWS = "lastFetchTimeMovie"
        private const val LAST_FETCH_TIME_SCIENCE_NEWS = "lastFetchTimeScience"
        private const val LAST_FETCH_TIME_SPORTS_NEWS = "lastFetchTimeSports"
    }
}