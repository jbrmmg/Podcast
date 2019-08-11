package com.jbr.middletier.podcast.control;

import com.jbr.middletier.podcast.data.Podcast;
import com.jbr.middletier.podcast.dataaccess.PodcastRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by jason on 31/05/16.
 *
 *  curl -X GET -H "Content-type: application/json" http://localhost:13001/jbr/ext/log/type
 */

@SuppressWarnings("WeakerAccess")
@Controller
@RequestMapping("/jbr/ext/podcast/type")
public class PodcastController {
    final static private Logger LOG = LoggerFactory.getLogger(PodcastController.class);

    @SuppressWarnings("WeakerAccess")
    final
    PodcastRepository podcastRepository;

    @Autowired
    public PodcastController(PodcastRepository podcastRepository) {
        this.podcastRepository = podcastRepository;
    }

    @RequestMapping(method= RequestMethod.GET)
    public @ResponseBody Iterable<Podcast> getPodcasts() {
        LOG.info("Request for podcasts.");
        return podcastRepository.findAll();
    }
}
