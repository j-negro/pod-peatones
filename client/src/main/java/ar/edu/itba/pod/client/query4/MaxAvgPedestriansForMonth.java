package ar.edu.itba.pod.client.query4;

import ar.edu.itba.pod.api.models.Pair;

import java.math.BigDecimal;

public class MaxAvgPedestriansForMonth extends Pair<String, BigDecimal> {
    public MaxAvgPedestriansForMonth(String month, BigDecimal maxMonthlyAvg) {
        super(month, maxMonthlyAvg);
    }

    public String getMonth() {
        return getFirst();
    }

    public BigDecimal getMaxMonthlyAvg() {
        return getSecond();
    }
}
