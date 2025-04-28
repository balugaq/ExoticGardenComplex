package com.be.registry;

import com.be.command.SetSpawnCommand;
import com.be.command.SpawnCommand;
import io.github.thebusybiscuit.exoticgarden.ExoticGarden;

public class BECommands {

    public static void onCommandsRegister() {
        ExoticGarden.getInstance().getCommand("spawn").setExecutor(new SpawnCommand());
        ExoticGarden.getInstance().getCommand("setspawn").setExecutor(new SetSpawnCommand());
    }
}