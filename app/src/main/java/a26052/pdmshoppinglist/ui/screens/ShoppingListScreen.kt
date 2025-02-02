package a26052.pdmshoppinglist.ui.screens

import a26052.pdmshoppinglist.ui.theme.Purple40
import a26052.pdmshoppinglist.viewmodel.ShoppingListViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ShoppingListScreen(navController: NavHostController, viewModel: ShoppingListViewModel = viewModel()) {
    val shoppingLists by viewModel.shoppingLists.collectAsState()

    // ✅ Fetch lists when this screen is opened
    LaunchedEffect(Unit) {
        viewModel.loadShoppingLists()
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login") { popUpTo("shoppingList") { inclusive = true } }
            },
            modifier = Modifier.align(Alignment.End).padding(top = 12.dp)
        ) {
            Text("Logout")
        }

        Text(
            text = "Listas de compras",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)) // ✅ Background color
                .padding(12.dp)
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
            label = { Text("Nome da lista") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (newListName.isNotBlank()) {
                    viewModel.addShoppingList(newListName)
                    newListName = "" // Clear input after adding
                    viewModel.loadShoppingLists() // ✅ Fetch updated lists after adding
                }
            },
        ) {
            Text("Adicionar lista")
        }

        Text(
            text = "Selecione uma lista:",
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.headlineSmall
        )

        // ✅ Display shopping lists & ensure they are loaded
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp) // espaçamento entre listas
        ) {
            items(shoppingLists) { list ->
                ShoppingListItem(
                    name = list.name,
                    onClick = { navController.navigate("itemScreen/${list.id}") },
                    onDelete = {
                        viewModel.deleteShoppingList(list.id)
                        viewModel.loadShoppingLists() // ✅ Refresh lists after deleting
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                )
            }
        }
    }
}


@Composable
fun ShoppingListItem(
    name: String,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)) // ✅ Background color
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = onDelete) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
        }
    }
}


