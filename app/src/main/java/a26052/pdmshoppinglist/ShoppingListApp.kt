package a26052.pdmshoppinglist

import android.app.Application
import com.google.firebase.FirebaseApp

// corre no momento de arranque da app, para que a base dados inicie imediatamente
class ShoppingListApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
