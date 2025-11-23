package ua.com.xela.soulgem_arsenal.skills.api

interface Skill {
    //player selected
    val selected: Boolean
    //is ready for use
    val armed: Boolean
    //cooldown in seconds for next recharge
    val cooldown: Int
    //count of charges
    val charges: Int
}