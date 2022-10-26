package ar.edu.itba.pod.client;

import ar.edu.itba.pod.api.collators.FourthQueryCollator;
import ar.edu.itba.pod.api.mappers.FourthQueryMapper;
import ar.edu.itba.pod.api.models.Pair;
import ar.edu.itba.pod.api.models.Reading;
import ar.edu.itba.pod.api.reducers.FourthQueryReducerFactory;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ExecutionException;

public class FourthQueryClient extends Query {

    private static final Logger LOGGER = LoggerFactory.getLogger(FourthQueryClient.class);

    public FourthQueryClient() {
        super(4);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Query query = new FourthQueryClient();

        query.logTime();
        query.setupSensorsMap();
        IList<Reading> readings = query.setupReadingsList();
        query.logTime();

        JobTracker t = query.client.getJobTracker("g8-max-avg");
        final KeyValueSource<String, Reading> sourceList = KeyValueSource.fromList(readings);
        Job<String, Reading> job = t.newJob(sourceList);

        long year = 0;
        try {
            year = Long.parseLong(System.getProperty("year", "2020"));
        } catch(NumberFormatException e) {
            LOGGER.error(e.getMessage());
            query.shutdown();
            System.exit(1);
        }

        long n = 5;
        try {
            n = Long.parseLong(System.getProperty("n", "5"));
        } catch(NumberFormatException e) {
            LOGGER.error(e.getMessage());
            query.shutdown();
            System.exit(1);
        }

        query.logTime();
        ICompletableFuture<SortedSet<Map.Entry<String, Pair<String, Double>>>> future =
                job.mapper(new FourthQueryMapper(year)).reducer(new FourthQueryReducerFactory(Math.toIntExact(year))).submit(new FourthQueryCollator(n));
        SortedSet<Map.Entry<String, Pair<String, Double>>> result = future.get();

        query.outputLine("Sensor;Max_Reading_Count;Max_Reading_DateTime");
        for(Map.Entry<String, Pair<String, Double>> entry : result) {
            String line = entry.getKey() +
                    ';' +
                    entry.getValue().getFirst() +
                    ';' +
                    String.format("%.2f", entry.getValue().getSecond());
            query.outputLine(line);
        }
        query.logTime();

        query.shutdown();
    }
}
