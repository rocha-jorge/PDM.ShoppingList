package a26052.pdmshoppinglist.model

import com.google.firebase.firestore.DocumentId

// ShoppingItem (Modelo de Dados - Representa um Item numa Lista de Compras)
// Representa um item dentro de uma lista de compras.

// - Está associado a um listId (ID da lista onde pertence).
// - Contém nome, quantidade e um estado booleano (isChecked) para marcar como comprado.
// - É uma data class, facilitando a conversão de/para Firestore.


data class ShoppingItem(
    @DocumentId val id: String = "",
    val listId: String = "",  // shopping list
    val name: String = "",
    val quantity: Int = 1,
    val isChecked: Boolean = false
)
