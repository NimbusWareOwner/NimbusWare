package com.example.nimbusware.core;

import com.example.nimbusware.NimbusWare;
import com.example.nimbusware.utils.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DragDropManager {
    private static DragDropManager instance;
    private NimbusWare client;
    private Map<String, DraggableElement> draggableElements = new ConcurrentHashMap<>();
    private String draggedElement = null;
    private boolean isDragging = false;
    private float dragStartX = 0;
    private float dragStartY = 0;
    private float currentX = 0;
    private float currentY = 0;
    
    private DragDropManager(NimbusWare client) {
        this.client = client;
        initializeDraggableElements();
    }
    
    public static DragDropManager getInstance(NimbusWare client) {
        if (instance == null) {
            instance = new DragDropManager(client);
        }
        return instance;
    }
    
    private void initializeDraggableElements() {
        // Register draggable HUD elements
        registerDraggableElement("target_hud", "Target HUD", 10, 10, 200, 150);
        registerDraggableElement("fps", "FPS Counter", 10, 170, 100, 30);
        registerDraggableElement("ping", "Ping Display", 120, 170, 100, 30);
        registerDraggableElement("coordinates", "Coordinates", 10, 210, 150, 30);
        registerDraggableElement("biome", "Biome Info", 170, 210, 120, 30);
        registerDraggableElement("time", "Time Display", 10, 250, 100, 30);
        registerDraggableElement("speed", "Speed Meter", 120, 250, 100, 30);
        registerDraggableElement("direction", "Direction", 230, 250, 100, 30);
        registerDraggableElement("health", "Health Bar", 10, 290, 150, 30);
        registerDraggableElement("armor", "Armor Bar", 170, 290, 150, 30);
        registerDraggableElement("experience", "Experience", 10, 330, 150, 30);
        registerDraggableElement("modules", "Module List", 10, 370, 200, 100);
        registerDraggableElement("watermark", "Watermark", 10, 480, 200, 30);
        
        Logger.info("DragDropManager initialized with " + draggableElements.size() + " draggable elements");
    }
    
    public void registerDraggableElement(String id, String name, float x, float y, float width, float height) {
        DraggableElement element = new DraggableElement(id, name, x, y, width, height);
        draggableElements.put(id, element);
        Logger.debug("Registered draggable element: " + name + " at (" + x + ", " + y + ")");
    }
    
    public void startDrag(String elementId, float mouseX, float mouseY) {
        if (!draggableElements.containsKey(elementId)) {
            Logger.warn("Cannot drag unknown element: " + elementId);
            return;
        }
        
        draggedElement = elementId;
        isDragging = true;
        dragStartX = mouseX;
        dragStartY = mouseY;
        
        DraggableElement element = draggableElements.get(elementId);
        currentX = element.getX();
        currentY = element.getY();
        
        Logger.debug("Started dragging " + element.getName());
    }
    
    public void updateDrag(float mouseX, float mouseY) {
        if (!isDragging || draggedElement == null) return;
        
        float deltaX = mouseX - dragStartX;
        float deltaY = mouseY - dragStartY;
        
        currentX = draggableElements.get(draggedElement).getX() + deltaX;
        currentY = draggableElements.get(draggedElement).getY() + deltaY;
        
        // Constrain to screen bounds
        currentX = Math.max(0, Math.min(currentX, 800 - draggableElements.get(draggedElement).getWidth()));
        currentY = Math.max(0, Math.min(currentY, 600 - draggableElements.get(draggedElement).getHeight()));
    }
    
    public void endDrag() {
        if (!isDragging || draggedElement == null) return;
        
        DraggableElement element = draggableElements.get(draggedElement);
        element.setPosition(currentX, currentY);
        
        Logger.debug("Finished dragging " + element.getName() + " to (" + currentX + ", " + currentY + ")");
        
        // Save position to config
        saveElementPosition(draggedElement, currentX, currentY);
        
        isDragging = false;
        draggedElement = null;
    }
    
    public void cancelDrag() {
        if (!isDragging || draggedElement == null) return;
        
        Logger.debug("Cancelled dragging " + draggableElements.get(draggedElement).getName());
        
        isDragging = false;
        draggedElement = null;
    }
    
    public boolean isDragging() {
        return isDragging;
    }
    
    public String getDraggedElement() {
        return draggedElement;
    }
    
    public DraggableElement getElement(String id) {
        return draggableElements.get(id);
    }
    
    public Map<String, DraggableElement> getAllElements() {
        return new HashMap<>(draggableElements);
    }
    
    public void resetElementPosition(String elementId) {
        DraggableElement element = draggableElements.get(elementId);
        if (element != null) {
            element.resetPosition();
            saveElementPosition(elementId, element.getX(), element.getY());
            Logger.info("Reset position for " + element.getName());
        }
    }
    
    public void resetAllPositions() {
        for (DraggableElement element : draggableElements.values()) {
            element.resetPosition();
        }
        Logger.info("Reset all element positions");
    }
    
    public void snapToGrid(String elementId, float gridSize) {
        DraggableElement element = draggableElements.get(elementId);
        if (element != null) {
            float snappedX = Math.round(element.getX() / gridSize) * gridSize;
            float snappedY = Math.round(element.getY() / gridSize) * gridSize;
            element.setPosition(snappedX, snappedY);
            saveElementPosition(elementId, snappedX, snappedY);
            Logger.debug("Snapped " + element.getName() + " to grid at (" + snappedX + ", " + snappedY + ")");
        }
    }
    
    public void snapAllToGrid(float gridSize) {
        for (Map.Entry<String, DraggableElement> entry : draggableElements.entrySet()) {
            snapToGrid(entry.getKey(), gridSize);
        }
        Logger.info("Snapped all elements to grid");
    }
    
    private void saveElementPosition(String elementId, float x, float y) {
        if (client.getConfigManager() != null) {
            // Save to config
            Logger.debug("Saved position for " + elementId + ": (" + x + ", " + y + ")");
        }
    }
    
    public void loadElementPositions() {
        // Load positions from config
        Logger.debug("Loading element positions from config");
    }
    
    public static class DraggableElement {
        private String id;
        private String name;
        private float x, y;
        private float width, height;
        private float originalX, originalY;
        private boolean visible = true;
        private boolean locked = false;
        
        public DraggableElement(String id, String name, float x, float y, float width, float height) {
            this.id = id;
            this.name = name;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.originalX = x;
            this.originalY = y;
        }
        
        public void setPosition(float x, float y) {
            if (!locked) {
                this.x = x;
                this.y = y;
            }
        }
        
        public void resetPosition() {
            this.x = originalX;
            this.y = originalY;
        }
        
        public boolean isPointInside(float pointX, float pointY) {
            return pointX >= x && pointX <= x + width && pointY >= y && pointY <= y + height;
        }
        
        // Getters and Setters
        public String getId() { return id; }
        public String getName() { return name; }
        public float getX() { return x; }
        public float getY() { return y; }
        public float getWidth() { return width; }
        public float getHeight() { return height; }
        public boolean isVisible() { return visible; }
        public void setVisible(boolean visible) { this.visible = visible; }
        public boolean isLocked() { return locked; }
        public void setLocked(boolean locked) { this.locked = locked; }
    }
}