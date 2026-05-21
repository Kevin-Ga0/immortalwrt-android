package com.immortalwrt.manager.data.local

import androidx.room.TypeConverter
import com.immortalwrt.manager.domain.model.DeviceSource
import com.immortalwrt.manager.domain.model.EndpointMode
import com.immortalwrt.manager.domain.model.RouterConnectionState
import com.immortalwrt.manager.domain.model.RouterScheme

class Converters {
    @TypeConverter
    fun fromRouterScheme(scheme: RouterScheme): String = scheme.name

    @TypeConverter
    fun toRouterScheme(value: String): RouterScheme = RouterScheme.valueOf(value)

    @TypeConverter
    fun fromEndpointMode(mode: EndpointMode): String = mode.name

    @TypeConverter
    fun toEndpointMode(value: String): EndpointMode = EndpointMode.valueOf(value)

    @TypeConverter
    fun fromDeviceSources(sources: Set<DeviceSource>): String =
        sources.joinToString(",") { it.name }

    @TypeConverter
    fun toDeviceSources(value: String): Set<DeviceSource> =
        if (value.isBlank()) emptySet()
        else value.split(",").map { DeviceSource.valueOf(it.trim()) }.toSet()

    @TypeConverter
    fun fromStringList(list: List<String>): String = list.joinToString(",")

    @TypeConverter
    fun toStringList(value: String): List<String> =
        if (value.isBlank()) emptyList()
        else value.split(",").map { it.trim() }

    @TypeConverter
    fun fromFloatList(list: List<Float>): String = list.joinToString(",")

    @TypeConverter
    fun toFloatList(value: String): List<Float> =
        if (value.isBlank()) emptyList()
        else value.split(",").map { it.trim().toFloat() }

    @TypeConverter
    fun fromRouterConnectionState(state: RouterConnectionState): String = state.name

    @TypeConverter
    fun toRouterConnectionState(value: String): RouterConnectionState =
        RouterConnectionState.valueOf(value)
}
