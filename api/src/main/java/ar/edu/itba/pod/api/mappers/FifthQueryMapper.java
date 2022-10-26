package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.models.Pair;
import ar.edu.itba.pod.api.models.Reading;
import ar.edu.itba.pod.api.models.Sensor;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class FifthQueryMapper implements Mapper<String, Long, Long, String> {

    @Override
    public void map(String s, Long count, Context<Long, String> context) {
        long billionCount = count / 1000000;
        if(billionCount > 0)
            context.emit(billionCount, s);
    }

}
