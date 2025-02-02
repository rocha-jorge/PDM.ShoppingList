package a26052.pdmshoppinglist

import a26052.pdmshoppinglist.ui.screens.ItemScreen
import a26052.pdmshoppinglist.ui.screens.ShoppingListScreen
import a26052.pdmshoppinglist.ui.screens.LoginScreen
import a26052.pdmshoppinglist.repository.ShoppingListRepository
import a26052.pdmshoppinglist.viewmodel.ShoppingListViewModel
import a26052.pdmshoppinglist.viewmodel.ShoppingListViewModelFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import a26052.pdmshoppinglist.ui.theme.PDMShoppingListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = ShoppingListRepository()
        val factory = ShoppingListViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, factory).get(ShoppingListViewModel::class.java)

        setContent {
            PDMShoppingListTheme {
                val navController = rememberNavController()
                val auth = FirebaseAuth.getInstance()
                val startDestination = if (auth.currentUser != null) "shoppingList" else "login" // ✅ Start at Login if user is not logged in

                NavHost(navController = navController, startDestination = startDestination) {
                    composable("login") { LoginScreen(navController) }
                    composable("shoppingList") { ShoppingListScreen(navController, viewModel) }
                    composable("itemScreen/{listId}") { backStackEntry ->
                        val listId = backStackEntry.arguments?.getString("listId") ?: ""
                        ItemScreen(listId, navController)
                    }
                }
            }
        }
    }
}
