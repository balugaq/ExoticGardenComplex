package io.github.thebusybiscuit.exoticgarden;

import org.bukkit.potion.PotionEffectType;

public class VersionedPotionEffectType {
    public static PotionEffectType DAMAGE_RESISTANCE = get("DAMAGE_RESISTANCE", "RESISTANCE");
    public static PotionEffectType INCREASE_DAMAGE = get("INCREASE_DAMAGE", "STRENGTH");
    public static PotionEffectType HEAL = get("HEAL", "INSTANT_HEAL");
    public static PotionEffectType CONFUSION = get("CONFUSION", "NAUSEA");

    public static PotionEffectType get(String... names) {
        for (PotionEffectType type : PotionEffectType.values()) {
            for (String name : names) {
                if (type.getName().equalsIgnoreCase(name)) {
                    return type;
                }
            }
        }

        return null;
    }
}
