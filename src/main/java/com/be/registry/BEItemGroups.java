package com.be.registry;

import com.be.BEPlugin;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.NestedItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerHead;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerSkin;
import org.bukkit.NamespacedKey;

public class BEItemGroups {

    public static final NestedItemGroup nestedItemGroup;
    public static final ItemGroup mainItemGroup;
    public static final ItemGroup miscItemGroup;
    public static final ItemGroup foodItemGroup;
    public static final ItemGroup drinksItemGroup;

    static {
        nestedItemGroup = new NestedItemGroup(new NamespacedKey(BEPlugin.getInstance(), "parent_category"), new CustomItemStack(PlayerHead.getItemStack(PlayerSkin.fromHashCode("2448c183a7640867e42118e69c3f4d15db1ffb0d93646b77078ecedca2a43454")), "&aBE拓展"));
        mainItemGroup = new SubItemGroup(new NamespacedKey(BEPlugin.getInstance(), "plants_and_fruits"), nestedItemGroup, new CustomItemStack(PlayerHead.getItemStack(PlayerSkin.fromHashCode("aaa139ecc894c4e455825e313b542e2068601f2f31ab26d30cf276d51345bf3b")), "&aBE拓展 - 植物和水果"));
        miscItemGroup = new SubItemGroup(new NamespacedKey(BEPlugin.getInstance(), "misc"), nestedItemGroup, new CustomItemStack(PlayerHead.getItemStack(PlayerSkin.fromHashCode("d073ba0ad38816369c7ffffcfa9ecc3115159c314b55ad7b5cbc5076a1ee5ebf")), "&aBE拓展 - 配料和工具"));
        foodItemGroup = new SubItemGroup(new NamespacedKey(BEPlugin.getInstance(), "food"), nestedItemGroup, new CustomItemStack(PlayerHead.getItemStack(PlayerSkin.fromHashCode("ad1be3c159a5118f6e0adba3b7c33d72e4055524090c3a1d37b2b4e7be6bc375")), "&aBE拓展 - 食物"));
        drinksItemGroup = new SubItemGroup(new NamespacedKey(BEPlugin.getInstance(), "drinks"), nestedItemGroup, new CustomItemStack(PlayerHead.getItemStack(PlayerSkin.fromHashCode("2a8f1f70e85825607d28edce1a2ad4506e732b4a5345a5ea6e807c4b313e88")), "&aBE拓展 - 饮料"));
    }

}