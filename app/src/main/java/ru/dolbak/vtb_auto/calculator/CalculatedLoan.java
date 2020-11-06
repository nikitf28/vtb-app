package ru.dolbak.vtb_auto.calculator;

public class CalculatedLoan {
    public class Range {
        public boolean filled;
        public int max;
        public int min;
    }

    public class Program {
        public Range cost;
        public String id;
        public String programName;
        public String programUrl;
        public String requestUrl;
    }

    public class Ranges {
        // cost - undefined по доке, втф?
        public Range initialFee;
        public Range residualPayment;
        public Range term;
    }

    public Program program;
    public Ranges ranges;
    public GraphSettings result;
}
