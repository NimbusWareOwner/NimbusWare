package com.example.nimbusware.features;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AutoClicker module
 */
public class AutoClickerTest {
    
    private AutoClicker autoClicker;
    
    @BeforeEach
    public void setUp() {
        autoClicker = new AutoClicker();
    }
    
    @Test
    public void testInitialState() {
        assertEquals(10, autoClicker.getCps());
        assertTrue(autoClicker.isLeftClick());
        assertFalse(autoClicker.isRightClick());
        assertTrue(autoClicker.isUseFuntimeBypass());
        assertTrue(autoClicker.isTapeMouse());
    }
    
    @Test
    public void testSetCps_ValidRange() {
        autoClicker.setCps(15);
        assertEquals(15, autoClicker.getCps());
    }
    
    @Test
    public void testSetCps_TooLow() {
        autoClicker.setCps(0);
        assertEquals(1, autoClicker.getCps()); // Should clamp to minimum
    }
    
    @Test
    public void testSetCps_TooHigh() {
        autoClicker.setCps(25);
        assertEquals(20, autoClicker.getCps()); // Should clamp to maximum
    }
    
    @Test
    public void testSetLeftClick() {
        autoClicker.setLeftClick(false);
        assertFalse(autoClicker.isLeftClick());
        
        autoClicker.setLeftClick(true);
        assertTrue(autoClicker.isLeftClick());
    }
    
    @Test
    public void testSetRightClick() {
        autoClicker.setRightClick(true);
        assertTrue(autoClicker.isRightClick());
        
        autoClicker.setRightClick(false);
        assertFalse(autoClicker.isRightClick());
    }
    
    @Test
    public void testSetUseFuntimeBypass() {
        autoClicker.setUseFuntimeBypass(false);
        assertFalse(autoClicker.isUseFuntimeBypass());
        
        autoClicker.setUseFuntimeBypass(true);
        assertTrue(autoClicker.isUseFuntimeBypass());
    }
    
    @Test
    public void testSetTapeMouse() {
        autoClicker.setTapeMouse(false);
        assertFalse(autoClicker.isTapeMouse());
        
        autoClicker.setTapeMouse(true);
        assertTrue(autoClicker.isTapeMouse());
    }
    
    @Test
    public void testResetToDefaults() {
        // Change some values
        autoClicker.setCps(15);
        autoClicker.setRightClick(true);
        autoClicker.setUseFuntimeBypass(false);
        
        // Reset
        autoClicker.resetToDefaults();
        
        // Check if reset to defaults
        assertEquals(10, autoClicker.getCps());
        assertTrue(autoClicker.isLeftClick());
        assertFalse(autoClicker.isRightClick());
        assertTrue(autoClicker.isUseFuntimeBypass());
        assertTrue(autoClicker.isTapeMouse());
    }
    
    @Test
    public void testGetClickRate() {
        autoClicker.setCps(12);
        assertEquals(12.0, autoClicker.getClickRate());
    }
    
    @Test
    public void testIsAnyClickEnabled_LeftOnly() {
        autoClicker.setLeftClick(true);
        autoClicker.setRightClick(false);
        assertTrue(autoClicker.isAnyClickEnabled());
    }
    
    @Test
    public void testIsAnyClickEnabled_RightOnly() {
        autoClicker.setLeftClick(false);
        autoClicker.setRightClick(true);
        assertTrue(autoClicker.isAnyClickEnabled());
    }
    
    @Test
    public void testIsAnyClickEnabled_Both() {
        autoClicker.setLeftClick(true);
        autoClicker.setRightClick(true);
        assertTrue(autoClicker.isAnyClickEnabled());
    }
    
    @Test
    public void testIsAnyClickEnabled_None() {
        autoClicker.setLeftClick(false);
        autoClicker.setRightClick(false);
        assertFalse(autoClicker.isAnyClickEnabled());
    }
    
    @Test
    public void testModuleProperties() {
        assertEquals("AutoClicker", autoClicker.getName());
        assertEquals("Automatically clicks with tape mouse", autoClicker.getDescription());
        assertEquals(com.example.nimbusware.core.Module.Category.COMBAT, autoClicker.getCategory());
        assertEquals(0, autoClicker.getKeyBind());
        assertFalse(autoClicker.isEnabled());
        assertTrue(autoClicker.isVisible());
    }
}