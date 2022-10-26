package ar.edu.itba.pod.api.colators;


import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class FirstQueryColator implements Collator<Map.Entry<String, Long>, SortedSet<Map.Entry<String, Long>>> {

    @Override
    public SortedSet<Map.Entry<String, Long>> collate(Iterable<Map.Entry<String, Long>> values) {
        SortedSet<Map.Entry<String, Long>> set = new TreeSet<>( new Comparator<Map.Entry<String, Long>>() {
            @Override
            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                int citizenCountComparizon =(int) (o2.getValue() - o1.getValue());
                if(citizenCountComparizon == 0){
                    return o1.getKey().compareTo(o2.getKey());
                }
                return citizenCountComparizon;
            }
        });
        for (Map.Entry<String, Long> entry : values) {
            set.add(entry);
        }
        return set;
    }
}
