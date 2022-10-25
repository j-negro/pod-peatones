package ar.edu.itba.pod.client;

import ar.edu.itba.pod.api.collators.SecondQueryCollator;
import ar.edu.itba.pod.api.mappers.SecondQueryMapper;
import ar.edu.itba.pod.api.models.Pair;
import ar.edu.itba.pod.api.models.Reading;
import ar.edu.itba.pod.api.reducers.SecondQueryReducerFactory;
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

public class SecondQueryClient extends Query {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecondQueryClient.class);

    public SecondQueryClient() {
        super(2);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SecondQueryClient query = new SecondQueryClient();

        query.logTime();
        IList<Reading> readings = query.setupReadingsList();
        query.logTime();

        JobTracker t = query.client.getJobTracker("g8-count-by-year-and-weekday");
        final KeyValueSource<String, Reading> sourceList = KeyValueSource.fromList(readings);
        Job<String, Reading> job = t.newJob(sourceList);

        query.logTime();
        ICompletableFuture<SortedSet<Map.Entry<Integer, Pair<Integer, Integer>>>> future = job
                .mapper(new SecondQueryMapper())
                .reducer(new SecondQueryReducerFactory())
                .submit(new SecondQueryCollator());
        SortedSet<Map.Entry<Integer, Pair<Integer, Integer>>> result = future.get();
        query.logTime();

        query.outputLine("Year;Weekdays_Count;Weekends_Count;Total_Count");
        for (Map.Entry<Integer, Pair<Integer, Integer>> entry : result) {
            query.outputLine(entry.getKey() + ";" + entry.getValue().getFirst() + ";" + entry.getValue().getSecond()
                    + ";" + (entry.getValue().getFirst() + entry.getValue().getSecond()));
        }

        query.shutdown();
    }
}
