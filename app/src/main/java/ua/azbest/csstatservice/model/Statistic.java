package ua.azbest.csstatservice.model;

import android.transition.Transition;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public class Statistic {

    private Picture picture;
    private List<Record> list;
    private Data data;

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

    private Data calcData() {
        Data result = new Data();

        if (list.size() == 0) {
            result.setTotal(0);
            result.setLeft(picture.getCrossStitchCount());
            result.setFirstDay(LocalDate.now());
            result.setLastDay(LocalDate.now());
        } else {
            result.setTotal(
                    list.stream().collect(Collectors.summingInt(Record::getCrosses)));
            result.setLeft(
                    picture.getCrossStitchCount() - result.getTotal());
            result.setFirstDay(
                    list.stream().min(Comparator.comparing(Record::getDate)).get().getDate()
            );
            result.setLastDay(
                    list.stream().max(Comparator.comparing(Record::getDate)).get().getDate()
            );
        }
        return result;
    }

    public double getPercentLeft() {
        return ((double) getLeft()) / picture.getCrossStitchCount() * 100;
    }

    public int getCrossesPerDay() {
        return (int) (getLeft() / DAYS.between(LocalDate.now(), picture.getWishDate()));
    }

    public int getAveragePerDay() {
        int days = (int) DAYS.between(data.getFirstDay(), data.getLastDay());
        if (days != 0)
            return (int) (getTotal() / (days + 1));
        else if (list.size() == 0)
            return 0;
        //TODO re-calc if list contains few records in one single day
        return list.get(0).getCrosses();
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

    private class Data {
        int total;
        int left;
        LocalDate firstDay;
        LocalDate lastDay;


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

}
