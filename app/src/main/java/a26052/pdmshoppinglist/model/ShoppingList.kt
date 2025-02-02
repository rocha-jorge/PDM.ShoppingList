package a26052.pdmshoppinglist.model

import com.google.firebase.firestore.DocumentId


// ShoppingList (Modelo de Dados - Representa uma Lista de Compras)
// Representa uma lista de compras na base de dados Firestore.

// - Contém um ID, um nome e a data de criação.
// - É uma data class mapeada automaticamente pelo Firestore.

data class ShoppingList(
    @DocumentId val id: String = "",
    val name: String = "",
    val userId: String = ""
)