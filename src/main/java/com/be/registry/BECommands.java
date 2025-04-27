package com.be.registry;

import com.be.BEPlugin;
import com.be.command.SetSpawnCommand;
import com.be.command.SpawnCommand;

public class BECommands {

    public static void onCommandsRegister() {
        BEPlugin.getInstance().getCommand("spawn").setExecutor(new SpawnCommand());
        BEPlugin.getInstance().getCommand("setspawn").setExecutor(new SetSpawnCommand());
    }
}