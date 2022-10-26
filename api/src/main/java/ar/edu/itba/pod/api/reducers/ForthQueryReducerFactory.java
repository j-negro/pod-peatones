package ar.edu.itba.pod.api.reducers;

import ar.edu.itba.pod.api.models.Pair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

public class ForthQueryReducerFactory implements
        ReducerFactory<String, Pair<String, Integer>, Pair<String, Double>> {

    private final int year;

    public ForthQueryReducerFactory(int year) {
        this.year = year;
    }

    @Override
    public Reducer<Pair<String, Integer>, Pair<String, Double>> newReducer(String s) {
        return new Reducer<>() {
            Map<String, Long> monthToCount = new HashMap<>();

            @Override
            public void reduce(Pair<String, Integer> entry) {
                if (entry.getFirst() == null || entry.getSecond() == null) return;

                String sensor = entry.getFirst();
                Integer count = entry.getSecond();
                monthToCount.compute(sensor, (k, v) -> (v == null) ? count : v + count);
            }

            @Override
            public Pair<String, Double> finalizeReduce() {
                Pair<String, Double> result = new Pair<>(null, null);

                for (Map.Entry<String, Long> entry : monthToCount.entrySet()) {
                    Double currAvg = Double.valueOf(entry.getValue()) / getDayCountInMonth(year, getMonthOrdinalFromName(entry.getKey()));
                    if (result.getSecond() == null || result.getSecond() < currAvg) {
                        result.setFirst(entry.getKey());
                        result.setSecond(currAvg);
                    }
                }

                return result;
            }

            private int getMonthOrdinalFromName(String name) {
                Date date = null;
                try {
                    date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(name);
                } catch (ParseException e) {
                    System.err.println("ERROR " + name);
                    System.err.flush();
                    System.exit(1);
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                return cal.get(Calendar.MONTH);
            }

            private int getDayCountInMonth(int year, int month) {
                YearMonth yearMonthObject = YearMonth.of(year, month+1);
                int daysInMonth = yearMonthObject.lengthOfMonth();
                return daysInMonth;
            }
        };
    }
}
