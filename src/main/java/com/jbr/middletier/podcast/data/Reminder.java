package com.jbr.middletier.podcast.data;

import javax.persistence.*;

/**
 * Created by jason on 25/12/16.
 */
@SuppressWarnings("unused")
@Entity
@Table(name="reminder")
public class Reminder {
    @Id
    @Column(name="what")
    private String what;

    @Column(name="details")
    private String details;

    protected Reminder() { }

    public Reminder(ReminderRequest request) {
        this.what = request.getWhat();
        this.details = request.getDetail();
    }

    public String getDetails() {
        return details;
    }

    public String getWhat() {
        return what;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setWhat(String what) {
        this.what = what;
    }
}
