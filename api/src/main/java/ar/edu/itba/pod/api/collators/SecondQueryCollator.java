package ar.edu.itba.pod.api.collators;


import ar.edu.itba.pod.api.models.Pair;
import com.hazelcast.mapreduce.Collator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SecondQueryCollator implements Collator<Map.Entry<Integer, Pair<Integer, Integer>>, List<Map.Entry<Integer, Pair<Integer, Integer>>>> {
    @Override
    public List<Map.Entry<Integer, Pair<Integer, Integer>>> collate(Iterable<Map.Entry<Integer, Pair<Integer, Integer>>> values) {
        List<Map.Entry<Integer, Pair<Integer, Integer>>> list = new ArrayList<>();
        for (Map.Entry<Integer, Pair<Integer, Integer>> entry : values) {
            list.add(entry);
        }
        list.sort(Map.Entry.comparingByKey(Comparator.reverseOrder()));
        return list;
    }
}
