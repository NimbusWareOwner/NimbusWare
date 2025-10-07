package com.example.nimbusware.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Simple cache manager with TTL support
 * @param <K> Key type
 * @param <V> Value type
 */
public class CacheManager<K, V> {
    private final ConcurrentHashMap<K, CacheEntry<V>> cache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleanupExecutor;
    private final long defaultTtlMillis;
    
    public CacheManager(long defaultTtlMillis) {
        this.defaultTtlMillis = defaultTtlMillis;
        this.cleanupExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "CacheCleanup-Thread");
            t.setDaemon(true);
            return t;
        });
        
        // Start cleanup task
        cleanupExecutor.scheduleAtFixedRate(this::cleanup, 1, 1, TimeUnit.MINUTES);
    }
    
    /**
     * Get value from cache, computing if not present
     * @param key Cache key
     * @param valueFunction Function to compute value if not in cache
     * @return Cached or computed value
     */
    public V get(K key, Function<K, V> valueFunction) {
        return get(key, valueFunction, defaultTtlMillis);
    }
    
    /**
     * Get value from cache with custom TTL
     * @param key Cache key
     * @param valueFunction Function to compute value if not in cache
     * @param ttlMillis TTL in milliseconds
     * @return Cached or computed value
     */
    public V get(K key, Function<K, V> valueFunction, long ttlMillis) {
        CacheEntry<V> entry = cache.get(key);
        
        if (entry != null && !entry.isExpired()) {
            return entry.getValue();
        }
        
        // Compute new value
        V value = valueFunction.apply(key);
        cache.put(key, new CacheEntry<>(value, System.currentTimeMillis() + ttlMillis));
        return value;
    }
    
    /**
     * Put value in cache
     * @param key Cache key
     * @param value Value to cache
     */
    public void put(K key, V value) {
        put(key, value, defaultTtlMillis);
    }
    
    /**
     * Put value in cache with custom TTL
     * @param key Cache key
     * @param value Value to cache
     * @param ttlMillis TTL in milliseconds
     */
    public void put(K key, V value, long ttlMillis) {
        cache.put(key, new CacheEntry<>(value, System.currentTimeMillis() + ttlMillis));
    }
    
    /**
     * Remove value from cache
     * @param key Cache key
     * @return Removed value or null
     */
    public V remove(K key) {
        CacheEntry<V> entry = cache.remove(key);
        return entry != null ? entry.getValue() : null;
    }
    
    /**
     * Clear all cached values
     */
    public void clear() {
        cache.clear();
    }
    
    /**
     * Get cache size
     * @return Number of cached entries
     */
    public int size() {
        return cache.size();
    }
    
    /**
     * Check if key exists in cache
     * @param key Cache key
     * @return true if key exists and is not expired
     */
    public boolean containsKey(K key) {
        CacheEntry<V> entry = cache.get(key);
        return entry != null && !entry.isExpired();
    }
    
    private void cleanup() {
        long now = System.currentTimeMillis();
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired(now));
    }
    
    /**
     * Shutdown the cache manager
     */
    public void shutdown() {
        cleanupExecutor.shutdown();
        try {
            if (!cleanupExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                cleanupExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            cleanupExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    private static class CacheEntry<V> {
        private final V value;
        private final long expiryTime;
        
        public CacheEntry(V value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }
        
        public V getValue() {
            return value;
        }
        
        public boolean isExpired() {
            return isExpired(System.currentTimeMillis());
        }
        
        public boolean isExpired(long now) {
            return now > expiryTime;
        }
    }
}