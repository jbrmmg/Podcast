package com.jbr.middletier.podcast.data;

import javax.persistence.*;

/**
 * Created by jason on 25/12/16.
 */
@SuppressWarnings("unused")
@Entity
@Table(name="podcast")
public class Podcast {
    @Id
    @Column(name="id")
    private String id;

    @Column(name="source")
    private String source;

    @Column(name="directory")
    private String directory;

    @Column(name="name")
    private String name;

    @Column(name="datefile")
    private String datefile;

    protected Podcast() { }

    public String getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getDirectory() {
        return directory;
    }

    public String getName() {
        return name;
    }

    public boolean useDateInFilename() { return !datefile.equalsIgnoreCase("N"); }
}
