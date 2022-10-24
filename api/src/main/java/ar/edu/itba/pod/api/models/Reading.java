package ar.edu.itba.pod.api.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class Reading implements DataSerializable {

    private final Sensor sensor;
    private final Integer year;
    private final String month;
    private final Integer mDate;
    private final String day;
    private final Integer hour;
    private final Integer hourlyCounts;

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

    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {

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

    public Integer getmDate() {
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
