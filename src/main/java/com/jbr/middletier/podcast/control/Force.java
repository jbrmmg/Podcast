package com.jbr.middletier.podcast.control;

import com.jbr.middletier.podcast.data.StatusResponse;
import com.jbr.middletier.podcast.manage.PodcastManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by jason on 31/05/17.
 */

@Controller
@RequestMapping("/jbr/int/podcast/force")
class Force {
    final static private Logger LOG = LoggerFactory.getLogger(Force.class);

    private final PodcastManager manager;

    @Autowired
    public Force(PodcastManager manager) {
        this.manager = manager;
    }

    @RequestMapping(method= RequestMethod.POST)
    public @ResponseBody
    StatusResponse podcastDownload() {
        LOG.info("Force - perform the download.");
        manager.download();
        return new StatusResponse();
    }

}
