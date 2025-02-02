package a26052.pdmshoppinglist.repository

import a26052.pdmshoppinglist.model.ShoppingItem
import a26052.pdmshoppinglist.model.ShoppingList
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.FirebaseAuth


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


    // Add a new shopping list
    suspend fun addShoppingList(name: String) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val list = ShoppingList(name = name, userId = user.uid)
        shoppingListsCollection.add(list).await()
    }

    // Fetch the name of a shopping list
    suspend fun getShoppingListName(listId: String): String {
        return try {
            val snapshot = shoppingListsCollection.document(listId).get().await()
            snapshot.getString("name") ?: "Lista sem nome" //
        } catch (e: Exception) {
            "Erro ao carregar"
        }
    }

    // Delete a shopping list
    suspend fun deleteShoppingList(id: String) {
        shoppingListsCollection.document(id).delete().await()
    }

    // Get ao nome da shopping list
    suspend fun getShoppingLists(): List<ShoppingList> {
        val user = FirebaseAuth.getInstance().currentUser ?: return emptyList()
        return try {
            shoppingListsCollection
                .whereEqualTo("userId", user.uid)
                .get().await()
                .toObjects(ShoppingList::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Fetch all items for a shopping list
    suspend fun getShoppingItems(listId: String): List<ShoppingItem> {
        return try {
            shoppingListsCollection.document(listId).collection("items")
                .get().await()
                .toObjects(ShoppingItem::class.java)
                .filter { it.name.isNotBlank() } // ✅ Filter items with a valid name
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

    // Atualizar a quantidade de um item na lista de compras
    suspend fun updateItemQuantity(listId: String, itemId: String, newQuantity: Int) {
        shoppingListsCollection.document(listId).collection("items")
            .document(itemId)
            .update("quantity", newQuantity).await()
    }


}
