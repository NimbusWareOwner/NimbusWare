package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;

public class AutoArmor extends Module {
    public AutoArmor() {
        super("AutoArmor", "Automatically equips armor", Module.Category.COMBAT, 0);
    }
    
    @Override
    protected void onEnable() {}
    
    @Override
    protected void onDisable() {}
}