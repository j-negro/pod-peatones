package ar.edu.itba.pod.api.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class Pair<T, U> implements DataSerializable {
    private T first;
    private U second;

    public Pair() {}

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public void setSecond(U second) {
        this.second = second;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(first);
        out.writeObject(second);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        first = in.readObject();
        second = in.readObject();
    }
}
