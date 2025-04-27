package com.be;

//import com.be.fixes.HeadDropFix;

import com.be.registry.BECommands;
import com.be.registry.BEFoodRegistry;
import com.be.registry.BEItemGroups;
import com.be.registry.BEPlants;
import com.be.registry.BETrees;
import com.be.utils.BEListener;
import com.be.utils.RegistryHandler;
import io.github.thebusybiscuit.exoticgarden.ExoticGarden;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.libraries.paperlib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BEPlugin extends JavaPlugin implements SlimefunAddon {
    private static Config config;
    private static BEPlugin instance;

    public static void start() {
        instance = new BEPlugin();
        instance.onEnable();
    }

    public static void stop() {
        instance.onDisable();
    }

    public static BEPlugin getInstance() {
        return instance;
    }

    public void onEnable() {
        PaperLib.suggestPaper(ExoticGarden.instance);
        instance = this;

        config = new Config(ExoticGarden.instance);
        if (!RegistryHandler.getSchematicsFolder().exists()) {
            RegistryHandler.getSchematicsFolder().mkdirs();
        }

        BEPlants.onPlantsRegister();
        BETrees.onTreesRegister();
        BEFoodRegistry.register(this, BEItemGroups.miscItemGroup, BEItemGroups.drinksItemGroup, BEItemGroups.foodItemGroup);
        //HeadDropFix.onHeadDropFix();
        BECommands.onCommandsRegister();
        Bukkit.getPluginManager().registerEvents(BEListener.getInstance(), ExoticGarden.instance);

        /*
        // Auto Updater
        if (BEPlugin.config.getBoolean("options.auto-update")) {
            PluginUpdater updater = new GitHubBuildsUpdater(this, getFile(), "1798643961/BEPlugin/master");
            updater.start();
        }

         */
        config.save();
    }

    public void onDisable() {
        instance = null;
    }

    @NotNull
    public JavaPlugin getJavaPlugin() {
        return ExoticGarden.instance;
    }

    @Nullable
    public String getBugTrackerURL() {
        return null;
    }
}