package ar.edu.itba.pod.client;

import ar.edu.itba.pod.api.colators.FirstQueryColator;
import ar.edu.itba.pod.api.mappers.FirstQueryMapper;
import ar.edu.itba.pod.api.models.Reading;
import ar.edu.itba.pod.api.reducers.FirstQueryReducerFactory;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirstQueryClient extends Query {
    private static final Logger LOGGER = LoggerFactory.getLogger(FirstQueryClient.class);
    public FirstQueryClient() {
        super(1);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        FirstQueryClient query = new FirstQueryClient();

        query.logTime();
        query.setupSensorsMap();
        IList<Reading> readings = query.setupReadingsList();
        query.logTime();

        JobTracker t = query.client.getJobTracker("g8-citizen-count");
        final KeyValueSource<String, Reading> sourceList = KeyValueSource.fromList(readings);
        Job<String, Reading> job = t.newJob(sourceList);

        query.logTime();
        ICompletableFuture<List<Map.Entry<String, Long>>> future = job.mapper( new FirstQueryMapper() )
                .reducer( new FirstQueryReducerFactory() ).submit( new FirstQueryColator());

        List<Map.Entry<String, Long>> result = future.get();

        query.outputLine("Sensor;Total_Count");
        for(Map.Entry<String, Long> sensor: result){
            query.outputLine(sensor.getKey() + ";" + sensor.getValue());
        }
        query.logTime();

        query.shutdown();
    }
}
