package com.benmohammad.nynuze.ui.details

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.benmohammad.nynuze.NEWS_ID
import com.benmohammad.nynuze.NEWS_TYPE
import kotlinx.coroutines.newSingleThreadContext
import javax.inject.Inject

class DetailsActivity: AppCompatActivity() {


    companion object {
        fun getNewIntent(id: String, context: Context, type: String): Intent {
            val newIntent = Intent(context, DetailsActivity::class.java)
            newIntent.putExtra(NEWS_ID, id)
            newIntent.putExtra(NEWS_TYPE, type)
            return newIntent
        }
    }
}