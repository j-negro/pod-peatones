package ar.edu.itba.pod.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStampLogger {

    private static final Logger logger = LoggerFactory.getLogger(TimeStampLogger.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSSS");
    private Status status;
    private final FileWriter writer;

    public TimeStampLogger(Path outPath) throws IOException {
        this.status = Status.START_READ;
        this.writer = new FileWriter(outPath.toFile());
    }

    public void log() {
        Date now = new Date();
        logger.info("{} - {}", sdf.format(now), status);
        try {
            writer.write(sdf.format(now) + " - " + status + '\n');
        } catch (Exception e) {
            logger.error("Could not log timestamps");
        } finally {
            next();
        }
    }

    private void next() {
        switch (status) {
            case START_READ:
                status = Status.END_READ;
                break;
            case END_READ:
                status = Status.START_MAPREDUCE;
                break;
            case START_MAPREDUCE:
                status = Status.END_MAPREDUCE;
                break;
            case END_MAPREDUCE:
                status = null;
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.error("Could not log timestamps");
                }
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private enum Status {
        START_READ("Inicio de la lectura del archivo"),
        END_READ("Fin de lectura del archivo"),
        START_MAPREDUCE("Inicio del trabajo map/reduce"),
        END_MAPREDUCE("Fin del trabajo map/reduce");

        private final String s;

        Status(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }
    }
}
