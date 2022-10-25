package ar.edu.itba.pod.client.query2;

import ar.edu.itba.pod.api.models.Pair;

public class WeekdayWeekendPair extends Pair<Integer, Integer> {
    public WeekdayWeekendPair(Integer weekdayCount, Integer weekendCount) {
        super(weekdayCount, weekendCount);
    }
    public int getWeekdayCount() {
        return getFirst();
    }
    public int getWeekendCount() {
        return getSecond();
    }
}
