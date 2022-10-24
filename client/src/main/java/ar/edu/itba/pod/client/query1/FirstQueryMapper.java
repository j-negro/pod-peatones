package ar.edu.itba.pod.client.query1;

import ar.edu.itba.pod.api.models.Reading;
import ar.edu.itba.pod.api.models.Sensor;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.List;

public class FirstQueryMapper implements Mapper<String, List<Reading>, String, Long> {

    //private static  final Logger LOGGER = LoggerFactory.getLogger(Reading.class);
    private HazelcastInstance hazelcastInstance;

    @Override
    public void map(String s, List<Reading> readings, Context<String, Long> context) {
        for(Reading reading : readings){
            Sensor sensor = (Sensor) hazelcastInstance.getMap("sensors").get(reading.getSensorId());

            context.emit(sensor.getDescription(), (long) reading.getHourlyCounts());
        }
    }
}
