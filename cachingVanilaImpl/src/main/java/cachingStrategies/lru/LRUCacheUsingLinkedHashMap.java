package cachingStrategies.lru;

import cachingStrategies.lru.ILRUCache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU (Least Recently Used) Cache implementation using a LinkedHashMap.
 *
 * This cache keeps track of the most recently accessed entries and evicts the least recently used
 * entries when the cache reaches its capacity. LinkedHashMap with access order as `true`
 * automatically maintains the least recently used order.
 *
 * @param <K> The type of keys used in the cache.
 * @param <V> The type of values stored in the cache.
 */
public class LRUCacheUsingLinkedHashMap<K, V> implements ILRUCache<K, V> {
    private final int capacity;
    private final LinkedHashMap<K, V> map; // Use LinkedHashMap with access order

    /**
     * Creates a new LRUCache with the specified capacity.
     *
     * @param capacity The maximum number of entries the cache can hold.
     */
    public LRUCacheUsingLinkedHashMap(int capacity) {
        this.capacity = capacity;
        map = new LinkedHashMap<>(capacity, 0.75f, true); // Access order, load factor, access order as true
    }

    /**
     * Retrieves a value from the cache based on its key.
     *
     * If the key exists in the cache, the corresponding value is returned and the accessed entry is
     * automatically moved to the front of the LinkedHashMap (most recently used).
     *
     * @param key The key of the data to retrieve.
     * @return The value associated with the key, or null if not found.
     */
    public V get(K key) {
        return map.get(key);
    }

    /**
     * Adds or updates a key-value pair in the cache.
     *
     * If the key already exists, the value is updated. If the key is new, a new entry is added
     * to the LinkedHashMap. The least recently used entry (eldest) will be automatically evicted
     * when the capacity is reached.
     *
     * @param key The key of the data to store or update.
     * @param value The value to associate with the key.
     */
    public void put(K key, V value) {
        map.put(key, value);
        evictLeastRecentlyUsed(); // Evict if capacity is exceeded
    }

    /**
     * Evicts the least recently used (LRU) entry from the cache if the map size exceeds capacity.
     */
    private void evictLeastRecentlyUsed() {
        if (map.size() > capacity) {
            Map.Entry<K, V> eldestEntry = map.entrySet().iterator().next(); // Get the eldest entry
            K keyToRemove = eldestEntry.getKey();
            map.remove(keyToRemove);
        }
    }

}
