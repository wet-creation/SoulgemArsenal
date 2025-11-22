package ua.com.xela.soulgem_arsenal.mixin_merge_logic;


import kotlin.reflect.KClass;

public interface ModifiedPlayer {

    <T> void soulgem_SkillsSwitch(KClass<T> skill);

    <T> void soulgem_SkillsPut(KClass<T> skill, boolean value);

    <T> boolean soulgem_SkillsGet(KClass<T> skill);

    <T> boolean soulgem_HasKey(KClass<T> skill);
}
