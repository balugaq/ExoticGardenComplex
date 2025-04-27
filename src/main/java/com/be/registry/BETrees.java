package com.be.registry;

import com.be.utils.RegistryHandler;
import org.bukkit.Color;
import org.bukkit.Material;

public class BETrees {

    public static void onTreesRegister() {
        RegistryHandler.initTree("BREADFRUIT", "面包果", "aaa139ecc894c4e455825e313b542e2068601f2f31ab26d30cf276d51345bf3b", "&2", Color.GREEN, "面包果汁", true, Material.DIRT, Material.GRASS_BLOCK);
        RegistryHandler.initTree("ALIEN_FRUIT", "外星果实", "d073ba0ad38816369c7ffffcfa9ecc3115159c314b55ad7b5cbc5076a1ee5ebf", "&2", Color.PURPLE, "外星果汁", true, Material.DIRT, Material.GRASS_BLOCK);
        RegistryHandler.initTree("PUNCH_FRUIT", "潘趣果实", "ad1be3c159a5118f6e0adba3b7c33d72e4055524090c3a1d37b2b4e7be6bc375", "&2", Color.BLUE, "潘趣果汁", true, Material.DIRT, Material.GRASS_BLOCK);
        RegistryHandler.initTree("JACKFRUIT", "菠萝蜜", "44ba890fa8d8684c5119cf1b4b9d5460f5eff392e26ce68b3434e52d18fc666", "&2", Color.YELLOW, "菠萝蜜汁", true, Material.DIRT, Material.GRASS_BLOCK);
    }
}