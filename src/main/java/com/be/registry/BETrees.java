package com.be.registry;

import org.bukkit.Color;
import org.bukkit.Material;

import com.be.utils.RegistryHandler;

public class BETrees {

    public static void onTreesRegister() {
        RegistryHandler.initTree("BREADFRUIT", "异域面包果", "aaa139ecc894c4e455825e313b542e2068601f2f31ab26d30cf276d51345bf3b", "&2", Color.GREEN, "异域面包果汁", true, Material.DIRT, Material.GRASS_BLOCK);
        RegistryHandler.initTree("ALIEN_FRUIT", "外星果实", "d073ba0ad38816369c7ffffcfa9ecc3115159c314b55ad7b5cbc5076a1ee5ebf", "&2", Color.PURPLE, "外星果汁", true, Material.DIRT, Material.GRASS_BLOCK);
        RegistryHandler.initTree("PUNCH_FRUIT", "潘趣果实", "ad1be3c159a5118f6e0adba3b7c33d72e4055524090c3a1d37b2b4e7be6bc375", "&2", Color.BLUE, "潘趣果汁", true, Material.DIRT, Material.GRASS_BLOCK);
        RegistryHandler.initTree("JACKFRUIT", "异域菠萝蜜", "a1f74dca0952024e13e8115499e01b1ac97e25936c2df6da092c3b1818962962", "&2", Color.YELLOW, "异域菠萝蜜汁", true, Material.DIRT, Material.GRASS_BLOCK);
    }
}