package ar.edu.itba.pod.api.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class Sensor implements DataSerializable {

    private int sensorId;
    private String description;
    private String status;

    public Sensor() {}

    public Sensor(int sensorId, String description, String status) {
        this.sensorId = sensorId;
        this.description = description;
        this.status = status;
    }

    public int getSensorId() {
        return this.sensorId;
    }

    public String getDescription() {
        return this.description;
    }

    public String getStatus() {
        return status;
    }

    public boolean isActive() {
        return status.equals("A");
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeInt(this.sensorId);
        out.writeUTF(this.description);
        out.writeUTF(this.status);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        this.sensorId = in.readInt();
        this.description = in.readUTF();
        this.status = in.readUTF();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sensor sensor = (Sensor) o;
        return sensorId == sensor.sensorId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sensorId);
    }
}
