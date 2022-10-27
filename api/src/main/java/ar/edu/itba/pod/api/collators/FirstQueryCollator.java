package ar.edu.itba.pod.api.collators;


import com.hazelcast.mapreduce.Collator;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class FirstQueryCollator implements Collator<Map.Entry<String, Long>, SortedSet<Map.Entry<String, Long>>> {

    @Override
    public SortedSet<Map.Entry<String, Long>> collate(Iterable<Map.Entry<String, Long>> values) {
        SortedSet<Map.Entry<String, Long>> set = new TreeSet<>((o1, o2) -> {
            int citizenCountComparizon = (int) (o2.getValue() - o1.getValue());
            if (citizenCountComparizon == 0) {
                return o1.getKey().compareTo(o2.getKey());
            }
            return citizenCountComparizon;
        });
        for (Map.Entry<String, Long> entry : values) {
            set.add(entry);
        }
        return set;
    }
}
