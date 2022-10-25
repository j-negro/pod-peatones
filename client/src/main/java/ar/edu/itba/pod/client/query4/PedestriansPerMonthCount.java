package ar.edu.itba.pod.client.query4;

import ar.edu.itba.pod.api.models.Pair;

import java.math.BigDecimal;

public class PedestriansPerMonthCount extends Pair<String, Integer> {
    public PedestriansPerMonthCount(String month, Integer pedestrianCount) {
        super(month, pedestrianCount);
    }

    public String getMonth() {
        return getFirst();
    }

    public Integer getPedestrianCount() {
        return getSecond();
    }
}
