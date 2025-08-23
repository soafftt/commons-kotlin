package soft.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.getOrElse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Test

class ChannelTest {
    private val channel = Channel<Int>(BUFFERED)


    @Test
fun consumeEachTest() {
    (1..10).forEach {
        channel.trySend(it)
    }


    CoroutineScope(Dispatchers.IO).launch {
        channel.receiveAsFlow()
            .buffer(capacity = 20, onBufferOverflow = BufferOverflow.SUSPEND)
            .map {
                // 데이터 변환
                it
            }
            .catch {
                // 에러 처리.
            }
            .flowOn(Dispatchers.Default) // flow Dispatchers 변경.
            .collect { println(it) }
    }


    (11..20).forEach {
        channel.trySend(it)
    }
}

}