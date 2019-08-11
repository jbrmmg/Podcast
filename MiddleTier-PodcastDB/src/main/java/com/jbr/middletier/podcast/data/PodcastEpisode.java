package com.jbr.middletier.podcast.data;

import javax.persistence.*;

/**
 * Created by jason on 25/12/16.
 */
@SuppressWarnings("unused")
@Entity
@Table(name="podcastepisode")
public class PodcastEpisode {
    @SuppressWarnings("CanBeFinal")
    @Id
    @Column(name="guid")
    private String guid;

    @SuppressWarnings("CanBeFinal")
    @Column(name="title")
    private String title;

    @Column(name="filesize")
    private int fileSize;

    @Column(name="podcastid")
    private String podcastId;

    @Column(name="`ignore`")
    private String ignore;

    public String getId() {
        return guid;
    }

    public String getTitle() {
        return title;
    }
}
