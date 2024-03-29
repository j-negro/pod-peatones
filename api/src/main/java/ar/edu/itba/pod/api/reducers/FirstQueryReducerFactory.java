package ar.edu.itba.pod.api.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class FirstQueryReducerFactory implements ReducerFactory<String, Long, Long> {

    @Override
    public Reducer<Long, Long> newReducer(String key) {
        return new CitizenCountReducer();
    }

    private static class  CitizenCountReducer extends Reducer<Long, Long>{
        private long sum;

        @Override
        public void beginReduce(){ sum = 0;}

        @Override
        public void reduce(Long citizenCount) {
            sum += citizenCount;
        }

        @Override
        public Long finalizeReduce() {
            return sum;
        }
    }
}
