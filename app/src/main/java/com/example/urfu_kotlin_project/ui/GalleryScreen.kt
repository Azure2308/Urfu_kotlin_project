package com.example.urfu_kotlin_project.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter


import com.example.urfu_kotlin_project.data.generateSamplePictures
import com.example.urfu_kotlin_project.models.Picture


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen() {
    var gallery by remember { mutableStateOf<List<Picture>>(generateSamplePictures()) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    var isGrid by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var newAuthor by remember { mutableStateOf("") }

    val filteredGallery = gallery.filter {
        it.author.contains(searchText.text, ignoreCase = true)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Галерея") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Поиск по автору") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { isGrid = !isGrid }) {
                    Icon(
                        imageVector = if (isGrid) Icons.AutoMirrored.Filled.List else Icons.Filled.Menu,
                        contentDescription = "Список/Сетка"
                    )
                }
                IconButton(onClick = { gallery = emptyList() }) {
                    Icon(Icons.Default.Clear, contentDescription = "Очистить всё")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isGrid) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredGallery, key = { it.id }) { picture ->
                        PictureCard(picture) {
                            gallery = gallery.filterNot { it.id == picture.id }
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredGallery, key = { it.id }) { picture ->
                        PictureCard(picture) {
                            gallery = gallery.filterNot { it.id == picture.id }
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Добавить") },
                text = {
                    Column {
                        TextField(
                            value = newAuthor,
                            onValueChange = { newAuthor = it },
                            label = { Text("Автор") }
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        val newPic = Picture(
                            id = (gallery.maxOfOrNull { it.id } ?: 0) + 1,
                            author = newAuthor.ifBlank { "Никто" },
                            url = "https://avatar.iran.liara.run/public/${(1..100).random()}"
                        )
                        if (gallery.none { it.url == newPic.url }) {
                            gallery = gallery + newPic
                        }
                        showAddDialog = false
                        newAuthor = ""
                    }) {
                        Text("Добавить")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showAddDialog = false
                        newAuthor = ""
                    }) {
                        Text("Отмена")
                    }
                }
            )
        }
    }
}

@Composable
fun PictureCard(picture: Picture, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDelete() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(picture.url),
                contentDescription = picture.author,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Text(
                text = picture.author,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

