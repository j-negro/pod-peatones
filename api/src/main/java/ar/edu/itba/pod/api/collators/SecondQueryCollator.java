package ar.edu.itba.pod.api.collators;


import ar.edu.itba.pod.api.models.Pair;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class SecondQueryCollator implements Collator<Map.Entry<Integer, Pair<Integer, Integer>>, SortedSet<Map.Entry<Integer, Pair<Integer, Integer>>>> {
    @Override
    public SortedSet<Map.Entry<Integer, Pair<Integer, Integer>>> collate(Iterable<Map.Entry<Integer, Pair<Integer, Integer>>> values) {
        SortedSet<Map.Entry<Integer, Pair<Integer, Integer>>> set =
                new TreeSet<>(Map.Entry.comparingByKey(Comparator.reverseOrder()));
        for (Map.Entry<Integer, Pair<Integer, Integer>> entry : values) {
            set.add(entry);
        }
        return set;
    }
}
