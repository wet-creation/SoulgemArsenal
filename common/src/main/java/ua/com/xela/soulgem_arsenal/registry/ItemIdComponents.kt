package ua.com.xela.soulgem_arsenal.registry

import ua.com.xela.soulgem_arsenal.platform.api.Services
import ua.com.xela.soulgem_arsenal.skills.base.ItemInstanceId

object ItemIdComponents {

    lateinit var ITEM_ID: ComponentRef<ItemInstanceId>

    fun register() {
        ITEM_ID = Services.PLATFORM.registerDataComponent(
            "item_instance_id",
            ItemInstanceId.CODEC,
            ItemInstanceId.STREAM_CODEC
        )
    }
}
