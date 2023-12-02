package renovation.webflux.server.dto

data class IdNameTypeResponse(
    val id: Long,
    val name: String,
    val type: ResultType
)

enum class ResultType {
    USER,
    COMPANY
}
