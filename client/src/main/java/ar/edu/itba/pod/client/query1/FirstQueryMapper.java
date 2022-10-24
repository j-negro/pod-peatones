package ar.edu.itba.pod.server.mappers;

import com.hazelcast.logging.LoggerFactory;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.StringTokenizer;
import java.util.logging.Logger;

public class FirstQueryMapper implements Mapper<String, String, String, Long> {
    private static final Long ONE = 1L;
    private static  final Logger LOGGER = LoggerFactory.getLogger(TokenizerMapper.class);
    @Override
    public void map(String s, String s2, Context<String, Long> context) {
        StringTokenizer tokenizer = new StringTokenizer();
        while(tokenizer.hasMoreTokens()){
            context.emit(tokenizer.nextToken(),ONE);
        }
    }
}
