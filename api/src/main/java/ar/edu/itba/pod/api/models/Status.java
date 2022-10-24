package ar.edu.itba.pod.api.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public enum Status implements DataSerializable {
    ACTIVE("A"),
    REMOVED("R"),
    INACTIVE("I");

    private String status;

    Status(String status) {
        this.status = status;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(status);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        this.status = in.readUTF();
    }

    public static Status from(String status) {
        for (Status s : Status.values()) {
            if (status.equals(s.status)) {
                return s;
            }
        }
        throw new IllegalArgumentException(status);
    }
}
