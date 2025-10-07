package com.example.nimbusware.config;

import com.example.nimbusware.utils.ValidationUtils;

/**
 * Represents a configuration property with validation
 * @param <T> Type of the property value
 */
public class ConfigProperty<T> {
    private final String key;
    private final T defaultValue;
    private final PropertyValidator<T> validator;
    private T value;
    
    public ConfigProperty(String key, T defaultValue, PropertyValidator<T> validator) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.validator = validator;
        this.value = defaultValue;
    }
    
    public ConfigProperty(String key, T defaultValue) {
        this(key, defaultValue, null);
    }
    
    /**
     * Get the property value
     * @return Current value or default if not set
     */
    public T getValue() {
        return value != null ? value : defaultValue;
    }
    
    /**
     * Set the property value with validation
     * @param newValue New value to set
     * @throws IllegalArgumentException if validation fails
     */
    public void setValue(T newValue) {
        if (validator != null) {
            validator.validate(newValue);
        }
        this.value = newValue;
    }
    
    /**
     * Get the property key
     * @return Configuration key
     */
    public String getKey() {
        return key;
    }
    
    /**
     * Get the default value
     * @return Default value
     */
    public T getDefaultValue() {
        return defaultValue;
    }
    
    /**
     * Reset to default value
     */
    public void reset() {
        this.value = defaultValue;
    }
    
    /**
     * Check if value has been set (not default)
     * @return true if custom value is set
     */
    public boolean isSet() {
        return value != null;
    }
    
    @Override
    public String toString() {
        return key + "=" + getValue();
    }
    
    /**
     * Functional interface for property validation
     * @param <T> Type of the property value
     */
    @FunctionalInterface
    public interface PropertyValidator<T> {
        void validate(T value) throws IllegalArgumentException;
    }
    
    // Common validators
    public static class Validators {
        public static PropertyValidator<Integer> range(int min, int max) {
            return value -> ValidationUtils.validateRange(value, min, max, "Property");
        }
        
        public static PropertyValidator<Double> range(double min, double max) {
            return value -> ValidationUtils.validateRange(value, min, max, "Property");
        }
        
        public static PropertyValidator<String> notNullOrEmpty() {
            return value -> ValidationUtils.validateNotNullOrEmpty(value, "Property");
        }
        
        public static PropertyValidator<Integer> positive() {
            return value -> ValidationUtils.validatePositive(value, "Property");
        }
        
        public static PropertyValidator<Double> positiveDouble() {
            return value -> ValidationUtils.validatePositive(value, "Property");
        }
    }
}