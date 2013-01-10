package SearchEnginePackage;

import java.util.Arrays;


class Bucket {
    final String key;
    final int hashKey;
    URLList value;

    public Bucket(String s, int k, URLList v) {
        key = s;
        hashKey = k;
        value = v;
    }
}
public class SimpleMap{
    int size;
    int defaultCapacity = 64;

    Bucket[] mappings = new Bucket[defaultCapacity];

    public URLList get(int key) {
        for(int i = 0; i < size; i++) {
            if(mappings[i] != null) {
                if(mappings[i].hashKey == key){
                    return mappings[i].value;
                }
            }
        }
        return null;
    }

    public void put(String s, int key, URLList value) {
        boolean insert = true;
        for(int i = 0; i < size; i++) {
            if(mappings[i] == null){
                continue;
            }
            if(mappings[i].hashKey == key) {
                value.next = mappings[i].value; //insert at the beginning
                mappings[i].value = value;
                insert = false;
            }
        }
        if(insert) {
            size++;
            expand();
            mappings[size] = new Bucket(s, key, value);
        }
    }

    public void expand() {
        if(size == mappings.length) {
            mappings = Arrays.copyOf(mappings, mappings.length * 2);
        }
    }
}