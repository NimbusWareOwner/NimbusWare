package com.example.nimbusware.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ValidationUtils
 */
public class ValidationUtilsTest {
    
    @Test
    public void testValidateNotNullOrEmpty_ValidString() {
        // Should not throw exception
        assertDoesNotThrow(() -> ValidationUtils.validateNotNullOrEmpty("valid", "test"));
    }
    
    @Test
    public void testValidateNotNullOrEmpty_NullString() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ValidationUtils.validateNotNullOrEmpty(null, "test")
        );
        assertTrue(exception.getMessage().contains("cannot be null or empty"));
    }
    
    @Test
    public void testValidateNotNullOrEmpty_EmptyString() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ValidationUtils.validateNotNullOrEmpty("", "test")
        );
        assertTrue(exception.getMessage().contains("cannot be null or empty"));
    }
    
    @Test
    public void testValidateRange_ValidInt() {
        assertDoesNotThrow(() -> ValidationUtils.validateRange(5, 1, 10, "test"));
    }
    
    @Test
    public void testValidateRange_IntTooLow() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ValidationUtils.validateRange(0, 1, 10, "test")
        );
        assertTrue(exception.getMessage().contains("must be between 1 and 10"));
    }
    
    @Test
    public void testValidateRange_IntTooHigh() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ValidationUtils.validateRange(11, 1, 10, "test")
        );
        assertTrue(exception.getMessage().contains("must be between 1 and 10"));
    }
    
    @Test
    public void testValidateRange_ValidDouble() {
        assertDoesNotThrow(() -> ValidationUtils.validateRange(5.5, 1.0, 10.0, "test"));
    }
    
    @Test
    public void testValidatePositive_ValidValue() {
        assertDoesNotThrow(() -> ValidationUtils.validatePositive(5.0, "test"));
    }
    
    @Test
    public void testValidatePositive_Zero() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ValidationUtils.validatePositive(0.0, "test")
        );
        assertTrue(exception.getMessage().contains("must be positive"));
    }
    
    @Test
    public void testValidatePositive_Negative() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ValidationUtils.validatePositive(-1.0, "test")
        );
        assertTrue(exception.getMessage().contains("must be positive"));
    }
    
    @Test
    public void testValidateEmail_ValidEmail() {
        assertDoesNotThrow(() -> ValidationUtils.validateEmail("test@example.com"));
    }
    
    @Test
    public void testValidateEmail_InvalidEmail() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ValidationUtils.validateEmail("invalid-email")
        );
        assertTrue(exception.getMessage().contains("Invalid email format"));
    }
    
    @Test
    public void testValidateIPAddress_ValidIP() {
        assertDoesNotThrow(() -> ValidationUtils.validateIPAddress("192.168.1.1"));
    }
    
    @Test
    public void testValidateIPAddress_InvalidIP() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ValidationUtils.validateIPAddress("999.999.999.999")
        );
        assertTrue(exception.getMessage().contains("Invalid IP address format"));
    }
    
    @Test
    public void testValidateAlphanumeric_ValidString() {
        assertDoesNotThrow(() -> ValidationUtils.validateAlphanumeric("abc123", "test"));
    }
    
    @Test
    public void testValidateAlphanumeric_InvalidString() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ValidationUtils.validateAlphanumeric("abc-123", "test")
        );
        assertTrue(exception.getMessage().contains("must contain only alphanumeric characters"));
    }
    
    @Test
    public void testValidateLength_ValidLength() {
        assertDoesNotThrow(() -> ValidationUtils.validateLength("hello", 3, 10, "test"));
    }
    
    @Test
    public void testValidateLength_TooShort() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ValidationUtils.validateLength("hi", 3, 10, "test")
        );
        assertTrue(exception.getMessage().contains("length must be between 3 and 10"));
    }
    
    @Test
    public void testValidateLength_TooLong() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ValidationUtils.validateLength("hello world", 3, 10, "test")
        );
        assertTrue(exception.getMessage().contains("length must be between 3 and 10"));
    }
    
    @Test
    public void testValidateNotNull_ValidObject() {
        assertDoesNotThrow(() -> ValidationUtils.validateNotNull("test", "test"));
    }
    
    @Test
    public void testValidateNotNull_NullObject() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ValidationUtils.validateNotNull(null, "test")
        );
        assertTrue(exception.getMessage().contains("cannot be null"));
    }
    
    @Test
    public void testValidateCondition_True() {
        assertDoesNotThrow(() -> ValidationUtils.validateCondition(true, "test"));
    }
    
    @Test
    public void testValidateCondition_False() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ValidationUtils.validateCondition(false, "test message")
        );
        assertEquals("test message", exception.getMessage());
    }
}