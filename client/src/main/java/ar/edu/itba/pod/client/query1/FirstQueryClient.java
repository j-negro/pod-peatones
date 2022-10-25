package ar.edu.itba.pod.client.query1;

import ar.edu.itba.pod.api.mappers.FirstQueryMapper;
import ar.edu.itba.pod.api.models.Reading;
import ar.edu.itba.pod.api.models.Sensor;
import ar.edu.itba.pod.api.reducers.FirstQueryReducer;
import ar.edu.itba.pod.client.Util;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.lang.System.getProperty;

public class FirstQueryClient {

    private static final Logger logger = LoggerFactory.getLogger(FirstQueryClient.class);
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        String address = getProperty("address");
        if (address == null) {
            logger.error("Server address not provided. Shutting down...");
            System.exit(1);
        }
        String inPath = System.getProperty("inPath");
        if (inPath == null) {
            logger.error("inPath not provided. Shutting down...");
            System.exit(1);
        }
        String outPath = System.getProperty("outPath");
        if(outPath == null){
            logger.error("outPath not provided. Shutting down...");
            System.exit(1);
        }
        ClientConfig clientConfig = new ClientConfig();

        // Group Config
        GroupConfig groupConfig = new GroupConfig().setName("g8").setPassword("g8-pass");
        clientConfig.setGroupConfig(groupConfig);

        // Client Network Config
        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
        clientNetworkConfig.addAddress(address);
        clientConfig.setNetworkConfig(clientNetworkConfig);

        Collection<Reading> readingList = Util.readReadings(Paths.get(inPath));
        Collection<Sensor> sensorMap = Util.readSensors(Paths.get(outPath));

        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
        IMap<Integer, Sensor> imap = hazelcastInstance.getMap("sensors");
        IList<Reading> iList = hazelcastInstance.getList("readings");
        Util.importReadings(iList, readingList);
        Util.importSensors(imap, sensorMap);

        JobTracker t = hazelcastInstance.getJobTracker("citizen-count");
        final KeyValueSource<String, Reading> sourceList = KeyValueSource.fromList(iList);
        Job<String, Reading> job = t.newJob(sourceList);
        ICompletableFuture<Map<String, Long>> future = job.mapper( new FirstQueryMapper() ).reducer( new FirstQueryReducer() ).submit();
        Map<String, Long> result = future.get();
        for(String sensorName: result.keySet()){
            System.out.println(sensorName + ";" + result.get(sensorName)+'\n');
        }
        HazelcastClient.shutdownAll();
    }
}
