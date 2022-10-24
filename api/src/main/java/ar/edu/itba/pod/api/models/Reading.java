package ar.edu.itba.pod.api.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class Reading implements DataSerializable {

    private Sensor sensor;
    private Integer year;
    private String month;
    private Integer mDate;
    private String day;
    private Integer hour;
    private Integer hourlyCounts;

    public Reading(Sensor sensor, Integer year, String month, Integer mDate, String day, Integer hour, Integer hourlyCounts) {
        this.sensor = sensor;
        this.year = year;
        this.month = month;
        this.mDate = mDate;
        this.day = day;
        this.hour = hour;
        this.hourlyCounts = hourlyCounts;
    }



    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(sensor);
        out.writeInt(year);
        out.writeUTF(month);
        out.writeInt(mDate);
        out.writeUTF(day);
        out.writeInt(hour);
        out.writeInt(hourlyCounts);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        sensor = in.readObject();
        year = in.readInt();
        month = in.readUTF();
        mDate = in.readInt();
        day = in.readUTF();
        hour = in.readInt();
        hourlyCounts = in.readInt();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Reading reading = (Reading) obj;
        return sensor.equals(reading.sensor) &&
                year.equals(reading.year) &&
                month.equals(reading.month) &&
                mDate.equals(reading.mDate) &&
                day.equals(reading.day) &&
                hour.equals(reading.hour) &&
                hourlyCounts.equals(reading.hourlyCounts);
    }

    public Sensor getSensor() {
        return sensor;
    }

    public Integer getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public Integer getMDate() {
        return mDate;
    }

    public String getDay() {
        return day;
    }

    public Integer getHour() {
        return hour;
    }

    public Integer getHourlyCounts() {
        return hourlyCounts;
    }


}
