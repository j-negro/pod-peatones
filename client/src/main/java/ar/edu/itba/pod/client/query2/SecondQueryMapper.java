package ar.edu.itba.pod.client.query2;

import ar.edu.itba.pod.api.models.Pair;
import ar.edu.itba.pod.api.models.Reading;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.List;

public class SecondQueryMapper implements Mapper<String, Reading, Integer, WeekendCountPair> {
    private boolean isWeekend(String day) {
        return day.equals("Saturday") || day.equals("Sunday");
    }

    @Override
    public void map(String s, Reading reading, Context<Integer, WeekendCountPair> context) {
        context.emit(reading.getYear(), new WeekendCountPair(isWeekend(reading.getDay()), reading.getHourlyCounts()));
    }
}
