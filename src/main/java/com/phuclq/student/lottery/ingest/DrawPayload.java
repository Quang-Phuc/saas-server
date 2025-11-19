package com.phuclq.student.lottery.ingest;


import com.google.firebase.database.annotations.NotNull;

import java.util.List;

public class DrawPayload {
    @NotNull
    public String region;
    public String province;
    @NotNull
    public String drawDate;
    @NotNull
    public List<ResultItem> results;

    public static class ResultItem {
        @NotNull
        public String prize;
        public Integer seq = 1;
        @NotNull
        public String number;
    }
}
