package com.jbr.middletier.podcast.schedule;

import com.jbr.middletier.podcast.manage.PodcastManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * Created by jason on 25/12/16.
 */

@SuppressWarnings("WeakerAccess")
@Component
public class PodcastDownload {
    final static private Logger LOG = LoggerFactory.getLogger(PodcastDownload.class);

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
