package com.example.catdeployer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.catdeployer.data.PreferenceStore
import com.example.catdeployer.model.PreferenceViewModel
import com.example.catdeployer.ui.theme.CatDeployerTheme

class PreferencesActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val preferenceStore= PreferenceStore(applicationContext)
        val preferenceViewModel= PreferenceViewModel(preferenceStore)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            CatDeployerTheme {

                /*val preferenceViewModel = viewModel<PreferenceViewModel>(factory = object :
                    ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return PreferenceViewModel(preferenceStore) as T
                    }
                })*/

                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar={
                        TopAppBar(
                            title={Text(getString(R.string.app_name))},
                            modifier = Modifier.statusBarsPadding(),
                            colors= TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                            navigationIcon = {
                                IconButton(onClick = {
                                    finish()
                                }){
                                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription="Back")

                                }

                            }
                        )
                    }
                    ) { innerPadding ->


                    var checked by remember {mutableStateOf(false) }
                    checked=preferenceViewModel.state.collectAsState().value.order;
                    Log.d("checked","entro "+checked.toString())
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier= Modifier.padding(innerPadding)
                    ){
                        Text("Orden alfabético: ")
                        Checkbox(
                            checked=checked,
                            onCheckedChange = {
                                checked=it
                                preferenceViewModel.saveOrder(it)
                                Log.d("checked","pulsado"+it.toString())

                            }
                        )

                    }
                }
            }
        }
    }
}

