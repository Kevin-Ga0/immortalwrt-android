package com.immortalwrt.manager.core.network

enum class UbusStatusCode(val code: Int) {
    OK(0),
    INVALID_COMMAND(1),
    INVALID_ARGUMENT(2),
    METHOD_NOT_FOUND(3),
    RESOURCE_NOT_FOUND(4),
    NO_DATA(5),
    PERMISSION_DENIED(6),
    TIMEOUT(7),
    NOT_SUPPORTED(8),
    UNSPECIFIED_ERROR(9),
    CONNECTION_LOST(10);

    companion object {
        fun fromCode(code: Int): UbusStatusCode =
            entries.find { it.code == code } ?: UNSPECIFIED_ERROR
    }
}
