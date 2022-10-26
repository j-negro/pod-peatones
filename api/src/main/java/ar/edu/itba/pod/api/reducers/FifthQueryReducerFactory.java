package ar.edu.itba.pod.api.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.HashSet;
import java.util.Set;

public class FifthQueryReducerFactory implements ReducerFactory<Long, String, Set<String>> {

    @Override
    public Reducer<String, Set<String>> newReducer(Long key) {
        return new BillionCountReducer();
    }

    private static class BillionCountReducer extends Reducer<String, Set<String>>{

        private volatile Set<String> sameBillionCount;

        @Override
        public void beginReduce() {
            sameBillionCount = new HashSet<>();
        }

        @Override
        public void reduce(String sensor) {
            sameBillionCount.add(sensor);
        }

        @Override
        public Set<String> finalizeReduce() {
            return sameBillionCount;
        }
    }
}
