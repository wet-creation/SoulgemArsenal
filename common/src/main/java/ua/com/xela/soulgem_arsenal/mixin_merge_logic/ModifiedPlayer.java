package ua.com.xela.soulgem_arsenal.mixin_merge_logic;


import kotlin.reflect.KClass;

public interface ModifiedPlayer {

    <T> void soulgem$SkillsSwitch(KClass<T> skill);

    <T> void soulgem$SkillsPut(KClass<T> skill, boolean value);

    <T> boolean soulgem$SkillsGet(KClass<T> skill);
    <T> boolean soulgem$SHasKey(KClass<T> skill);
}
