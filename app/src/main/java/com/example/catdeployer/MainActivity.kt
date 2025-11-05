package com.example.catdeployer
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.catdeployer.model.CatUiModel
import com.example.catdeployer.model.Gender
import com.example.catdeployer.model.db.DataManager
import com.example.catdeployer.ui.theme.CatDeployerTheme


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


    }

    override fun onResume() {
        super.onResume()
        setContent {
            CatDeployerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val context = LocalContext.current
                    val db= DataManager(context)

                    var cats=db.getCats()
                    var catName by remember{mutableStateOf("")}
                    var catBiography by remember{mutableStateOf("")}
                    val catPhoto="https://cdn2.thecatapi.com/images/KJF8fB_20.jpg"
                    Column {
                        OutlinedTextField(
                            modifier=Modifier.fillMaxWidth(),
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
}