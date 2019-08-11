package com.jbr.middletier.podcast.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by jason on 02/01/17.
 */
@Entity
@Table(name="podcast")
public class Podcast {
    @SuppressWarnings("CanBeFinal")
    @Id
    @Column(name="id")
    private String id;

    @SuppressWarnings("CanBeFinal")
    @Column(name="name")
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
