package ar.edu.itba.pod.api.collators;

import ar.edu.itba.pod.api.models.Pair;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class FifthQueryCollator implements
        Collator<Map.Entry<Long, Set<String>>, Collection<Map.Entry<Long, Pair<String, String>>>> {

    private static final Comparator<Map.Entry<Long, Pair<String, String>>> COMPARATOR = Comparator
            .comparingLong((Map.Entry<Long, Pair<String, String>> o) -> o.getKey()).reversed()
            .thenComparing(o -> o.getValue().getFirst())
            .thenComparing(o -> o.getValue().getSecond());


    @Override
    public Collection<Map.Entry<Long, Pair<String, String>>> collate(Iterable<Map.Entry<Long, Set<String>>> iterable) {
        Collection<Map.Entry<Long, Pair<String, String>>> ans = new TreeSet<>(COMPARATOR);
        for (Map.Entry<Long, Set<String>> me : iterable) {
            if (me.getValue().size() > 1) {
                for (String sensor : me.getValue()) {
                    for (String otherSensor : me.getValue()) {
                        if (sensor.equals(otherSensor))
                            continue;
                        if (sensor.compareTo(otherSensor) < 0) {
                            ans.add(Map.entry(me.getKey(), new Pair<>(sensor, otherSensor)));
                        } else {
                            ans.add(Map.entry(me.getKey(), new Pair<>(otherSensor, sensor)));
                        }

                    }
                }
            }
        }
        return ans;
    }
}
