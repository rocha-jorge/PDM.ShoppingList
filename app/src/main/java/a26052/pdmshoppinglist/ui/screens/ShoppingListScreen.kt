package a26052.pdmshoppinglist.ui.screens

import a26052.pdmshoppinglist.viewmodel.ShoppingListViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun ShoppingListScreen(navController: NavHostController, viewModel: ShoppingListViewModel = viewModel()) {
    val shoppingLists by viewModel.shoppingLists.collectAsState()

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Listas de Compras",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top=10.dp).padding(bottom = 10.dp)
            )

        Text(
            text = "Adicionar uma nova lista:",
            style = MaterialTheme.typography.headlineSmall,
        )
        // Button to Add a new Shopping List
        var newListName by remember { mutableStateOf("") }
        OutlinedTextField(
            value = newListName,
            onValueChange = { newListName = it },
            label = { Text("Nome da Lista") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (newListName.isNotBlank()) {
                    viewModel.addShoppingList(newListName)
                    newListName = "" // Clear input after adding
                }
            },
        ) {
            Text("Adicionar Lista")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display shopping lists
        LazyColumn {
            items(shoppingLists) { list ->
                ShoppingListItem(
                    name = list.name,
                    onClick = { navController.navigate("itemScreen/${list.id}") },
                    onDelete = { viewModel.deleteShoppingList(list.id) }
                )
            }
        }
    }
}

@Composable
fun ShoppingListItem(name: String, onClick: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically, // Alinha nome da lista e botao de apagar
        horizontalArrangement = Arrangement.SpaceBetween //
    ) {
        // Item Name (Expands to take available space)
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f) // ✅ Makes sure the text takes the available space
        )

        // Delete Button
        IconButton(
            onClick = onDelete
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
        }
    }
}

