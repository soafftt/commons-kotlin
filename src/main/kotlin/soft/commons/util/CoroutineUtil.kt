package soft.commons.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CoroutineUtil {
    companion object {
        suspend fun <T> withContextDefault(block: suspend CoroutineScope.() -> T): T =
            withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
                block()
            }

        suspend fun <T> withContextIO(block: suspend CoroutineScope.() -> T): T =
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                block()
            }

        suspend fun <T> withContextMain(block: suspend CoroutineScope.() -> T): T =
            withContext(CoroutineScope(Dispatchers.Main).coroutineContext) {
                block()
            }
    }
}