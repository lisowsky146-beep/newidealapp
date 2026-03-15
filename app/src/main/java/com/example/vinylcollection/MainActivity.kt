package com.example.vinylcollection

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vinylcollection.data.local.AppDatabase
import com.example.vinylcollection.data.network.DiscogsApi
import com.example.vinylcollection.data.repository.VinylRepository
import com.example.vinylcollection.ui.navigation.VinylNavGraph
import com.example.vinylcollection.ui.theme.VinylCollectionTheme
import com.example.vinylcollection.ui.viewmodel.VinylViewModel
import com.example.vinylcollection.ui.viewmodel.VinylViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(applicationContext)
        val repository = VinylRepository(
            vinylDao = db.vinylDao(),
            discogsService = DiscogsApi.service
        )

        setContent {
            VinylCollectionTheme {
                val vm: VinylViewModel = viewModel(factory = VinylViewModelFactory(repository))
                VinylNavGraph(viewModel = vm)
            }
        }
    }
}
