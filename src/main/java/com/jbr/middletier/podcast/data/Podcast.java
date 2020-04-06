package com.jbr.middletier.podcast.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
    @NotNull
    private String source;

    @Column(name="directory")
    @NotNull
    private String directory;

    @Column(name="name")
    @NotNull
    private String name;

    @Column(name="datefile")
    @NotNull
    private Boolean datefile;

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

    public Boolean useDateInFilename() { return this.datefile; }
}
