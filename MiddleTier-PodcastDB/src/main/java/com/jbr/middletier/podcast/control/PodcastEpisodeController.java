package com.jbr.middletier.podcast.control;

import com.jbr.middletier.podcast.dataaccess.PodcastEpisodeRepository;
import com.jbr.middletier.podcast.dataaccess.PodcastEpisodeSpecifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

/**
 * Created by jason on 02/01/17.
 */

@Controller
@RequestMapping("/jbr/ext/podcast/episode")
class PodcastEpisodeController {
    final static private Logger LOG = LoggerFactory.getLogger(PodcastEpisodeController.class);

    private final
    PodcastEpisodeRepository podcastEpisodeRepository;

    @Autowired
    public PodcastEpisodeController(PodcastEpisodeRepository podcastEpisodeRepository) {
        this.podcastEpisodeRepository = podcastEpisodeRepository;
    }

    @RequestMapping(method= RequestMethod.GET)
    public @ResponseBody List getPodcasts(@RequestParam(value="podcastId", defaultValue="UNKN") String podcastId) {
        LOG.info("Request for podcast episodes.");
        //noinspection unchecked
        return podcastEpisodeRepository.findAll(Specification.where(PodcastEpisodeSpecifications.episodeParentPodcast(podcastId)).and(PodcastEpisodeSpecifications.notIgnored()));
    }
}
