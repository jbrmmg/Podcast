package com.jbr.middletier.podcast.schedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jason on 27/12/16.
 */
public class PodcastItem {
    private String title;
    private String description;
    private String author;
    private String guid;
    private String sourceURL;
    private int fileSize;
    private Date publishDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setEnclosure(String sourceURL, int fileSize) {
        this.sourceURL = sourceURL;
        this.fileSize = fileSize;
    }

    public void setPublishDate(String pubDate) {
        // Default the publish date to today.
        Calendar cal = Calendar.getInstance();
        publishDate = cal.getTime();

        try {
            //Fri, 28 Dec 2012 00:00:00 +0000
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyyy HH:mm:ss +SSSS");
            publishDate = formatter.parse(pubDate);
        } catch (Exception e) {
            // Ignore if it can't be converted.
        }
    }

    public Date getPublishDate() {
        return this.publishDate;
    }

    public String getSourceURL() {
        return sourceURL;
    }

    public int getFileSize() {
        return fileSize;
    }

    @Override
    public String toString() {
        return "PodcastItem [title=" + title + ", description=" + description
                + ", link=" + sourceURL + ", author=" + author + ", guid=" + guid
                + "]";
    }
}
