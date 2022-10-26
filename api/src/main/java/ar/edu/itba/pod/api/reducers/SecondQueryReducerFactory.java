package ar.edu.itba.pod.api.reducers;

import ar.edu.itba.pod.api.models.Pair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class SecondQueryReducerFactory implements ReducerFactory<Integer, Pair<Integer, Integer>, Pair<Integer, Integer>> {
    @Override
    public Reducer<Pair<Integer, Integer>, Pair<Integer, Integer>> newReducer(Integer integer) {
        return new SecondQueryReducer();
    }

    private static class SecondQueryReducer extends Reducer<Pair<Boolean, Integer>, Pair<Integer, Integer>> {

        private int weekdayCount = 0;
        private int weekendCount = 0;

        @Override
        public void reduce(Pair<Integer, Integer> weekendCountPair) {
            weekdayCount += weekendCountPair.getFirst();
            weekendCount += weekendCountPair.getSecond();
        }

        @Override
        public Pair<Integer, Integer> finalizeReduce() {
            return new Pair<>(weekdayCount, weekendCount);
        }
    }
}
