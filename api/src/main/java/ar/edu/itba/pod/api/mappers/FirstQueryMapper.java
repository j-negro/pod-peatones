package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.models.Reading;
import ar.edu.itba.pod.api.models.Sensor;
import ar.edu.itba.pod.api.models.Status;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FirstQueryMapper implements Mapper<String, Reading, String, Long>, HazelcastInstanceAware {

    private static  final Logger LOGGER = LoggerFactory.getLogger(FirstQueryMapper.class);
    private HazelcastInstance hazelcastInstance;

    @Override
    public void map(String s, Reading reading, Context<String, Long> context) {
        IMap<Integer, Sensor> map = hazelcastInstance.getMap("sensors");

        Sensor sensor = map.get(reading.getSensorId());
        if(sensor.getStatus().equals("A") )
            LOGGER.debug("mapping sensor"+ sensor.getDescription());
            context.emit(sensor.getDescription(), (long) reading.getHourlyCounts());
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
