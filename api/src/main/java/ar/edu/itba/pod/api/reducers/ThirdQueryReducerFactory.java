package ar.edu.itba.pod.api.reducers;

import ar.edu.itba.pod.api.models.Pair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.time.LocalDateTime;
import java.util.Comparator;

public class ThirdQueryReducerFactory implements
        ReducerFactory<String, Pair<Integer, LocalDateTime>, Pair<Integer, LocalDateTime>> {
    private static final Comparator<Pair<Integer, LocalDateTime>> COMPARATOR =
            Comparator.comparingInt((Pair<Integer, LocalDateTime> o) -> o.getFirst()).thenComparing(Pair::getSecond);

    @Override
    public Reducer<Pair<Integer, LocalDateTime>, Pair<Integer, LocalDateTime>> newReducer(String s) {
        return new Reducer<>() {
            Pair<Integer, LocalDateTime> result;

            @Override
            public void reduce(Pair<Integer, LocalDateTime> entry) {
                if(entry == null) return;
                if(result == null || COMPARATOR.compare(entry, result) > 0) {
                    result = entry;
                }
            }

            @Override
            public Pair<Integer, LocalDateTime> finalizeReduce() {
                return result;
            }
        };
    }

}
