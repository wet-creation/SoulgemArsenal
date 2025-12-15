package ua.com.xela.soulgem_arsenal.platform.api

import ua.com.xela.soulgem_arsenal.core.Constants
import ua.com.xela.soulgem_arsenal.platform.api.services.INetworkServiceClient
import ua.com.xela.soulgem_arsenal.platform.api.services.INetworkServiceServer
import ua.com.xela.soulgem_arsenal.platform.api.services.IPlatformClient
import ua.com.xela.soulgem_arsenal.platform.api.services.IPlatformHelper
import java.util.*

object Services {
    // In this example we provide a platform helper which provides information about what platform the mod is running on.
    // For example this can be used to check if the code is running on Forge vs Fabric, or to ask the modloader if another
    // mod is loaded.
    val PLATFORM: IPlatformHelper = loadService<IPlatformHelper>()
    val CLIENT_PLATFORM: IPlatformClient = loadService<IPlatformClient>()
    val NETWORK_SERVER: INetworkServiceServer = loadService<INetworkServiceServer>()
    val NETWORK_CLIENT: INetworkServiceClient = loadService<INetworkServiceClient>()

    // This code is used to load a service for the current environment. Your implementation of the service must be defined
    // manually by including a text file in META-INF/services named with the fully qualified class name of the service.
    // Inside the file you should write the fully qualified class name of the implementation to load for the platform. For
    // example our file on Forge points to ForgePlatformHelper while Fabric points to FabricPlatformHelper.
    inline fun <reified T> loadService(): T {
        val loadedService = ServiceLoader.load(T::class.java)
            .findFirst()
            .orElseThrow {
                NullPointerException("Failed to load service for ${T::class.qualifiedName}")
            }

        Constants.LOG.debug("Loaded {} for service {}", loadedService, T::class.java)
        return loadedService
    }
}
