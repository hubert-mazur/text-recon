package histogram;

import java.util.ArrayList;

public class Histogram {

    private double beginValue;
    private double endValue;
    private double step;
    private histogramInterval root;
    private ArrayList<histogramInterval> bin;
    private double countsSum;

    private class histogramInterval {
        private double begin;
        private double end;
        private int counts;

        public histogramInterval(double begin, double end) {
            this.begin = begin;
            this.end = end;
            this.counts = 0;
        }

        public double getBegin() {
            return begin;
        }

        public double getEnd() {
            return end;
        }

        public int getCounts() {
            return counts;
        }

        public boolean valueInInterval(double value) {
            if (this.begin <= value && value < this.end) {
                this.counts++;
                return true;
            }
            return false;
        }
    }

    public Histogram(double beginValue, double step, double endValue) {

        this.beginValue = beginValue;
        this.step = step;
        this.endValue = endValue;

        this.bin = new ArrayList<histogramInterval>();

        for (double i = beginValue; i <= endValue - step; i += step) {
            this.bin.add(new histogramInterval(i, i + step));
        }

        if (this.bin.get(this.bin.size() - 1).getEnd() != this.endValue)
            this.bin.add(new histogramInterval((this.bin.get(this.bin.size() - 1).getEnd()), this.endValue));

    }

    public void insert(double value) {
        for (var i : bin) {
            if (i.valueInInterval(value))
                break;
        }
    }

    public void print() {
        for (var i : bin) {
            System.out.println(i.counts + " in <" + i.begin + ";" + i.end + ")");
        }
    }

    public int getLength() {
        return this.bin.size();
    }

    public double sum() {
        double sum = 0;

        for (var i : this.bin)
            sum += i.counts;
        return sum;
    }

    public int getCount(int index) {
        return this.bin.get(index).counts;
    }

    public double getBeginOf(int index) {
        return this.bin.get(index).begin;
    }

    public void reset() {
        for (var i : this.bin) {
            i.counts = 0;
        }
    }
}


