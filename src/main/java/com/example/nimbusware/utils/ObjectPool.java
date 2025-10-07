package com.example.nimbusware.utils;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

/**
 * Generic object pool for reusing objects to reduce garbage collection
 * @param <T> Type of objects to pool
 */
public class ObjectPool<T> {
    private final ConcurrentLinkedQueue<T> pool = new ConcurrentLinkedQueue<>();
    private final Supplier<T> objectFactory;
    private final int maxSize;
    private volatile int currentSize = 0;
    
    /**
     * Create a new object pool
     * @param objectFactory Factory function to create new objects
     * @param maxSize Maximum number of objects to keep in the pool
     */
    public ObjectPool(Supplier<T> objectFactory, int maxSize) {
        this.objectFactory = objectFactory;
        this.maxSize = maxSize;
    }
    
    /**
     * Get an object from the pool, creating a new one if necessary
     * @return Object from the pool
     */
    public T acquire() {
        T object = pool.poll();
        if (object == null) {
            object = objectFactory.get();
        } else {
            currentSize--;
        }
        return object;
    }
    
    /**
     * Return an object to the pool for reuse
     * @param object Object to return to the pool
     */
    public void release(T object) {
        if (object != null && currentSize < maxSize) {
            pool.offer(object);
            currentSize++;
        }
    }
    
    /**
     * Get the current number of objects in the pool
     * @return Current pool size
     */
    public int size() {
        return currentSize;
    }
    
    /**
     * Clear all objects from the pool
     */
    public void clear() {
        pool.clear();
        currentSize = 0;
    }
}