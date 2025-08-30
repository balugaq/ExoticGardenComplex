package com.be.registry;

import org.bukkit.ChatColor;

import com.be.utils.RegistryHandler;

import io.github.thebusybiscuit.exoticgarden.PlantType;

public class BEPlants {

    public static void onPlantsRegister() {
        RegistryHandler.initPlant("BIG_CARROT", "大萝卜", ChatColor.RED, PlantType.DOUBLE_PLANT, true, "2448c183a7640867e42118e69c3f4d15db1ffb0d93646b77078ecedca2a43454");
        RegistryHandler.initPlant("BIG_WHEAT", "异域大麦", ChatColor.YELLOW, PlantType.DOUBLE_PLANT, true, "3b3c84e4bdaf5cc5f85632ac928d059fc2f1ff0cc9e5998f1fe8b227881ada85");
        RegistryHandler.initPlant("AMARANTH", "异域苋菜", ChatColor.RED, PlantType.FRUIT, true, "e6ef614b3a5fbec9b8af35d8a40e91dccdd8977c712ecef6ce52d91af49c4c93");
        RegistryHandler.initPlant("MEXICO_CHILI", "异域墨西哥辣椒", ChatColor.DARK_GREEN, PlantType.DOUBLE_PLANT, true, "5c8e453e84f663f2f6f4af8ed58e65a47aa8c5bffc2a4f67fad318a523b7a75c");
        RegistryHandler.initPlant("INFERNOFRUIT", "地狱果", ChatColor.DARK_RED, PlantType.DOUBLE_PLANT, true, "37faca995aa2bfa391f1c9ddcb20118fdc331bf5c8a5172bb4c7eb038e3d2b2c");
        RegistryHandler.initPlant("ACORN_SQUASH", "异域橡子南瓜", ChatColor.GRAY, PlantType.DOUBLE_PLANT, true, "bde904b116304c3e816b1b8c75c2184260f20591077f63be871bc71675092aa8");
        RegistryHandler.initPlant("ROSE", "异域玫瑰", ChatColor.DARK_RED, PlantType.DOUBLE_PLANT, true, "16fb9b3e3f650b7b258c04ffcb85c1b5dcac92b81e52d992c5124b670fe8d6");
        RegistryHandler.initPlant("SAKURA", "异域樱花", ChatColor.LIGHT_PURPLE, PlantType.DOUBLE_PLANT,  true,"30a39da2c099f7277969184ca32a74a53aea5bc8b645fbdf5f31e5fbba75f844");
        RegistryHandler.initPlant("REED", "异域甘蔗", ChatColor.GREEN, PlantType.FRUIT, true, "8624bacb5f1986e6477abce4ae7dca1820a5260b6233b55ba1d9ba936c84b");
        RegistryHandler.initPlant("ALOE", "异域芦荟", ChatColor.GREEN, PlantType.DOUBLE_PLANT, true, "2e3c538caa4d6e3089ee36f69c958e1ab31859a27221e0cd6173030589f03473");
        RegistryHandler.initPlant("PURPLE_FLOWER", "紫花", ChatColor.LIGHT_PURPLE, PlantType.DOUBLE_PLANT, true, "5fe0588605c62bd5493f4a6dc991033d22f93783554baafb88beb0f709d89594");
        RegistryHandler.initPlant("PURPLE_ROSE", "紫色玫瑰", ChatColor.LIGHT_PURPLE, PlantType.FRUIT, true, "5ef8b12b049423766b9460b9652f8fd8c66528b064130ba01eab819e0a89c269");
        RegistryHandler.initPlant("MANDRAKE_ROOT", "异域曼德拉根", ChatColor.GOLD, PlantType.FRUIT, true, "ef3ee139baa76ec40fa2f5349690268f3508ae7207ffde592705d2fe78f96a7a");
        RegistryHandler.initPlant("DEVIL_MELON", "邪恶瓜", ChatColor.LIGHT_PURPLE, PlantType.FRUIT, true, "fc4182685eeb2ef49221d744d8b64fd5e8f8ef5d92452f27daaaffbaad770e14");

    }

}