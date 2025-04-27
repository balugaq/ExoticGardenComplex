package io.github.thebusybiscuit.exoticgarden;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PlayerAlcohol {
    final String player;
    int alcohol;
    boolean isDrunk;

    public PlayerAlcohol(String player, int alcohol) {
        this.player = player;
        this.alcohol = alcohol;
        this.isDrunk = false;
        try {
            YamlConfiguration storge = ExoticGarden.instance.getYamlStorge();
            ConfigurationSection section = storge.createSection("Players");
            section.set(player + ".Alcohol", 0);
            section.set(player + ".Drunk", Boolean.FALSE);
            storge.save(new File(ExoticGarden.instance.getDataFolder() + File.separator + "storge.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PlayerAlcohol(String player, int alcohol, boolean isDrunk) {
        this.player = player;
        this.alcohol = alcohol;
        this.isDrunk = isDrunk;
    }

    public int getAlcohol() {
        return this.alcohol;
    }

    public void setAlcohol(int alcohol) {
        if (alcohol < 0) {
            this.alcohol = 0;
            return;
        }
        this.alcohol = alcohol;
    }

    public void addAlcohol(int alcohol) {
        int result = this.alcohol + alcohol;
        if (result < 0) {
            this.alcohol = 0;
            return;
        }
        this.alcohol = result;
    }

    public boolean isDrunk() {
        return this.isDrunk;
    }

    public void setDrunk(boolean drunk) {
        this.isDrunk = drunk;
    }

    public String getPlayer() {
        return this.player;
    }

    public boolean check() {
        if (this.alcohol >= 100) {
            if (!this.isDrunk) {
                this.isDrunk = true;
            }
            return true;
        }
        return false;
    }
}


