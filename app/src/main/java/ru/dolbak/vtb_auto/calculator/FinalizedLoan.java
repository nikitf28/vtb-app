package ru.dolbak.vtb_auto.calculator;

import com.google.gson.annotations.SerializedName;

public class FinalizedLoan {
    public class CustomerParty {
        public class Person {
            @SerializedName("birth_date_time")
            public String birthDateTime;
            @SerializedName("birth_place")
            public String birthPlace;
            @SerializedName("family_name")
            public String familyName;
            @SerializedName("first_name")
            public String firstName;
            public String gender;
            @SerializedName("middle_name")
            public String middleName;
            @SerializedName("nationality_country_code")
            public String nationalityCountryCode;
        }

        public String email;
        @SerializedName("income_amount")
        public int incomeAmount;
        public Person person;
        public String datetime;
        @SerializedName("interest_rate")
        public double interestRate;
        @SerializedName("requested_amount")
        public int requestedAmount;
        @SerializedName("requested_term")
        public int requestedTerm;
        @SerializedName("trade_mark")
        public int tradeMark;
        @SerializedName("vehicle_cost")
        public int vehicleCost;
    }

    public String comment;
    public CustomerParty customerParty;
}
