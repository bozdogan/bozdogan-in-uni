package org.bozdogan;

/* b.ozdogan_ *//** Utility data type */
public class Pair<K, V>{
    private K key;
    private V value;

    public K getKey(){ return key; }
    public V getValue(){ return value; }

    public Pair(K key, V value){
        this.key = key;
        this.value = value;
    }

    @Override
    public int hashCode(){
        // key's hashcode is multiplied by a prime number to differentiate
        // between Pair("A", "B") and Pair("B", "A")
        return key.hashCode()*17+(value==null ? 0 : value.hashCode());
    }

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(o instanceof Pair){
            Pair pair = (Pair) o;
            return (key!=null ? key.equals(pair.key) : pair.key==null) &&
                    (value!=null ? value.equals(pair.value) : pair.value==null);
        }

        return false;
    }

    @Override
    public String toString(){ return key+"=="+value; }
}

