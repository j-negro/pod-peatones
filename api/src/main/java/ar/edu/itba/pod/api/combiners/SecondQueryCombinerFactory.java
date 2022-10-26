package ar.edu.itba.pod.api.combiners;

import ar.edu.itba.pod.api.models.Pair;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class SecondQueryCombinerFactory implements CombinerFactory<Integer, Pair<Boolean, Integer>, Pair<Integer, Integer>> {

    @Override
    public Combiner<Pair<Boolean, Integer>, Pair<Integer, Integer>> newCombiner(Integer integer) {
        return new Combiner<>() {
            private int weekdayCount = 0;
            private int weekendCount = 0;

            @Override
            public void reset() {
                weekdayCount = 0;
                weekendCount = 0;
            }

            @Override
            public void combine(Pair<Boolean, Integer> booleanIntegerPair) {
                if (booleanIntegerPair.getFirst()) {
                    weekendCount += booleanIntegerPair.getSecond();
                } else {
                    weekdayCount += booleanIntegerPair.getSecond();
                }
            }

            @Override
            public Pair<Integer, Integer> finalizeChunk() {
                return new Pair<>(weekdayCount, weekendCount);
            }
        };
    }
}
