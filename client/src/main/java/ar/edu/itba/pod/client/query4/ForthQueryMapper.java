package ar.edu.itba.pod.client.query4;

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

public class ForthQueryMapper implements Mapper<String, Reading, String, PedestriansPerMonthCount>, HazelcastInstanceAware {

    private static  final Logger LOGGER = LoggerFactory.getLogger(ForthQueryMapper.class);
    private HazelcastInstance hazelcastInstance;

    @Override
    public void map(String s, Reading reading, Context<String, PedestriansPerMonthCount> context) {
        IMap<Integer, Sensor> map = hazelcastInstance.getMap("sensors");

        Sensor sensor = map.get(reading.getSensorId());
        if(sensor.getStatus() == Status.ACTIVE)
            context.emit(sensor.getDescription(), new PedestriansPerMonthCount(reading.getMonth(), reading.getHourlyCounts()));
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
