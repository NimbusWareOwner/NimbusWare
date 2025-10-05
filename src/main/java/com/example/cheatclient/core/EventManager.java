package com.example.cheatclient.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private final List<Object> listeners = new CopyOnWriteArrayList<>();
    
    public void register(Object listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public void unregister(Object listener) {
        listeners.remove(listener);
    }
    
    public void post(Event event) {
        for (Object listener : listeners) {
            try {
                event.call(listener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static class Event {
        public void call(Object listener) {
            // Override in subclasses
        }
    }
    
    public static class TickEvent extends Event {
        @Override
        public void call(Object listener) {
            if (listener instanceof TickListener) {
                ((TickListener) listener).onTick();
            }
        }
    }
    
    public static class RenderEvent extends Event {
        private final float partialTicks;
        
        public RenderEvent(float partialTicks) {
            this.partialTicks = partialTicks;
        }
        
        public float getPartialTicks() {
            return partialTicks;
        }
        
        @Override
        public void call(Object listener) {
            if (listener instanceof RenderListener) {
                ((RenderListener) listener).onRender(partialTicks);
            }
        }
    }
    
    public static class KeyEvent extends Event {
        private final int key;
        private final int action;
        
        public KeyEvent(int key, int action) {
            this.key = key;
            this.action = action;
        }
        
        public int getKey() {
            return key;
        }
        
        public int getAction() {
            return action;
        }
        
        @Override
        public void call(Object listener) {
            if (listener instanceof KeyListener) {
                ((KeyListener) listener).onKey(key, action);
            }
        }
    }
    
    public interface TickListener {
        void onTick();
    }
    
    public interface RenderListener {
        void onRender(float partialTicks);
    }
    
    public interface KeyListener {
        void onKey(int key, int action);
    }
}