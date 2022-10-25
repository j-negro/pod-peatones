package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.models.Pair;
import ar.edu.itba.pod.api.models.Reading;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.List;

public class SecondQueryMapper implements Mapper<String, Reading, Integer, Pair<Boolean, Integer>> {
    private boolean isWeekend(String day) {
        return day.equals("Saturday") || day.equals("Sunday");
    }

    @Override
    public void map(String s, Reading reading, Context<Integer, Pair<Boolean, Integer>> context) {
        context.emit(reading.getYear(), new Pair<>(isWeekend(reading.getDay()), reading.getHourlyCounts()));
    }
}
