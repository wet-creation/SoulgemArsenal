# Soulgem Arsenal Overview

- Minecraft 1.21 multi-loader mod (Fabric and Forge); Kotlin-first code in `common/`.
- Requires JDK 21; versions pinned in `gradle.properties`.
- Shared Gradle logic lives in `buildSrc` (`multiloader-common.gradle`, `multiloader-loader.gradle`).

## Layout Highlights
- `common/` shared gameplay logic, registries, mixins, assets.
- `fabric/` Fabric entrypoint, loader resources, platform helper service.
- `forge/` Forge entrypoint, event handlers, loader resources, platform helper service.
- `buildSrc/` convention plugins that wire common sources/resources into each loader.
- `./gradlew build` outputs per-loader jars to `<loader>/build/libs`.

## Build & Run
- Build all: `./gradlew build`
- Shared-only check: `./gradlew :common:jar`
- Fabric dev: `./gradlew :fabric:runClient` or `:fabric:runServer`
- Forge dev: `./gradlew :forge:runClient`, `:forge:runServer`, `:forge:data` (writes to `forge/src/generated/resources`)

## Core Systems (Common)
- Init entry: `common/src/main/java/ua/com/xela/soulgem_arsenal/Common.kt` registers items, data components, and skill behaviors.
- Platform abstraction: `IPlatformHelper` (`common/.../platform/services/IPlatformHelper.kt`) implemented per loader and loaded via `META-INF/services`.
- Data components: registered in `SkillComponents` (`common/.../registries/SkillComponents.kt`) and keyed by `KClass` for each skill.
- Skill behaviors: `SkillBehaviorRegistry` and `SkillEvents` (`common/.../skills/base/`) route bow/arrow events to behaviors.
- Player mixin storage: `ServerPlayerModifiedMixin` implements `ModifiedPlayer` to track transient flags per skill.

## Teleport Bow Skill Flow
Paths reference `common/src/main/java/ua/com/xela/soulgem_arsenal`.
- Data model `skills/bow/telport/TeleportBow.kt` (`TeleportSkill`): fields `enabled`, `armed`, `charges` with `CODEC`/`STREAM_CODEC`.
- Behavior `skills/bow/telport/TeleportSkillBehavior.kt`:
  - On bow shot: if armed/enabled with charges, flag the shooter via `ModifiedPlayer`, consume a charge, disarm.
  - On arrow block/entity hit: if shooter flagged, teleport shooter to the arrow hit location and clear the flag.
- Item helpers `skills/SkillsExt.kt`: get/set skill data components on `ItemStack`, `armTeleportSkill`, `hasTeleportSkill`.
- Mixin hooks (`mixin/`):
  - `BowItemMixin` injects into `releaseUsing` to call `SkillEvents.onBowShot`.
  - `AbstractArrowMixin` injects into `onHitBlock` and `onHitEntity` to dispatch arrow hit events.
  - `MixinMinecraft` logs version on client init.

## Loader-Specific Behavior
- Fabric
  - Entrypoint `fabric/src/main/java/ua/com/xela/soulgem_arsenal/SoulGemArsenal.kt`.
  - `UseItemCallback` (shift + bow) installs teleport skill with 3 charges or arms next shot; sends chat feedback.
  - Platform helper implementation `fabric/.../platform/FabricPlatformHelper.kt`; service file in `fabric/src/main/resources/META-INF/services/...IPlatformHelper`.
- Forge
  - Entrypoint `forge/src/main/java/ua/com/xela/soulgem_arsenal/SoulgemArsenal.kt`; registers DeferredRegisters via `ForgePlatformHelper.register`.
  - Interaction handler `forge/.../interations/ForgeInteractionEvents.kt` listens for shift + bow right-click to install/arm teleport skill; uses chat messages.
  - Platform helper implementation `forge/.../platform/ForgePlatformHelper.kt`; service file in `forge/src/main/resources/META-INF/services/...IPlatformHelper`.

## Resources & Metadata
- Common mixin config `common/src/main/resources/soulgem_arsenal.mixins.json`.
- Loader mixins: `fabric/src/main/resources/soulgem_arsenal.fabric.mixins.json`, `forge/src/main/resources/soulgem_arsenal.forge.mixins.json`.
- Mod metadata: `fabric/src/main/resources/fabric.mod.json`, `forge/src/main/resources/META-INF/mods.toml`.
- Pack metadata: `common/src/main/resources/pack.mcmeta`.

## Adding a New Skill (Blueprint)
1) Create skill data class with `Codec`/`StreamCodec` under `common/.../skills/...`.
2) Register its data component in `SkillComponents.register()` and add to the `skills` map.
3) Implement `SkillBehavior` and register in `Common.registerSkillBehaviors()`.
4) Add mixin/event hook to trigger behavior (e.g., bow shot, arrow hit, player action).
5) Expose loader-specific interaction (Fabric callback / Forge event) to attach the skill to items and arm/disarm it.
