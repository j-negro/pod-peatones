package ar.edu.itba.pod.api.mappers;

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
