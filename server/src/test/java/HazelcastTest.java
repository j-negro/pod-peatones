import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.junit.Test;

import java.util.Map;

public class HazelcastTest {

    @Test
    public void simpleTest() {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();

        Map<String, String> datos = hz.getMap("materias");
        datos.put("72.42", "POD");System.out.println( String.format("%d Datos en el cluster", datos.size() ));

        for (String key : datos.keySet()){
            System.out.println(String.format( "Datos con key %s= %s", key,datos.get(key)));}
    }

}
