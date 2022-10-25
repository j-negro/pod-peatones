package ar.edu.itba.pod.client.query2;

import ar.edu.itba.pod.api.models.Pair;

public class WeekendCountPair extends Pair<Boolean, Integer> {
    public WeekendCountPair(Boolean isWeekend, Integer hourlyCount) {
        super(isWeekend, hourlyCount);
    }
    public boolean getIsWeekend() {
        return getFirst();
    }
    public int getHourlyCount() {
        return getSecond();
    }
}
