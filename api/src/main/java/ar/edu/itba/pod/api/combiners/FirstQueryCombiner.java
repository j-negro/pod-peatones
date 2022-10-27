package ar.edu.itba.pod.api.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class FirstQueryCombiner implements CombinerFactory<String, Long, Long> {
    @Override
    public Combiner<Long, Long> newCombiner(String s) {
        return new Combiner<>(){
            long sum = 0;

            @Override
            public void reset() {
                sum = 0;
            }

            @Override
            public void combine(Long citizenCount) {
                sum += citizenCount;
            }

            @Override
            public Long finalizeChunk() {
                return sum;
            }
        };
    }

}
