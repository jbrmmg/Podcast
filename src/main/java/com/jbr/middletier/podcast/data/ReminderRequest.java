package com.jbr.middletier.podcast.data;

public class ReminderRequest {
    private String what;
    private String detail;

    public ReminderRequest() {
        this.what = "Unknown";
        this.detail = "None";
    }

    public String getWhat() {
        return what;
    }

    public String getDetail() {
        return detail;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
