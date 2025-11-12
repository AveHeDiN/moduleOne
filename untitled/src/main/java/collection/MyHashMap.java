package collection;


public class MyHashMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Entry<K, V>[] table;
    private int size;
    private int capacity;
    private float loadFactor;

    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int capacity, float loadFactor) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        table = new Entry[capacity];
    }


    static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        public Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }


    private int hash(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }


    public void put(K key, V value) {
        int index = hash(key);

        Entry<K, V> newEntry = new Entry<>(key, value, null);

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
                return;
            }
            current.next = newEntry;
        }

        size++;

        // Проверяем, нужно ли изменить размер таблицы
        if ((float) size / capacity >= loadFactor) {
            resizeTable();
        }
    }


    public V get(K key) {
        int index = hash(key);

        Entry<K, V> current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }


    public void remove(K key) {
        int index = hash(key);
        Entry<K, V> current = table[index];
        Entry<K, V> previous = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return;
            }
            previous = current;
            current = current.next;
        }
    }


    public int size() {
        return size;
    }


    // Изменяет размер таблицы, когда достигнут коэффициент загрузки
    private void resizeTable() {
        int newCapacity = capacity * 2;
        Entry<K, V>[] newTable = new Entry[newCapacity];

        // Перехешируем и копируем существующие записи в новую таблицу.
        for (int i = 0; i < capacity; i++) {
            Entry<K, V> current = table[i];
            while (current != null) {
                Entry<K, V> next = current.next;
                int newIndex = Math.abs(current.key.hashCode()) % newCapacity;
                current.next = newTable[newIndex];
                newTable[newIndex] = current;
                current = next;
            }
        }

        table = newTable;
        capacity = newCapacity;
    }

    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();
        map.put("Один", 1);
        map.put("Два", 2);
        map.put("Три", 3);

        System.out.println("Значение для ключа 'Два': " + map.get("Два"));
        System.out.println("Размер: " + map.size());

        map.remove("Два");
        System.out.println("Значение для ключа 'Два' после удаления: " + map.get("Два")); 
        System.out.println("Размер после удаления: " + map.size());
    }
}
