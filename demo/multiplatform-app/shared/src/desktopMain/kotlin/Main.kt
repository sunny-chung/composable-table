import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.sunnychung.lib.android.composabletable.demo.ux.AppView

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Composable Table Demo") {
        AppView()
    }
}
