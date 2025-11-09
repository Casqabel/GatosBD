package com.example.catdeployer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset


import androidx.lifecycle.ViewModelProvider
import com.example.catdeployer.data.PreferenceStore
import com.example.catdeployer.model.CatUiModel
import com.example.catdeployer.model.Gender
import com.example.catdeployer.model.PreferenceViewModel
import com.example.catdeployer.model.db.DataManager
import com.example.catdeployer.ui.theme.CatDeployerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()




    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onResume() {
        super.onResume()
        val preferenceStore= PreferenceStore(applicationContext)
       val preferenceViewModel= PreferenceViewModel(preferenceStore)
        setContent {
            val context = LocalContext.current
            CatDeployerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                topBar={
                   TopAppBar(
                        title={Text(getString(R.string.app_name))},
                        modifier = Modifier.statusBarsPadding(),
                        colors= TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                       actions={
                           val (isExpanded, setExpanded) = remember{mutableStateOf(false)}
                           IconButton(onClick={setExpanded(true)

                           }) {

                               Icon(imageVector= Icons.Filled.MoreVert, contentDescription = "Ver más")
                               DropdownMenu(expanded=isExpanded,
                                   onDismissRequest={setExpanded(false)},
                                   //offset= DpOffset(x=0.dp, y=4.dp)
                                   ) {
                                   DropdownMenuItem(
                                       text={Text("Preferencias")},
                                       onClick={
                                          val intent=Intent(context, PreferencesActivity::class.java)
                                           context.startActivity(intent)
                                           setExpanded(false)
                                       }
                                   )
                               }



                           }

                       }
                    )
                }
                ) { innerPadding ->


                    val db= DataManager(context)
                    var cats: SnapshotStateList<CatUiModel>
                    if (preferenceViewModel.state.collectAsState().value.order==true){
                        cats=db.getCats()}
                    else{
                        cats=db.getCatsIds()}
                    var catName by remember{mutableStateOf("")}
                    var catBiography by remember{mutableStateOf("")}
                    val catPhoto="https://cdn2.thecatapi.com/images/KJF8fB_20.jpg"
                    Column {
                        OutlinedTextField(
                            modifier=Modifier.fillMaxWidth()
                                .padding(innerPadding),
                            value=catName,
                            onValueChange = {catName=it},
                            label={
                                Text(text= stringResource(id=R.string.cat_name))
                            }

                        )
                        OutlinedTextField(
                            modifier=Modifier.fillMaxWidth(),
                            value=catBiography,
                            onValueChange = {catBiography=it},
                            label={
                                Text(text= stringResource(id=R.string.cat_biography))
                            }

                        )

                        Button(
                            modifier=Modifier.fillMaxWidth(),
                            onClick={
                                db.insert(catName,Gender.FEMALE,catBiography, catPhoto)
                               onResume()



                            }
                        ){
                            Text("Añadir Gato")
                        }
                        CatAgents(
                            cats = cats,
                            modifier = Modifier.padding(innerPadding),
                            onCatClick = { catIndex ->
                                Toast.makeText(
                                    context,
                                    "${cats[catIndex].name} clicked!",
                                    Toast.LENGTH_SHORT

                                ).show()

                            },
                            onCatSwipe = {itemIndex->

                                db.delete(cats[itemIndex].id)

                                cats.removeAt(itemIndex)
                            }
                        )}
                }
            }
        }
    }
}


@Composable
fun CatAgents(
    cats: List<CatUiModel>,
    modifier: Modifier = Modifier,
    onCatClick: (Int) -> Unit = {},
    onCatSwipe: (Int) -> Unit= {}
) {
    val columnState = rememberLazyListState()

    LazyColumn(state = columnState, modifier = modifier) {
        items(cats.size,
            key={index -> cats[index].id}
        )
        { index ->
            Cat(
                cat = cats[index],
                onClick = {
                    onCatClick(index)
                },
                onSwipe={
                    onCatSwipe(index)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CatDeployerTheme {
        CatAgents(
            cats = listOf(
                CatUiModel(1,
                    gender = Gender.MALE,
                    name = "Fred",
                    biography = "Silent and deadly",
                    imageUrl = ""
                ),
                CatUiModel(2,
                    gender = Gender.FEMALE,
                    name = "Wilma",
                    biography = "Cuddly assassin",
                    imageUrl = ""
                ),
                CatUiModel(3,
                    gender = Gender.UNKNOWN,
                    name = "Curious George",
                    biography = "Award winning investigator",
                    imageUrl = ""
                )
            )
        )
    }

    data class ActionItem(
        val name: String,
        val icon: ImageVector?=null,
        val action:()-> Unit,
        val order: Int

    )
}