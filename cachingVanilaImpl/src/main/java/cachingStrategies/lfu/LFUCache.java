package cachingStrategies.lfu;

import cachingStrategies.ICache;

import java.util.HashMap;
import java.util.Map;

/**
 * Least Frequently Used (LFU) Cache implementation using HashMaps.
 *
 * This cache keeps track of key-value pairs and their access frequencies.
 * When the cache reaches its capacity, the entry with the lowest access count
 * (least frequently used) is evicted.
 *
 * @param <K> The type of keys used in the cache.
 * @param <V> The type of values stored in the cache.
 */
public class LFUCache<K, V> implements ICache<K,V> {

    private final int capacity;
    private final Map<K, V> map;  // Stores data
    private final Map<K, Integer> frequencyMap; // Tracks access frequency

    /**
     * Creates a new LFUCache with the specified capacity.
     *
     * @param capacity The maximum number of entries the cache can hold.
     */
    public LFUCache(int capacity) {
        this.capacity = capacity;
        map = new HashMap<>();
        frequencyMap = new HashMap<>();
    }

    /**
     * Retrieves a value from the cache based on its key.
     *
     * If the key exists in the cache, the corresponding value is returned and the access
     * count for the entry is incremented.
     *
     * @param key The key of the data to retrieve.
     * @return The value associated with the key, or null if not found.
     */
    public V get(K key) {
        V value = map.get(key);
        if (value != null) {
            // Update frequency for accessed entry
            int count = frequencyMap.getOrDefault(key, 0);
            frequencyMap.put(key, count + 1);
        }
        return value;
    }

    /**
     * Adds or updates a key-value pair in the cache.
     *
     * If the key already exists, the value is updated and the access count is incremented.
     * If the key is new, a new entry is added and its access count is set to 1.
     * The least frequently used entry is evicted if the capacity is exceeded.
     *
     * @param key The key of the data to store or update.
     * @param value The value to associate with the key.
     */
    public void put(K key, V value) {
        map.put(key, value);
        frequencyMap.put(key, 1); // New entry starts with frequency 1
        evictLeastFrequentlyUsed();
    }

    /**
     * Evicts the least frequently used (LFU) entry from the cache if the map size exceeds capacity.
     *
     * Iterates through the frequency map and finds the entry with the lowest access count.
     * Removes the entry with the lowest access count from both the data map (map) and the frequency map (frequencyMap).
     */
    private void evictLeastFrequentlyUsed() {
        if (map.size() > capacity) {
            K keyToRemove = null;
            int minFrequency = Integer.MAX_VALUE;
            for (Map.Entry<K, Integer> entry : frequencyMap.entrySet()) {
                int frequency = entry.getValue();
                if (frequency < minFrequency) {
                    minFrequency = frequency;
                    keyToRemove = entry.getKey();
                }
            }
            if (keyToRemove != null) {
                map.remove(keyToRemove);
                frequencyMap.remove(keyToRemove);
            }
        }
    }
}

