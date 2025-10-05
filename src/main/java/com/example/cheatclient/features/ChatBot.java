package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;

public class ChatBot extends Module {
    public ChatBot() {
        super("ChatBot", "Automatically responds to chat", Module.Category.MISC, 0);
    }
    
    @Override
    protected void onEnable() {}
    
    @Override
    protected void onDisable() {}
}