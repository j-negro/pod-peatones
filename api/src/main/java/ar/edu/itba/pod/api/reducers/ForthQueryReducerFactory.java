package ar.edu.itba.pod.api.reducers;

import ar.edu.itba.pod.api.models.Pair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ForthQueryReducerFactory implements
        ReducerFactory<String, Pair<String, Integer>, Pair<String, Double>> {

    private static final Comparator<Pair<Integer, LocalDateTime>> COMPARATOR =
            Comparator.comparingInt((Pair<Integer, LocalDateTime> o) -> o.getFirst()).thenComparing(Pair::getSecond);

    @Override
    public Reducer<Pair<String, Integer>, Pair<String, Double>> newReducer(String s) {
        return new Reducer<>() {
            Map<String, Pair<Long, Integer>> monthToAvg = new HashMap<>();

            @Override
            public void reduce(Pair<String, Integer> entry) {
                if (entry.getFirst() == null || entry.getSecond() == null) return;

                monthToAvg.putIfAbsent(entry.getFirst(), new Pair<>(0L, 0)); // Add entry to month
                Pair<Long, Integer> monthCount = monthToAvg.get(entry.getFirst());
                monthCount.setFirst(monthCount.getFirst() + entry.getSecond());
                monthCount.setSecond(monthCount.getSecond() + 1);

            }

            @Override
            public Pair<String, Double> finalizeReduce() {
                Pair<String, Double> result = new Pair<>("", 0.0);

                for (Map.Entry<String, Pair<Long, Integer>> monthly : monthToAvg.entrySet()) {
                    Double monthlyAvg = monthly.getValue().getFirst() / Double.valueOf(monthly.getValue().getSecond());
                    if (result.getSecond() < monthlyAvg) {
                        result.setFirst(monthly.getKey());
                        result.setSecond(monthlyAvg);
                    }
                }

                return result;
            }
        };
    }
}
