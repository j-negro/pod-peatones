package ar.edu.itba.pod.client;

import ar.edu.itba.pod.api.models.Reading;
import ar.edu.itba.pod.api.models.Sensor;
import ar.edu.itba.pod.api.models.Status;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

public class Util {
    public static Collection<Reading> readReadings(Path path) throws IOException {
        return Files.readAllLines(path)
                .parallelStream().skip(1)
                .map(l -> l.split(";"))
                .map(v -> new Reading(
                        Integer.parseInt(v[2]),
                        v[3],
                        Integer.parseInt(v[4]),
                        v[5],
                        Integer.parseInt(v[6]),
                        Integer.parseInt(v[7]),
                        Integer.parseInt(v[9])
                ))
                .collect(Collectors.toList());
    }

    public static Collection<Sensor> readSensors(Path path) throws IOException {
        return Files.readAllLines(path)
                .parallelStream().skip(1)
                .map(l -> l.split(";"))
                .map(v -> new Sensor(
                        Integer.parseInt(v[0]),
                        v[1],
                        v[4]
                ))
                .collect(Collectors.toList());
    }

    public static void importReadings(IList<Reading> list, Collection<Reading> collection) {
        list.addAll(collection);
    }

    public static void importSensors(IMap<Integer, Sensor> map, Collection<Sensor> collection) {
        collection.parallelStream().forEach(s -> map.put(s.getSensorId(), s));
    }
}
