package soft.http.ktor.properties

data class KtorDispatcherProperties (
    /**
     * dispatchers 에서 thread 분리의 복적.
     *  * Dispatchers.IO 를 쓸수는 있으나, thread 공유 상태로 인하여 분리의 복적
     *  * useVtDispatcher = true 인 경우 무시됨
     */
    val ioCount: Int = 0,
    /**
     * virtualThread Dispatcher 사용여부
     */
    val useVtDispatchers: Boolean = false
) {
    init {
        require(ioCount == 0 || !useVtDispatchers) { "You must use either ioCount or vtDispatchers." }
        require(ioCount > 1) { "ioCount must be greater than 0." }
    }
}