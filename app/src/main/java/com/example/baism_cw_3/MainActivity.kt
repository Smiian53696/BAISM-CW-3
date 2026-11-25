package com.example.baism_cw_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.baism_cw_3.model.Character
import com.example.baism_cw_3.network.RetrofitClient
import com.example.baism_cw_3.ui.theme.BAISMCW3Theme
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BAISMCW3Theme {
                RickMortyScreen()
            }
        }
    }
}

@Composable
fun RickMortyScreen() {

    var characters by remember {
        mutableStateOf<List<Character>>(emptyList())
    }

    var selectedCharacter by remember {
        mutableStateOf<Character?>(null)
    }

    var characterId by remember { mutableStateOf("1") }
    var error by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp)
    ) {

        Text(
            text = "Rick and Morty API",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Pobieranie listy postaci
        Button(
            onClick = {
                scope.launch {
                    loading = true
                    error = ""

                    try {
                        val response = RetrofitClient.api.getCharacters()
                        characters = response.results

                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        error = e.toString()
                    } finally {
                        loading = false
                    }
                }
            },
            enabled = !loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pobierz wszystkie postacie")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = characterId,
            onValueChange = { characterId = it },
            label = { Text("ID postaci") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Pobieranie postaci po ID
        Button(
            onClick = {
                val id = characterId.toIntOrNull()

                if (id == null || id <= 0) {
                    error = "Podaj poprawne ID"
                } else {
                    scope.launch {
                        loading = true
                        error = ""
                        selectedCharacter = null

                        try {
                            selectedCharacter = RetrofitClient.api
                                .getCharacterById(id)

                        } catch (e: CancellationException) {
                            throw e
                        } catch (e: Exception) {
                            error = e.toString()
                        } finally {
                            loading = false
                        }
                    }
                }
            },
            enabled = !loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pobierz postać po ID")
        }

        if (loading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }

        // Dane wybranej postaci
        selectedCharacter?.let { character ->

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = character.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Text("ID: ${character.id}")
                    Text("Status: ${character.status}")
                    Text("Species: ${character.species}")
                    Text("Gender: ${character.gender}")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Lista postaci (${characters.size})",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        // Lista wszystkich postaci
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(characters) { character ->

                Text(
                    text = "${character.id}. ${character.name}",
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                HorizontalDivider()
            }
        }
    }
}