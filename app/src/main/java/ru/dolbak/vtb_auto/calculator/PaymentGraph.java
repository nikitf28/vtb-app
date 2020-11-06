package ru.dolbak.vtb_auto.calculator;

public class PaymentGraph {
    public class Payment {
        public int order;
        public int percent;
        public int debt;
        public int payment;
        public int balanceOut;
    }

    public Payment[] payments;
}
