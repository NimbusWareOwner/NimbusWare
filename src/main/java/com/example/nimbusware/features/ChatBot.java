package com.example.nimbusware.features;

import com.example.nimbusware.core.Module;

public class ChatBot extends Module {
    public ChatBot() {
        super("ChatBot", "Automatically responds to chat", Module.Category.MISC, 0);
    }
    
    @Override
    protected void onEnable() {
        System.out.println("ChatBot enabled");
    }
    
    @Override
    protected void onDisable() {
        System.out.println("ChatBot disabled");
    }
}