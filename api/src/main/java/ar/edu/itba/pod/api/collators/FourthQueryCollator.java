package ar.edu.itba.pod.api.collators;


import ar.edu.itba.pod.api.models.Pair;
import com.hazelcast.mapreduce.Collator;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class FourthQueryCollator implements Collator<Map.Entry<String, Pair<String, Double>>, SortedSet<Map.Entry<String, Pair<String, Double>>>> {

    private final Comparator<Map.Entry<String, Pair<String, Double>>> COMPARATOR = Comparator
            .comparingDouble((Map.Entry<String, Pair<String, Double>> o) -> o.getValue().getSecond()).reversed()
            .thenComparing(Map.Entry::getKey);

    private final long n;

    public FourthQueryCollator(long n) {
        this.n = n;
    }

    @Override
    public SortedSet<Map.Entry<String, Pair<String, Double>>> collate(Iterable<Map.Entry<String, Pair<String, Double>>> values) {
        SortedSet<Map.Entry<String, Pair<String, Double>>> set =
                new TreeSet<>(COMPARATOR);
        for (Map.Entry<String, Pair<String, Double>> entry : values) {
            set.add(entry);
        }

        return set.stream().limit(n).collect(Collectors.toCollection( () -> new TreeSet<>(COMPARATOR)));
    }
}
