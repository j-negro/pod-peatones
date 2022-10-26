package ar.edu.itba.pod.api.collators;

import ar.edu.itba.pod.api.models.Pair;
import com.hazelcast.mapreduce.Collator;

import java.time.LocalDateTime;
import java.util.*;

public class ThirdQueryCollator implements
        Collator<Map.Entry<String, Pair<Integer, LocalDateTime>>, Collection<Map.Entry<String, Pair<Integer, LocalDateTime>>>> {

    private static final Comparator<Map.Entry<String, Pair<Integer, LocalDateTime>>> COMPARATOR = (o1, o2) -> {
        int c = o2.getValue().getFirst().compareTo(o1.getValue().getFirst());
        if(c == 0) {
            c = o1.getKey().compareTo(o2.getKey());
        }
        return c;
    };

    @Override
    public Collection<Map.Entry<String, Pair<Integer, LocalDateTime>>>
    collate(Iterable<Map.Entry<String, Pair<Integer, LocalDateTime>>> iterable) {
        Set<Map.Entry<String, Pair<Integer, LocalDateTime>>> ans = new TreeSet<>(COMPARATOR);
        for (Map.Entry<String, Pair<Integer, LocalDateTime>> entry : iterable) {
            ans.add(entry);
        }
        return ans;
    }
}
