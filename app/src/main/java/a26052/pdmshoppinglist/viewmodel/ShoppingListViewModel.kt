package a26052.pdmshoppinglist.viewmodel

import a26052.pdmshoppinglist.model.ShoppingList
import a26052.pdmshoppinglist.repository.ShoppingListRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


//✔ Fetches shopping lists from Firestore using ShoppingListRepository.
//✔ Uses StateFlow to expose data to Jetpack Compose UI.
//✔ Handles adding and deleting shopping lists asynchronously using viewModelScope.
//✔ Automatically loads lists when initialized.


// ShoppingListViewModel (Camada ViewModel - MVVM)
// É a ViewModel responsável por gerir a lógica de apresentação dos dados da lista de compras.
// - Interage com o repositório (ShoppingListRepository) para obter e modificar os dados no Firestore.
// - Usa StateFlow para armazenar e expor os dados ao Jetpack Compose.
// - Gere operações assíncronas (adicionar, remover e carregar listas).

class ShoppingListViewModel(
    private val repository: ShoppingListRepository
) : ViewModel() {

    private val _shoppingLists = MutableStateFlow<List<ShoppingList>>(emptyList())
    val shoppingLists: StateFlow<List<ShoppingList>> = _shoppingLists

    init {
        loadShoppingLists()
    }

    fun loadShoppingLists() {
        viewModelScope.launch {
            _shoppingLists.value = repository.getShoppingLists()
        }
    }

    fun addShoppingList(name: String) {
        viewModelScope.launch {
            repository.addShoppingList(name)
            loadShoppingLists()  // Refresh list after adding
        }
    }

    fun deleteShoppingList(id: String) {
        viewModelScope.launch {
            repository.deleteShoppingList(id)
            loadShoppingLists()  // Refresh list after deletion
        }
    }
}
