package com.phuclq.student.lottery.ingest;

import com.google.firebase.database.annotations.NotNull;

import java.util.List;

public class DrawPayload {

    @NotNull
    public String region; // MB/MN/MT
    public String province;        // null for MB
    @NotNull public String drawDate; // yyyy-MM-dd

    @NotNull public List<ResultItem> results;

    public static class ResultItem {
        @NotNull public String prize; // DB,G1,G2,...
        public Integer seq = 1;
        @NotNull public String number;
    }
}
