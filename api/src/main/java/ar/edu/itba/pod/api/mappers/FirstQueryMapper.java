package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.HazelcastCollections;
import ar.edu.itba.pod.api.models.Reading;
import ar.edu.itba.pod.api.models.Sensor;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;


public class FirstQueryMapper implements Mapper<String, Reading, String, Long>, HazelcastInstanceAware {

    private HazelcastInstance hazelcastInstance;

    @Override
    public void map(String s, Reading reading, Context<String, Long> context) {
        IMap<Integer, Sensor> map = hazelcastInstance.getMap(HazelcastCollections.SENSORS);

        if(map.containsKey(reading.getSensorId())){
            Sensor sensor = map.get(reading.getSensorId());
            if(sensor.isActive())
                context.emit(sensor.getDescription(), (long) reading.getHourlyCounts());
        }
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
