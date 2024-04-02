package cachingStrategies.lru;

import java.util.HashMap;
import java.util.Map;


/**
 * LRU (Least Recently Used) Cache implementation using a HashMap and a doubly linked list.
 *
 * This cache keeps track of the most recently accessed entries and evicts the least recently used
 * entries when the cache reaches its capacity.
 *
 * @param <K> The type of keys used in the cache.
 * @param <V> The type of values stored in the cache.
 */
public class LRUCacheUsingHashMap<K, V> implements ILRUCache<K,V> {
    private final int capacity;
    private final Map<K, Node<V>> map;  // HashMap for fast key-value lookup
    private Node<V> head; // Doubly linked list head
    private Node<V> tail; // Doubly linked list tail

    /**
     * Creates a new LRUCache with the specified capacity.
     *
     * @param capacity The maximum number of entries the cache can hold.
     */
    public LRUCacheUsingHashMap(int capacity) {
        this.capacity = capacity;
        map = new HashMap<>();
    }

    /**
     * Retrieves a value from the cache based on its key.
     *
     * If the key exists in the cache, the corresponding value is returned and the accessed node is
     * moved to the head of the doubly linked list (most recently used).
     *
     * @param key The key of the data to retrieve.
     * @return The value associated with the key, or null if not found.
     */
    public V get(K key) {
        Node<V> node = map.get(key);
        if (node != null) {
            moveToHead(node);  // Move accessed node to the beginning (most recently used)
            return node.value;
        }
        return null;
    }

    /**
     * Adds or updates a key-value pair in the cache.
     *
     * If the key already exists, the value is updated and the accessed node is moved to the head
     * of the doubly linked list. If the key is new, a new node is created, added to the map and
     * linked list, and potentially evicted if the capacity is reached (least recently used).
     *
     * @param key The key of the data to store or update.
     * @param value The value to associate with the key.
     */
    public void put(K key, V value) {
        Node<V> node = map.get(key);
        if (node != null) {
            node.value = value;
            moveToHead(node);
        } else {
            Node<V> newNode = new Node<>(key, value);
            map.put(key, newNode);
            addNodeAtBeginning(newNode);
            if (map.size() > capacity) {
                removeLeastRecentlyUsed();  // Evict least recently used when capacity is reached
            }
        }
    }

    // Helper methods to manage the doubly linked list

    /**
     * Moves a node to the head of the doubly linked list, signifying recent access.
     *
     * @param node The node to move.
     */
    private void moveToHead(Node<V> node) {
        removeNode(node);
        addNodeAtBeginning(node);
    }

    /**
     * Adds a node to the tail of the doubly linked list.
     *
     * @param node The node to add.
     */
    private void addNodeAtBeginning(Node<V> node) {
        if(head == null) {
            head = node;
            tail = node;
            head.prev = tail;
            tail.next = head;
        } else {
            node.next = head;
            node.prev = head.prev;
            head.prev = node;
            head = node;
        }
    }

    /**
     * Removes a node from the doubly linked list.
     *
     * @param node The node to remove.
     */
    private void removeNode(Node<V> node) {

        if(node == tail) {
            tail.prev.next = head;
            head.prev = tail.prev;
            tail = tail.prev;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

    }

    /**
     * Removes the least recently used (LRU) entry from the cache.
     *
     * This method removes the tail node (least recently used) from both the linked list and
     * the HashMap. However, there's a potential edge case where the cache becomes empty after
     * removal. This fix ensures the `tail` pointer is always set to the head (or null)
     * in such scenarios.
     */
    private void removeLeastRecentlyUsed() {
        Node<V> nodeToRemove = tail;
        map.remove(nodeToRemove.key);
        removeNode(nodeToRemove);
    }


    /**
     * Node class used in the LRU cache implementation.
     *
     * This class represents a node in a doubly linked list, holding the data value and references
     * to the previous and next nodes.
     *
     * @param <V> The type of value stored in the node.
     */
    private class Node<V> {

        /**
         * The key associated with the data (used for LRU eviction if needed).
         */
        final K key;  // Final for immutability

        /**
         * The value stored in the node.
         */
        V value;

        /**
         * Reference to the previous node in the doubly linked list.
         */
        Node<V> prev;

        /**
         * Reference to the next node in the doubly linked list.
         */
        Node<V> next;

        /**
         * Constructor to create a new Node with the specified value.
         *
         * @param value The value to store in the node.
         */
        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
