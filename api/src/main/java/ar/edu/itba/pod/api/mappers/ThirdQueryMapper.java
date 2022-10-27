package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.HazelcastCollections;
import ar.edu.itba.pod.api.models.Pair;
import ar.edu.itba.pod.api.models.Reading;
import ar.edu.itba.pod.api.models.Sensor;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ThirdQueryMapper implements
        Mapper<String, Reading, String, Pair<Integer, LocalDateTime>>,
        HazelcastInstanceAware {
    private final long threshold;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d yyyy H");
    private transient HazelcastInstance hazelcastInstance;

    public ThirdQueryMapper(long threshold) {
        this.threshold = threshold;
    }

    @Override
    public void map(String s, Reading reading, Context<String, Pair<Integer, LocalDateTime>> context) {
        final IMap<Integer, Sensor> map = hazelcastInstance.getMap(HazelcastCollections.SENSORS);
        final Sensor sensor = map.get(reading.getSensorId());
        if(reading.getHourlyCounts() > threshold && sensor != null && sensor.isActive()) {
            final LocalDateTime dateTime = LocalDateTime.parse(
                    reading.getMonth()+' '+reading.getMDate()+' '+reading.getYear()+' '+reading.getTime(), formatter);
            final Pair<Integer, LocalDateTime> entry = new Pair<>(reading.getHourlyCounts(), dateTime);
            context.emit(sensor.getDescription(), entry);
        }
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

}
