package ua.azbest.csstatservice.model;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public class Statistic {

    private final Picture picture;
    private final List<Record> list;
    private final Data data;

    public Statistic(Picture picture, List<Record> list) {
        this.picture = picture;
        this.list = list;
        data = calcData();
    }

    public int getTotal() {
        return data.getTotal();
    }

    public int getLeft() {
        return data.getLeft();
    }

    public Data getData() {
        return data;
    }

    private Data calcData() {
        Data result = new Data();

        if (list.size() == 0) {
            result.setTotal(0);
            result.setLeft(picture.getCrossStitchCount());
            result.setFirstDay(LocalDate.now());
            result.setLastDay(LocalDate.now());
        } else {
            result.setTotal(
                    list.stream().mapToInt(Record::getCrosses).sum());
            result.setLeft(
                    picture.getCrossStitchCount() - result.getTotal());
            result.setFirstDay(
                    list.stream().min(Comparator.comparing(Record::getDate)).orElse(null).getDate()
            );
            result.setLastDay(
                    list.stream().max(Comparator.comparing(Record::getDate)).orElse(null).getDate()
            );

            result.setAllCharacteristics(calcCharacteristics(list));
            result.setCharacteristicsByWeekday(0, calcCharacteristics(list.stream()
                    .filter(rec -> rec.getDate().getDayOfWeek().equals(DayOfWeek.MONDAY))
                    .filter(rec -> rec.getCrosses() < 10000)
                    .collect(Collectors.toList())));
            result.setCharacteristicsByWeekday(1, calcCharacteristics(list.stream()
                    .filter(rec -> rec.getDate().getDayOfWeek().equals(DayOfWeek.TUESDAY))
                    .filter(rec -> rec.getCrosses() < 10000)
                    .collect(Collectors.toList())));
            result.setCharacteristicsByWeekday(2, calcCharacteristics(list.stream()
                    .filter(rec -> rec.getDate().getDayOfWeek().equals(DayOfWeek.WEDNESDAY))
                    .filter(rec -> rec.getCrosses() < 10000)
                    .collect(Collectors.toList())));
            result.setCharacteristicsByWeekday(3, calcCharacteristics(list.stream()
                    .filter(rec -> rec.getDate().getDayOfWeek().equals(DayOfWeek.THURSDAY))
                    .filter(rec -> rec.getCrosses() < 10000)
                    .collect(Collectors.toList())));
            result.setCharacteristicsByWeekday(4, calcCharacteristics(list.stream()
                    .filter(rec -> rec.getDate().getDayOfWeek().equals(DayOfWeek.FRIDAY))
                    .filter(rec -> rec.getCrosses() < 10000)
                    .collect(Collectors.toList())));
            result.setCharacteristicsByWeekday(5, calcCharacteristics(list.stream()
                    .filter(rec -> rec.getDate().getDayOfWeek().equals(DayOfWeek.SATURDAY))
                    .filter(rec -> rec.getCrosses() < 10000)
                    .collect(Collectors.toList())));
            result.setCharacteristicsByWeekday(6, calcCharacteristics(list.stream()
                    .filter(rec -> rec.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY))
                    .filter(rec -> rec.getCrosses() < 10000)
                    .collect(Collectors.toList())));
        }
        return result;
    }

    public double getPercentLeft() {
        return ((double) getLeft()) / picture.getCrossStitchCount() * 100;
    }

    public double getPercentDone() {
        return 100. - getPercentLeft();
    }

    public int getCrossesPerDay() {
        return (int) (getLeft() / DAYS.between(LocalDate.now(), picture.getWishDate()));
    }

    public int getAveragePerDay() {
        int days = (int) DAYS.between(data.getFirstDay(), data.getLastDay());
        if (days != 0)
            return getTotal() / (days + 1);
        else if (list.size() == 0)
            return 0;
        //TODO re-calc if list contains few records in one single day
        return list.get(0).getCrosses();
    }

    public int getActionDays() {
        return list.size();
    }

    public int AveragePerActionDay() {
        return getTotal() / getActionDays();
    }

    public int getDaysLeft() {
        if (list.size() == 0)
            return 0;
        return getLeft() / getAveragePerDay();
    }

    public String getApproximateFinishDate() {
        LocalDate date = DAYS.addTo(LocalDate.now(), getDaysLeft());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return formatter.format(date);
    }

    public double getCharacteristicByWeekday(StatisticCharacteristic sch, int weekday) {
        switch (sch) {
            case COUNT: return data.characteristicsArrayByWeekday[weekday].count;
            case SUM: return data.characteristicsArrayByWeekday[weekday].sum;
            case VAR: return data.characteristicsArrayByWeekday[weekday].var;
            case AVERAGE: return data.characteristicsArrayByWeekday[weekday].average;
            case SIGMA: return data.characteristicsArrayByWeekday[weekday].sigma;
            case DELTA: return data.characteristicsArrayByWeekday[weekday].delta;
        }
        return 0;
    }

    private static class Characteristics {
        public int count;
        public int sum;
        public double average;
        public double var;
        public double sigma;
        public double delta;

        public Characteristics(int count, int sum, double average, double var, double sigma, double delta) {
            this.count = count;
            this.sum = sum;
            this.average = average;
            this.var = var;
            this.sigma = sigma;
            this.delta = delta;
        }
    }

    private static class Data {

        int total;
        int left;
        LocalDate firstDay;
        LocalDate lastDay;
        Characteristics[] characteristicsArrayByWeekday = new Characteristics[7];
        Characteristics allCharacteristics;

        public Characteristics[] getCharacteristicsArrayByWeekday() {
            return characteristicsArrayByWeekday;
        }

        public Characteristics getAllCharacteristics() {
            return allCharacteristics;
        }

        public void setAllCharacteristics(Characteristics allCharacteristics) {
            this.allCharacteristics = allCharacteristics;
        }

        public void setCharacteristicsArrayByWeekday(Characteristics[] characteristicsArrayByWeekday) {
            this.characteristicsArrayByWeekday = characteristicsArrayByWeekday;
        }

        public void setCharacteristicsByWeekday(int weekday, Characteristics characteristics) {
            characteristicsArrayByWeekday[weekday] = characteristics;
        }

        public Characteristics getCharacteristicsByWeekday(int weekday) {
            return characteristicsArrayByWeekday[weekday];
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
            this.left = left;
        }

        public LocalDate getFirstDay() {
            return firstDay;
        }

        public void setFirstDay(LocalDate firstDay) {
            this.firstDay = firstDay;
        }

        public LocalDate getLastDay() {
            return lastDay;
        }

        public void setLastDay(LocalDate lastDay) {
            this.lastDay = lastDay;
        }
    }

    @Contract("_ -> new")
    private @NotNull Characteristics calcCharacteristics(@NotNull List<Record> records) {
        int count = records.size();
        int sum = records.stream().mapToInt(Record::getCrosses).sum();
        double mean = (double) sum / records.size();
        double variance;
        if (count < 2)
            variance = 0;
        else {
            double sumSqr = 0.0;
            for (Record rec : records) {
                sumSqr += Math.pow(rec.getCrosses() - mean, 2);
            }
            variance = sumSqr / (count - 1);
        }
        double stdDev = Math.sqrt(variance);
        double delta = sum / stdDev;
        return new Characteristics(count, sum, mean, variance, stdDev, delta);
    }

    private static double getMean(@NotNull List<Record> records) {
        int sum = records.stream().mapToInt(Record::getCrosses).sum();
        return (double) sum / records.size();
    }

    private static double getVariance(@NotNull List<Record> records) {
        int size = records.size();
        if (size < 2)
            return 0;
        double mean = getMean(records);
        double sum = 0.0;
        for (Record rec : records) {
            sum += Math.pow(rec.getCrosses() - mean, 2);
        }
        return sum / (size - 1);
    }
}
