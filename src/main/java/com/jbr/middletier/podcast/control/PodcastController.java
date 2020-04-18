package com.jbr.middletier.podcast.control;

import com.jbr.middletier.podcast.data.Podcast;
import com.jbr.middletier.podcast.data.StatusResponse;
import com.jbr.middletier.podcast.dataaccess.PodcastRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

/**
 * Created by jason on 31/05/16.
 *
 *  curl -X GET -H "Content-type: application/json" http://localhost:13001/jbr/ext/log/type
 */

@Controller
@RequestMapping("/jbr")
class PodcastController {
    final static private Logger LOG = LoggerFactory.getLogger(PodcastController.class);

    private final PodcastRepository podcastRepository;

    @Autowired
    public PodcastController(
            PodcastRepository podcastRepository) {
        this.podcastRepository = podcastRepository;
    }

    @RequestMapping(path="/ext/podcast/type", method= RequestMethod.GET)
    public @ResponseBody Iterable<Podcast> getPodcasts() {
        LOG.info("Request for podcasts.");
        return podcastRepository.findAll();
    }

    @RequestMapping(path="/int/podcast/type", method= RequestMethod.POST)
    public @ResponseBody StatusResponse createPodcast(@RequestBody Podcast newPodcast) {
        LOG.info("Create new podcast.");

        if(this.podcastRepository.existsById(newPodcast.getId())) {
            return new StatusResponse(newPodcast.getId() + " already exists.");
        }

        this.podcastRepository.save(newPodcast);

        return new StatusResponse();
    }

    @RequestMapping(path="/int/podcast/type", method= RequestMethod.PUT)
    public @ResponseBody StatusResponse updatePodcast(@RequestBody Podcast newPodcast) {
        LOG.info("Update podcast.");

        Optional<Podcast> existingPodcast = this.podcastRepository.findById(newPodcast.getId());

        if(!existingPodcast.isPresent()) {
            return new StatusResponse(newPodcast.getId() + " does not exist exists.");
        }

        this.podcastRepository.save(newPodcast);

        return new StatusResponse();
    }

    @RequestMapping(path="/int/podcast/type", method= RequestMethod.DELETE)
    public @ResponseBody StatusResponse deletePodcast(@RequestBody Podcast deletePodcast) {
        LOG.info("Delete podcast");

        Optional<Podcast> existingPodcast = this.podcastRepository.findById(deletePodcast.getId());

        if(!existingPodcast.isPresent()) {
            return new StatusResponse(deletePodcast.getId() + " does not exist exists.");
        }

        this.podcastRepository.delete(deletePodcast);

        return new StatusResponse();
    }
}
