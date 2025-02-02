package a26052.pdmshoppinglist.ui.screens

import a26052.pdmshoppinglist.model.ShoppingItem
import a26052.pdmshoppinglist.model.ShoppingList
import a26052.pdmshoppinglist.repository.ShoppingListRepository
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@Composable
fun ItemScreen(listId: String, navController: NavHostController) {
    val repository = ShoppingListRepository()
    var items by remember { mutableStateOf<List<ShoppingItem>>(emptyList()) }
    var listName by remember { mutableStateOf("A carregar nome da lista...") } // State to hold the list name
    var newItemName by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    // Load Items
    LaunchedEffect(listId) {
        listName = repository.getShoppingListName(listId) // Fetch list name
        items = repository.getShoppingItems(listId) // Fetch list items
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(bottom = 16.dp)
        ) {
            Text("Voltar")
        }

        // Mostrar o nome da lista
        Text(
            text = "Lista: ${listName}",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // Adicionar item
        Text(
            text = "Adicionar um novo item:",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(top = 10.dp)
        )

        // Input novo item
        OutlinedTextField(
            value = newItemName,
            onValueChange = {
                if (it.length <= 20) { // maximo 20 caracteres
                    newItemName = it
                }
            },
            label = { Text("Nome do Item (Máx. 20 caracteres)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp) //
        )


        Button(
            onClick = {
                if (newItemName.isNotBlank()) {
                    coroutineScope.launch {
                        repository.addShoppingItem(listId, newItemName, 1) // Add with quantity 1
                        items = repository.getShoppingItems(listId) // Refresh items
                        newItemName = "" // Clear input field
                    }
                }
            },
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.End)
        ) {
            Text("Adicionar Item")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Itens na lista:"
        )

        // Display Items
        LazyColumn (
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (items.isNotEmpty()) {
                items(items) { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Product Name (Takes available space)
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                                .padding(12.dp) // ✅ This ensures name takes available space
                        )

                        // Buttons & Quantity (Fixed Size)
                        Row(
                            verticalAlignment = Alignment.CenterVertically, // ✅ Keep buttons aligned
                            horizontalArrangement = Arrangement.spacedBy(4.dp) // ✅ Even spacing between buttons
                        ) {
                            // Decrease Button
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        val newQuantity = item.quantity - 1
                                        if (newQuantity > 0) {
                                            repository.updateItemQuantity(listId, item.id, newQuantity)
                                        } else {
                                            repository.deleteShoppingItem(listId, item.id) // Delete item if 0
                                        }
                                        items = repository.getShoppingItems(listId) // Refresh list
                                    }
                                }
                            ) {
                                Icon(imageVector = Icons.Default.Remove, contentDescription = "Diminuir")
                            }

                            // Quantity Display
                            Text(
                                text = "${item.quantity}",
                                style = MaterialTheme.typography.bodyLarge
                            )

                            // Increase Button
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        repository.updateItemQuantity(listId, item.id, item.quantity + 1)
                                        items = repository.getShoppingItems(listId) // Refresh list
                                    }
                                }
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Aumentar")
                            }

                            // Delete Button
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        repository.deleteShoppingItem(listId, item.id)
                                        items = repository.getShoppingItems(listId) // Refresh list
                                    }
                                }
                            ) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        }
                    }
                }
            } else {
                item {
                    Text(
                        text = "Nenhum item na lista.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}