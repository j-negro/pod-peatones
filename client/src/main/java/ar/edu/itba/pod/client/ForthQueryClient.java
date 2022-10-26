package ar.edu.itba.pod.client;

import ar.edu.itba.pod.api.collators.ForthQueryCollator;
import ar.edu.itba.pod.api.collators.ThirdQueryCollator;
import ar.edu.itba.pod.api.mappers.ForthQueryMapper;
import ar.edu.itba.pod.api.mappers.ThirdQueryMapper;
import ar.edu.itba.pod.api.models.Pair;
import ar.edu.itba.pod.api.models.Reading;
import ar.edu.itba.pod.api.reducers.ForthQueryReducerFactory;
import ar.edu.itba.pod.api.reducers.ThirdQueryReducerFactory;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ExecutionException;

public class ForthQueryClient extends Query {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForthQueryClient.class);

    public ForthQueryClient() {
        super(4);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Query query = new ForthQueryClient();

        query.logTime();
        query.setupSensorsMap();
        IList<Reading> readings = query.setupReadingsList();
        query.logTime();

        JobTracker t = query.client.getJobTracker("g8-max-avg");
        final KeyValueSource<String, Reading> sourceList = KeyValueSource.fromList(readings);
        Job<String, Reading> job = t.newJob(sourceList);

        long year = 0;
        try {
            year = Long.parseLong(System.getProperty("year", "0"));
        } catch(NumberFormatException e) {
            LOGGER.error(e.getMessage());
            query.shutdown();
            System.exit(1);
        }

        long n = 0;
        try {
            n = Long.parseLong(System.getProperty("n", "0"));
        } catch(NumberFormatException e) {
            LOGGER.error(e.getMessage());
            query.shutdown();
            System.exit(1);
        }

        query.logTime();
        ICompletableFuture<SortedSet<Map.Entry<String, Pair<String, Double>>>> future =
                job.mapper(new ForthQueryMapper(year, n)).reducer(new ForthQueryReducerFactory()).submit(new ForthQueryCollator());
        SortedSet<Map.Entry<String, Pair<String, Double>>> result = future.get();

        query.outputLine("Sensor;Max_Reading_Count;Max_Reading_DateTime");
        for(Map.Entry<String, Pair<String, Double>> entry : result) {
            String line = entry.getKey() +
                    ';' +
                    entry.getValue().getFirst() +
                    ';' +
                    entry.getValue().getSecond();
            query.outputLine(line);
        }
        query.logTime();

        query.shutdown();
    }
}
