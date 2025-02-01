package a26052.pdmshoppinglist.repository

import a26052.pdmshoppinglist.model.ShoppingItem
import a26052.pdmshoppinglist.model.ShoppingList
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


//The repository will handle:
// Fetching shopping lists & items.
// Adding/deleting lists & items.
// Updating item states.

// ShoppingListRepository (Camada de Dados - MVVM / Clean Architecture)
// O repositório que faz a ponte entre o Firestore e a ShoppingListViewModel.

// - Lida com todas as operações no Firestore (buscar, adicionar, eliminar listas e itens).
// - Garante que a ViewModel não interage diretamente com a base de dados.
// - Usa corrotinas para chamadas assíncronas.

class ShoppingListRepository {

    private val db = FirebaseFirestore.getInstance()
    private val shoppingListsCollection = db.collection("shopping_lists")

    // Fetch all shopping lists
    suspend fun getShoppingLists(): List<ShoppingList> {
        return try {
            shoppingListsCollection.get().await().toObjects(ShoppingList::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Add a new shopping list
    suspend fun addShoppingList(name: String) {
        val list = ShoppingList(name = name)
        shoppingListsCollection.add(list).await()
    }

    // Delete a shopping list
    suspend fun deleteShoppingList(id: String) {
        shoppingListsCollection.document(id).delete().await()
    }

    // Fetch all items for a shopping list
    suspend fun getShoppingItems(listId: String): List<ShoppingItem> {
        return try {
            shoppingListsCollection.document(listId).collection("items")
                .get().await().toObjects(ShoppingItem::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Add an item to a shopping list
    suspend fun addShoppingItem(listId: String, name: String, quantity: Int) {
        val item = ShoppingItem(listId = listId, name = name, quantity = quantity)
        shoppingListsCollection.document(listId).collection("items").add(item).await()
    }

    // Toggle item checked state
    suspend fun updateItemChecked(listId: String, itemId: String, isChecked: Boolean) {
        shoppingListsCollection.document(listId).collection("items")
            .document(itemId).update("isChecked", isChecked).await()
    }

    // Delete an item from a shopping list
    suspend fun deleteShoppingItem(listId: String, itemId: String) {
        shoppingListsCollection.document(listId).collection("items")
            .document(itemId).delete().await()
    }
}
