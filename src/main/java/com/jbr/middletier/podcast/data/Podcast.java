package com.jbr.middletier.podcast.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by jason on 25/12/16.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
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
        return this.id;
    }

    public String getSource() {
        return this.source;
    }

    public String getDirectory() {
        return this.directory;
    }

    public String getName() {
        return this.name;
    }

    public Boolean getDatefile() { return this.datefile; }

    @Override
    public String toString() {
        return this.id + ":" + this.source + ":" + this.directory + ":" + this.name + ":" + ( this.datefile ? "Y" : "N" );
    }
}
