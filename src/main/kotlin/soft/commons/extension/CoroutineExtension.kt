package soft.commons.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import soft.commons.util.CoroutineUtil

class CoroutineExtension {
    companion object {
        suspend fun <T> Dispatchers.withContextDefault(block: suspend CoroutineScope.() -> T): T =
            CoroutineUtil.withContextDefault(block)

        suspend fun <T> Dispatchers.withContextIO(block: suspend CoroutineScope.() -> T): T =
            CoroutineUtil.withContextIO(block)

        suspend fun <T> Dispatchers.withContextMain(block: suspend CoroutineScope.() -> T): T =
            CoroutineUtil.withContextMain(block)
    }
}