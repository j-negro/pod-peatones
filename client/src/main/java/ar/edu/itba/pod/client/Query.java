package ar.edu.itba.pod.client;

import ar.edu.itba.pod.api.models.Reading;
import ar.edu.itba.pod.api.models.Sensor;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

import static java.lang.System.*;

public abstract class Query {

    private final String inPath = getProperty("inPath", "/afs/it.itba.edu.ar/pub/pod");
    private static final Logger LOGGER = LoggerFactory.getLogger(Query.class);
    private final TimeStampLogger timeStampLogger;
    private final BufferedWriter writer;
    private final Path outPath;

    protected final HazelcastInstance client;

    public Query(int number) {
        ClientConfig clientConfig = new ClientConfig();

        // Group Config
        GroupConfig groupConfig = new GroupConfig().setName("g8").setPassword("g8-pass");
        clientConfig.setGroupConfig(groupConfig);

        // Client Network Config
        String addresses = getProperty("addresses", "127.0.0.1:5701");

        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
        clientNetworkConfig.addAddress(addresses.split(";"));
        clientConfig.setNetworkConfig(clientNetworkConfig);

        this.client = HazelcastClient.newHazelcastClient(clientConfig);

        // Setup logger
        TimeStampLogger timeStampLogger;
        String outPath = getProperty("outPath", "/afs/it.itba.edu.ar/pub/pod-write/g8");
        Path timePath = Path.of(outPath, "time" + number + ".txt");
        try {
            timeStampLogger = new TimeStampLogger(timePath);
        } catch (IOException e) {
            timeStampLogger = null;
            LOGGER.error("Could not create TimeStampLogger on path {}, {}", timePath, e.getMessage());
        }
        this.timeStampLogger = timeStampLogger;

        // Setup outPath
        this.outPath = Path.of(outPath, "query" + number + ".csv");
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(this.outPath.toFile()));
        } catch (IOException e) {
            writer = null;
            LOGGER.error("Could not open output file on path {}, {}", this.outPath, e.getMessage());
            exit(1);
        }
        this.writer = writer;
    }

    public IMap<Integer, Sensor> setupSensorsMap() {
        IMap<Integer, Sensor> map = client.getMap("sensors");

        Path path = Path.of(inPath, "sensors.csv");
        Collection<Sensor> sensors = null;

        try {
            sensors = Util.readSensors(path);
        } catch (IOException e) {
            LOGGER.error("Could not import sensors from file {}, {}", path, e.getMessage());
            exit(1);
        }

        Util.importSensors(map, sensors);

        return map;
    }

    public IList<Reading> setupReadingsList() {
        IList<Reading> list = client.getList("readings");

        Path path = Path.of(inPath, "readings.csv");
        Collection<Reading> readings = null;

        try {
            readings = Util.readReadings(path);
        } catch (IOException e) {
            LOGGER.error("Could not import readings from file {}, {}", path, e.getMessage());
            exit(1);
        }

        Util.importReadings(list, readings);

        return list;
    }

    // NOTE: Created to avoid NullPointerException
    public void logTime() {
        if (timeStampLogger != null) {
            timeStampLogger.log();
        }
    }

    public void outputLine(String line) {
        try {
            writer.write(line);
        } catch (IOException e) {
            LOGGER.error("Could not output result to {}, {}", this.outPath, e.getMessage());
            System.exit(1);
        }
    }
}
