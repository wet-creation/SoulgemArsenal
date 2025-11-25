package ua.com.xela.soulgem_arsenal.mixin.injects.server;

import kotlin.reflect.KClass;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import ua.com.xela.soulgem_arsenal.mixin.api.ModifiedPlayer;

import java.util.HashMap;

@Mixin(ServerPlayer.class)
public class ServerPlayerModifiedMixin implements ModifiedPlayer {

    @Unique
    private final HashMap<String, Boolean> soulgem$Skills = new HashMap<>();


    @Override
    public <T> void soulgem_SkillsSwitch(KClass<T> skill) {
        soulgem$Skills.compute(skill.getSimpleName(), (k, value) -> Boolean.FALSE.equals(value));
    }

    @Override
    public <T> void soulgem_SkillsPut(KClass<T> skill, boolean value) {
        soulgem$Skills.put(skill.getSimpleName(), value);
    }

    @Override
    public <T> boolean soulgem_SkillsGet(KClass<T> skill) {
        return Boolean.TRUE.equals(soulgem$Skills.get(skill.getSimpleName()));
    }

    @Override
    public <T> boolean soulgem_HasKey(KClass<T> skill) {
        return soulgem$Skills.get(skill.getSimpleName()) != null;

    }
}
