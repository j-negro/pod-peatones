package ar.edu.itba.pod.api.collators;


import ar.edu.itba.pod.api.models.Pair;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class ForthQueryCollator implements Collator<Map.Entry<String, Pair<String, Double>>, SortedSet<Map.Entry<String, Pair<String, Double>>>> {

    @Override
    public SortedSet<Map.Entry<String, Pair<String, Double>>> collate(Iterable<Map.Entry<String, Pair<String, Double>>> values) {
        SortedSet<Map.Entry<String, Pair<String, Double>>> set =
                new TreeSet<>(Map.Entry.comparingByValue(Comparator.comparingDouble(Pair::getSecond)));
        for (Map.Entry<String, Pair<String, Double>> entry : values) {
            set.add(entry);
        }
        return set;
    }
}
