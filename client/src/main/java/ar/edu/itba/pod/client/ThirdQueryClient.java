package ar.edu.itba.pod.client;

import ar.edu.itba.pod.api.collators.ThirdQueryCollator;
import ar.edu.itba.pod.api.mappers.ThirdQueryMapper;
import ar.edu.itba.pod.api.models.Pair;
import ar.edu.itba.pod.api.models.Reading;
import ar.edu.itba.pod.api.reducers.ThirdQueryReducerFactory;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ThirdQueryClient extends Query {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdQueryClient.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ThirdQueryClient() {
        super(3);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Query query = new ThirdQueryClient();

        query.logTime();
        query.setupSensorsMap();
        IList<Reading> readings = query.setupReadingsList();
        query.logTime();

        JobTracker t = query.client.getJobTracker("g8-max-count");
        final KeyValueSource<String, Reading> sourceList = KeyValueSource.fromList(readings);
        Job<String, Reading> job = t.newJob(sourceList);

        long min = 0;
        try {
            min = Long.parseLong(System.getProperty("min", "0"));
        } catch(NumberFormatException e) {
            LOGGER.error(e.getMessage());
            query.shutdown();
            System.exit(1);
        }

        query.logTime();
        ICompletableFuture<Collection<Map.Entry<String, Pair<Integer, LocalDateTime>>>> future =
                job.mapper(new ThirdQueryMapper(min)).reducer(new ThirdQueryReducerFactory()).submit(new ThirdQueryCollator());
        Collection<Map.Entry<String, Pair<Integer, LocalDateTime>>> result = future.get();

        System.out.println("Map size: "+result.size());

        query.outputLine("Sensor;Max_Reading_Count;Max_Reading_DateTime");
        for(Map.Entry<String, Pair<Integer, LocalDateTime>> entry : result) {
            String line = entry.getKey() +
                    ';' +
                    entry.getValue().getFirst() +
                    ';' +
                    entry.getValue().getSecond().format(formatter);
            query.outputLine(line);
        }
        query.logTime();

        query.shutdown();
    }
}
