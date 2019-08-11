package com.jbr.middletier.podcast.control;

import com.jbr.middletier.podcast.data.PodcastEpisode;
import com.jbr.middletier.podcast.data.StatusResponse;
import com.jbr.middletier.podcast.dataaccess.PodcastEpisodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by jason on 31/05/16.
 *
 *  curl -X GET -H "Content-type: application/json" http://localhost:13001/jbr/ext/log/type
 */

@Controller
@RequestMapping("/jbr/ext/podcast/delete")
class PodcastController {
    final static private Logger LOG = LoggerFactory.getLogger(PodcastController.class);

    private final
    PodcastEpisodeRepository podcastRepository;

    @Autowired
    public PodcastController(PodcastEpisodeRepository podcastRepository) {
        this.podcastRepository = podcastRepository;
    }

    @RequestMapping(method= RequestMethod.DELETE) public @ResponseBody
    StatusResponse deletePodcast(@RequestParam(value="podcastId", defaultValue="0") String podcastId) {
        LOG.info("Request to delete podcast - " + podcastId);
        String result = "Accepted";

        Optional<PodcastEpisode> episode = podcastRepository.findById(podcastId);
        if(episode.isPresent()) {
            // Mark this episode for delete.
            episode.get().markForDelete();
            result = episode.get().getPodcastId();
            podcastRepository.save(episode.get());

            return new StatusResponse();
        }

        return new StatusResponse("Did not find podcast");
    }
}
