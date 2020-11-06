package ru.dolbak.vtb_auto.calculator;

import com.google.gson.annotations.SerializedName;

public class FinalizedLoanResponse {
    public class Application {
        public class Report {
            @SerializedName("application_status")
            public String applicationStatus;
            public String comment;
            @SerializedName("decision_date")
            public String decisionDate;
            @SerializedName("decision_end_date")
            public String decisionEndDate;
            @SerializedName("monthly_payment")
            public int monthlyPayment;
        }

        @SerializedName("VTB_client_ID")
        public int VTBClientID;
        @SerializedName("decision_report")
        public Report decisionReport;
    }

    public Application application;
    public String datetime;
}
