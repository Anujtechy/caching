import cachingStrategies.ICache;
import cachingStrategies.lfu.LFUCache;
import cachingStrategies.lru.ILRUCache;
import cachingStrategies.lru.LRUCacheUsingHashMap;

public class CacheClient {
    public static void main(String[] args) {

        ILRUCache<String, String> lruCache = new LRUCacheUsingHashMap<>(3);

        // Store some key-value pairs
        lruCache.put("key1", "value1");
        lruCache.put("key2", "value2");
        lruCache.put("key3", "value3");

        // Access a key (moves it to the most recently used position)
        String value = lruCache.get("key1");
        System.out.println("Retrieved value for key1: " + value);

        // Add a new key-value pair (may evict the least recently used if full)
        lruCache.put("key4", "value4");

        // Check if a key exists
        if (lruCache.get("key2") != null) {
            System.out.println("key2 is still in the cache");
        } else {
            System.out.println("key2 was evicted from the cache");
        }

        System.out.println();

        // Create an LFU cache with capacity 3
        ICache<String, Integer> lfuCache = new LFUCache<>(3);

        // Put some key-value pairs in the cache
        lfuCache.put("a", 1);
        lfuCache.put("b", 2);
        lfuCache.put("c", 3);

        // Access some entries to update their access frequencies
        System.out.println(lfuCache.get("a")); // Access "a"
        System.out.println(lfuCache.get("b")); // Access "b"
        lfuCache.put("d", 4); // Add a new entry "d"

        // Try to access a non-existent key
        System.out.println(lfuCache.get("c")); // Should be null
    }
}
