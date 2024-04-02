import cachingStrategies.lru.ILRUCache;
import cachingStrategies.lru.LRUCacheUsingHashMap;

public class CacheClient {
    public static void main(String[] args) {

        ILRUCache<String, String> cache = new LRUCacheUsingHashMap<>(3);

        // Store some key-value pairs
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");

        // Access a key (moves it to the most recently used position)
        String value = cache.get("key1");
        System.out.println("Retrieved value for key1: " + value);

        // Add a new key-value pair (may evict the least recently used if full)
        cache.put("key4", "value4");

        // Check if a key exists
        if (cache.get("key2") != null) {
            System.out.println("key2 is still in the cache");
        } else {
            System.out.println("key2 was evicted from the cache");
        }
    }
}
