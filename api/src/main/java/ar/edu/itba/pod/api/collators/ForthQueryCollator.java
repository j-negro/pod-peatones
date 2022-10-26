package ar.edu.itba.pod.api.collators;


import ar.edu.itba.pod.api.models.Pair;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class ForthQueryCollator implements Collator<Map.Entry<String, Pair<String, Double>>, SortedSet<Map.Entry<String, Pair<String, Double>>>> {

    private final long n;

    public ForthQueryCollator(long n) {
        this.n = n;
    }

    @Override
    public SortedSet<Map.Entry<String, Pair<String, Double>>> collate(Iterable<Map.Entry<String, Pair<String, Double>>> values) {
        SortedSet<Map.Entry<String, Pair<String, Double>>> set =
                new TreeSet<>(Map.Entry.comparingByValue(Comparator.comparingDouble(Pair::getSecond)));
        int i = 0;
        for (Map.Entry<String, Pair<String, Double>> entry : values) {
            if (i < n) break;
            set.add(entry);
            i++;
        }
        return set;
    }
}
