//Matheus Veiga Bacetic Joaquim | RA: 10425638
//João Vitor Rocha Miranda | RA: 10427273
//Gabriel Pereira Faravola | RA: 10427189

class MyHashMap<K, V> {
    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private Entry<K, V>[] table;
    private static final int INITIAL_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new Entry[INITIAL_CAPACITY];
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % INITIAL_CAPACITY;
    }

    public V getOrDefault(K key, V defaultValue) {
        int index = hash(key);
        Entry<K, V> current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return defaultValue;
    }

    public void put(K key, V value) {
        int index = hash(key);
        Entry<K, V> newEntry = new Entry<>(key, value);

        if (table[index] == null) {
            table[index] = newEntry;
        } else {
            Entry<K, V> current = table[index];
            while (current.next != null) {
                if (current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            if (current.key.equals(key)) {
                current.value = value;
            } else {
                current.next = newEntry;
            }
        }
    }

    public boolean containsKey(K key) {
        int index = hash(key);
        Entry<K, V> current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
}