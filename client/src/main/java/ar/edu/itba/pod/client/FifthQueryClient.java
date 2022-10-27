package ar.edu.itba.pod.client;

import ar.edu.itba.pod.api.collators.FifthQueryCollator;
import ar.edu.itba.pod.api.mappers.FifthQueryMapper;
import ar.edu.itba.pod.api.mappers.FirstQueryMapper;
import ar.edu.itba.pod.api.models.Pair;
import ar.edu.itba.pod.api.models.Reading;
import ar.edu.itba.pod.api.reducers.FifthQueryReducerFactory;
import ar.edu.itba.pod.api.reducers.FirstQueryReducerFactory;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class FifthQueryClient extends Query {

    private static final Logger LOGGER = LoggerFactory.getLogger(FifthQueryClient.class);

    public FifthQueryClient() {
        super(5);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FifthQueryClient query = new FifthQueryClient();

        query.logTime();
        query.setupSensorsMap();
        IList<Reading> readings = query.setupReadingsList();
        query.logTime();

        JobTracker t = query.client.getJobTracker("g8-pair-by-millions");
        final KeyValueSource<String, Reading> sourceList = KeyValueSource.fromList(readings);
        Job<String, Reading> job = t.newJob(sourceList);

        query.logTime();
        ICompletableFuture<Map<String, Long>> future = job
                .mapper(new FirstQueryMapper())
                .reducer(new FirstQueryReducerFactory())
                .submit();
        Map<String, Long> result = future.get();

        IMap<String, Long> countMap = query.client.getMap("g8-totalCount");
        countMap.clear();
        countMap.putAll(result);

        JobTracker t2 = query.client.getJobTracker("g8-pair-by-millions");
        final KeyValueSource<String, Long> countSource = KeyValueSource.fromMap(countMap);
        Job<String, Long> job2 = t2.newJob(countSource);

        ICompletableFuture<Collection<Map.Entry<Long, Pair<String, String>>>> billionPairings = job2
                .mapper(new FifthQueryMapper())
                .reducer(new FifthQueryReducerFactory())
                .submit(new FifthQueryCollator());
        Collection<Map.Entry<Long, Pair<String, String>>> billionSet = billionPairings.get();

        query.outputLine("Group;Sensor A;Sensor B");
        for(Map.Entry<Long, Pair<String, String>> me : billionSet) {
            String sb = String.valueOf(me.getKey() * 1000000) +
                    ';' +
                    me.getValue().getFirst() +
                    ';' +
                    me.getValue().getSecond();
            query.outputLine(sb);
        }
        query.logTime();

        query.shutdown();
    }
}
