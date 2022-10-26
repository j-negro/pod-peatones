package ar.edu.itba.pod.client;

import ar.edu.itba.pod.api.models.Reading;
import ar.edu.itba.pod.api.models.Sensor;
import ar.edu.itba.pod.api.models.Status;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

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

    private static final int BATCH_SIZE = 500000;

    public static void readAndImportSensors(IMap<Integer, Sensor> map, Path path) throws IOException {
        try (Stream<String> lines = Files.lines(path)) {
            lines.skip(1)
                    .map(l -> l.split(";"))
                    .map(v -> new Sensor(
                            Integer.parseInt(v[0]),
                            v[1],
                            v[4]
                    ))
                    .forEach(s -> map.put(s.getSensorId(), s));
        }
    }

    public static void readAndImportReadings(IList<Reading> list, Path path) throws IOException {
        List<Reading> aux = new ArrayList<>();
        int i = 0;
        do {
            LOGGER.info("Reading batch {}", i+1);
            aux.clear();
            try (Stream<String> lines = Files.lines(path)) {
                lines.skip(1 + (long) i *BATCH_SIZE).limit(BATCH_SIZE)
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
                        .forEach(aux::add);
                i++;

                list.addAll(aux);
            }
        }
        while (aux.size() == BATCH_SIZE);
    }
}
