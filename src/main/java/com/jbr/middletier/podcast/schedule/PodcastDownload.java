package com.jbr.middletier.podcast.schedule;

import com.jbr.middletier.podcast.manage.PodcastManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * Created by jason on 25/12/16.
 */

@Component
public class PodcastDownload {
    private final
    PodcastManager manager;

    @Autowired
    public PodcastDownload(PodcastManager manager) {
        this.manager = manager;
    }

    @Scheduled(cron = "${podcast.schedule}")
    public void podcastDownload() {
        manager.download();
    }
}
