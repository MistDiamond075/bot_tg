package utils;

import java.util.Objects;

public final class Pair<K,V>{
    public K first;
    public V second;

    public Pair(K first, V second){
        this.first = first;
        this.second = second;
    }

    public static class Builder<K,V>{
        private K first;
        private V second;
        public Builder<K,V> first(K first){
            this.first = first;
            return this;
        }
        public Builder<K,V> second(V second){
            this.second = second;
            return this;
        }
        public Pair<K,V> build(){
            return new Pair<>(first, second);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
