package ua.com.xela.soulgem_arsenal.core.utils

import java.util.UUID

fun UUID?.orRandom(): UUID {
    return this ?: UUID.randomUUID()
}