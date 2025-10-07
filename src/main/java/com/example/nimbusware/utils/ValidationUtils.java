package com.example.nimbusware.utils;

import java.util.regex.Pattern;

/**
 * Utility class for input validation
 */
public class ValidationUtils {
    
    // Common validation patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    private static final Pattern IP_PATTERN = Pattern.compile(
        "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
    );
    
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");
    
    /**
     * Validate that a string is not null or empty
     * @param value String to validate
     * @param fieldName Name of the field for error messages
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateNotNullOrEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
    }
    
    /**
     * Validate that a number is within a specified range
     * @param value Number to validate
     * @param min Minimum allowed value (inclusive)
     * @param max Maximum allowed value (inclusive)
     * @param fieldName Name of the field for error messages
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateRange(double value, double min, double max, String fieldName) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                String.format("%s must be between %.2f and %.2f, got %.2f", fieldName, min, max, value)
            );
        }
    }
    
    /**
     * Validate that an integer is within a specified range
     * @param value Integer to validate
     * @param min Minimum allowed value (inclusive)
     * @param max Maximum allowed value (inclusive)
     * @param fieldName Name of the field for error messages
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateRange(int value, int min, int max, String fieldName) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                String.format("%s must be between %d and %d, got %d", fieldName, min, max, value)
            );
        }
    }
    
    /**
     * Validate that a value is positive
     * @param value Value to validate
     * @param fieldName Name of the field for error messages
     * @throws IllegalArgumentException if validation fails
     */
    public static void validatePositive(double value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be positive, got " + value);
        }
    }
    
    /**
     * Validate that a value is non-negative
     * @param value Value to validate
     * @param fieldName Name of the field for error messages
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateNonNegative(double value, String fieldName) {
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + " must be non-negative, got " + value);
        }
    }
    
    /**
     * Validate email format
     * @param email Email to validate
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateEmail(String email) {
        validateNotNullOrEmpty(email, "Email");
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }
    
    /**
     * Validate IP address format
     * @param ip IP address to validate
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateIPAddress(String ip) {
        validateNotNullOrEmpty(ip, "IP Address");
        if (!IP_PATTERN.matcher(ip).matches()) {
            throw new IllegalArgumentException("Invalid IP address format: " + ip);
        }
    }
    
    /**
     * Validate that a string contains only alphanumeric characters
     * @param value String to validate
     * @param fieldName Name of the field for error messages
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateAlphanumeric(String value, String fieldName) {
        validateNotNullOrEmpty(value, fieldName);
        if (!ALPHANUMERIC_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(fieldName + " must contain only alphanumeric characters: " + value);
        }
    }
    
    /**
     * Validate that a string length is within bounds
     * @param value String to validate
     * @param minLength Minimum length (inclusive)
     * @param maxLength Maximum length (inclusive)
     * @param fieldName Name of the field for error messages
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateLength(String value, int minLength, int maxLength, String fieldName) {
        validateNotNullOrEmpty(value, fieldName);
        if (value.length() < minLength || value.length() > maxLength) {
            throw new IllegalArgumentException(
                String.format("%s length must be between %d and %d characters, got %d", 
                    fieldName, minLength, maxLength, value.length())
            );
        }
    }
    
    /**
     * Validate that an object is not null
     * @param value Object to validate
     * @param fieldName Name of the field for error messages
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }
    
    /**
     * Validate that a boolean condition is true
     * @param condition Condition to validate
     * @param message Error message if validation fails
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateCondition(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
}