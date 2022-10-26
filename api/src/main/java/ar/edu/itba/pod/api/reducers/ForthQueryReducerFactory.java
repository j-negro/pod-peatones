package ar.edu.itba.pod.client.query4;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.math.BigDecimal;

public class ForthQueryReducerFactory implements ReducerFactory<String, PedestriansPerMonthCount, MaxAvgPedestriansForMonth> {
    @Override
    public Reducer<PedestriansPerMonthCount, MaxAvgPedestriansForMonth> newReducer(String key) {
        return new MaxMonthlyAvgReducer();
    }

    private class MaxMonthlyAvgReducer extends Reducer<PedestriansPerMonthCount, MaxAvgPedestriansForMonth>{
        private volatile Integer sum;
        private volatile Integer

        @Override
        public void beginReduce(){ ;}

        @Override
        public void reduce(PedestriansPerMonthCount citizenCount) {
            avg += citizenCount;
        }

        @Override
        public MaxAvgPedestriansForMonth finalizeReduce() {
            return sum;
        }
    }
}
