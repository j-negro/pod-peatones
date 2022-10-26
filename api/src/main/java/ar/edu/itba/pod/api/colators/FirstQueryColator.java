package ar.edu.itba.pod.api.colators;


import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class FirstQueryColator implements Collator<Map.Entry<String, Long>, List<Map.Entry<String, Long>>> {

    @Override
    public List<Map.Entry<String, Long>> collate(Iterable<Map.Entry<String, Long>> values) {
        List<Map.Entry<String, Long>> list = new ArrayList<>();
        for (Map.Entry<String, Long> entry : values) {
            list.add(entry);
        }
        list.sort(new Comparator<Map.Entry<String, Long>>() {
            @Override
            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                int citizenCountComparizon =(int) (o2.getValue() - o1.getValue());
                if(citizenCountComparizon == 0){
                    return o1.getKey().compareTo(o2.getKey());
                }
                return citizenCountComparizon;
            }
        });
        return list;
    }
}
