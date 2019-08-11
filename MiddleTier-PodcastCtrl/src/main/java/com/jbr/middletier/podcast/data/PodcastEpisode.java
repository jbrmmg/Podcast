package com.jbr.middletier.podcast.data;

import com.jbr.middletier.podcast.schedule.PodcastItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Calendar;

/**
 * Created by jason on 25/12/16.
 */
@Entity
@Table(name="podcastepisode")
public class PodcastEpisode {
    final static private Logger LOG = LoggerFactory.getLogger(PodcastEpisode.class);

    @Id
    @Column(name="guid")
    private String guid;

    @Column(name="`source`")
    private String source;

    @Column(name="title")
    private String title;

    @Column(name="filename")
    private String filename;

    @Column(name="fileexists")
    private String fileExists;

    @Column(name="filesize")
    private int fileSize;

    @Column(name="podcastid")
    private String podcastId;

    @Column(name="deletefile")
    private String deleteFile;

    @Column(name="`ignore`")
    private String ignore;

    @Column(name="createdate")
    private int createDate;

    @Column(name="updatedate")
    private int updateDate;

    private PodcastEpisode() { }

    private String getFilenameFromUrn(PodcastItem source) {
        LOG.info("Get filename from URN : {}",source.getGuid());
        String[] guidElements = source.getGuid().split(":");
        return guidElements[guidElements.length-1];
    }

    private String getFilenameFromSourceUrl(PodcastItem source) {
        LOG.info("Get filename from URL : {}",source.getSourceURL());
        String[] sourceFileElements = source.getSourceURL().split("/");
        String[] filenameElements = sourceFileElements[sourceFileElements.length-1].split("\\.");
        return filenameElements[0];
    }

    private String getFilename(PodcastItem source,boolean useDateInFilename) {
        // Filename should always begin with YYYYDDMM.
        Calendar cal = Calendar.getInstance();
        cal.setTime(source.getPublishDate());

        // Get date string
        String dateString = Integer.toString(cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DAY_OF_MONTH));

        // Get the filename
        String filename;
        if(source.getGuid().startsWith("urn")) {
            filename = getFilenameFromUrn(source);
        } else {
            filename = getFilenameFromSourceUrl(source);
        }

        // Return the formatted date.
        if(useDateInFilename) {
            return String.format("%s %s.mp3", dateString, filename);
        }

        return String.format("%s.mp3", filename);
    }

    public PodcastEpisode(Podcast podcast, String filename) {
        this.guid = filename;
        this.source = "";
        this.filename = filename;
        this.fileSize = 0;
        this.fileExists = "Y";
        this.deleteFile = "N";
        this.ignore = "N";
        this.podcastId = podcast.getId();
        this.title = String.format("File: %s",filename);

        LOG.info("Filename: {} (existing)",this.filename);

        Calendar cal = Calendar.getInstance();
        this.createDate = cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DAY_OF_MONTH);
        this.updateDate = this.createDate;
    }

    public PodcastEpisode(Podcast podcast, PodcastItem source)
    {
        this.guid = source.getGuid();
        this.source = source.getSourceURL();
        this.filename = getFilename(source,podcast.useDateInFilename());
        this.fileSize = source.getFileSize();
        this.fileExists = "N";
        this.deleteFile = "N";
        this.ignore = "N";
        this.podcastId = podcast.getId();
        this.title = source.getTitle().substring(0, source.getTitle().length() >= 250 ? 250 : source.getTitle().length());

        LOG.info("Filename: {}",this.filename);

        Calendar cal = Calendar.getInstance();
        this.createDate = cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DAY_OF_MONTH);
        this.updateDate = this.createDate;
    }

    public void update(PodcastItem source) {
        Calendar cal = Calendar.getInstance();
        this.updateDate = cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DAY_OF_MONTH);
    }

    public String getId() {
        return guid;
    }

    public String getSource() {
        return source;
    }

    public String getFilename() {
        return filename;
    }

    public boolean fileExists() {
        return fileExists.equalsIgnoreCase("Y");
    }

    public void fileCreated() { this.fileExists = "Y"; }

    public boolean deleteFile() {
        return deleteFile.equalsIgnoreCase("Y");
    }

    public boolean ignore() {
        return ignore.equalsIgnoreCase("Y");
    }

    public int getCreateDate() {
        return createDate;
    }

    public int getUpdateDate() {
        return updateDate;
    }

    public void markForDelete() {
        deleteFile = "Y";
        ignore = "Y";
    }

    public String getPodcastId() {
        return podcastId;
    }

    public boolean updatedRecently() {
        Calendar recentDate = Calendar.getInstance();
        recentDate.add(Calendar.DAY_OF_MONTH,-7);

        Calendar updateDateCal = Calendar.getInstance();

        int updateYear = updateDate / 10000;
        int updateMonth = (updateDate - updateYear * 1000) / 100;
        int updateDay =  updateDate - updateYear * 1000 - updateMonth * 100;

        updateDateCal.set(updateYear, updateMonth, updateDay);

        return updateDateCal.after(recentDate);
    }

    public void fileDeleted() {
        this.fileExists = "N";
    }
}
