package ar.edu.itba.pod.api.reducers;

import ar.edu.itba.pod.api.models.Pair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class SecondQueryReducerFactory implements ReducerFactory<Integer, Pair<Boolean, Integer>, Pair<Integer, Integer>> {
    @Override
    public Reducer<Pair<Boolean, Integer>, Pair<Integer, Integer>> newReducer(Integer integer) {
        return new SecondQueryReducer();
    }

    private class SecondQueryReducer extends Reducer<Pair<Boolean, Integer>, Pair<Integer, Integer>> {

        private int weekdayCount = 0;
        private int weekendCount = 0;

        @Override
        public void reduce(Pair<Boolean, Integer> weekendCountPair) {
            if (weekendCountPair.getFirst()) {
                weekendCount += weekendCountPair.getSecond();
            } else {
                weekdayCount += weekendCountPair.getSecond();
            }
        }

        @Override
        public Pair<Integer, Integer> finalizeReduce() {
            return new Pair<Integer, Integer>(weekdayCount, weekendCount);
        }
    }
}
