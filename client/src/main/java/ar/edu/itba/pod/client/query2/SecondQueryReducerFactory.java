package ar.edu.itba.pod.client.query2;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class SecondQueryReducerFactory implements ReducerFactory<Integer, WeekendCountPair, WeekdayWeekendPair> {
    @Override
    public Reducer<WeekendCountPair, WeekdayWeekendPair> newReducer(Integer integer) {
        return new SecondQueryReducer();
    }

    private class SecondQueryReducer extends Reducer<WeekendCountPair, WeekdayWeekendPair> {

        private int weekdayCount = 0;
        private int weekendCount = 0;

        @Override
        public void reduce(WeekendCountPair weekendCountPair) {
            if (weekendCountPair.getIsWeekend()) {
                weekendCount += weekendCountPair.getHourlyCount();
            } else {
                weekdayCount += weekendCountPair.getHourlyCount();
            }
        }

        @Override
        public WeekdayWeekendPair finalizeReduce() {
            return new WeekdayWeekendPair(weekdayCount, weekendCount);
        }
    }
}
