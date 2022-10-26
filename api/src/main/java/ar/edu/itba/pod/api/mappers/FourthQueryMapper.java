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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FourthQueryMapper implements Mapper<String, Reading, String, Pair<String, Integer>>, HazelcastInstanceAware {

    private static  final Logger LOGGER = LoggerFactory.getLogger(FourthQueryMapper.class);
    private HazelcastInstance hazelcastInstance;

    private final Long year;

    public FourthQueryMapper(Long year) {
        this.year = year;
    }

    @Override
    public void map(String s, Reading reading, Context<String, Pair<String, Integer>> context) {
        IMap<Integer, Sensor> map = hazelcastInstance.getMap(HazelcastCollections.SENSORS);

        Sensor sensor = map.get(reading.getSensorId());
        if(reading.getYear() == year && sensor != null && sensor.isActive())
            context.emit(sensor.getDescription(), new Pair<>(reading.getMonth(), reading.getHourlyCounts()));
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
