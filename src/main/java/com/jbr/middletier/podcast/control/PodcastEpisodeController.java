package com.jbr.middletier.podcast.control;

import com.jbr.middletier.podcast.data.Podcast;
import com.jbr.middletier.podcast.data.PodcastEpisode;
import com.jbr.middletier.podcast.data.Reminder;
import com.jbr.middletier.podcast.data.StatusResponse;
import com.jbr.middletier.podcast.dataaccess.PodcastEpisodeRepository;
import com.jbr.middletier.podcast.dataaccess.PodcastEpisodeSpecifications;
import com.jbr.middletier.podcast.dataaccess.PodcastRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/jbr")
public class PodcastEpisodeController {
    final static private Logger LOG = LoggerFactory.getLogger(PodcastEpisodeController.class);

    private final PodcastEpisodeRepository podcastEpisodeRepository;
    private final PodcastRepository podcastRepository;

    @Autowired
    public PodcastEpisodeController(
            PodcastEpisodeRepository podcastEpisodeRepository,
            PodcastRepository podcastRepository ) {
        this.podcastEpisodeRepository = podcastEpisodeRepository;
        this.podcastRepository = podcastRepository;
    }

    @RequestMapping(path="/ext/podcast/episode", method= RequestMethod.DELETE) public @ResponseBody
    StatusResponse deletePodcast(@RequestParam(value="podcastId", defaultValue="0") String podcastId) {
        LOG.info("Request to delete podcast - " + podcastId);

        Optional<PodcastEpisode> episode = podcastEpisodeRepository.findById(podcastId);
        if(episode.isPresent()) {
            // Mark this episode for delete.
            episode.get().markForDelete();
            podcastEpisodeRepository.save(episode.get());

            return new StatusResponse();
        }

        return new StatusResponse("Did not find podcast");
    }

    @RequestMapping(path="/ext/podcast/episode", method= RequestMethod.GET)
    public @ResponseBody
    List getPodcasts(@RequestParam(value="podcastId", defaultValue="UNKN") String podcastId) throws Exception {
        LOG.info("Request for podcast episodes.");

        Optional<Podcast> existing = podcastRepository.findById(podcastId);
        if(!existing.isPresent()) {
            throw new Exception("Invalid id");
        }

        //noinspection unchecked
        return podcastEpisodeRepository.findAll(Specification.where(PodcastEpisodeSpecifications.episodeParentPodcast(existing.get())).and(PodcastEpisodeSpecifications.notIgnored()));
    }

    @RequestMapping(path="/int/podcast/episode", method= RequestMethod.PUT)
    public @ResponseBody
    StatusResponse updatePodcast(@RequestBody PodcastEpisode podcast) {
        LOG.info("Update podcase episode - " + podcast);

        Optional<PodcastEpisode> existing = podcastEpisodeRepository.findById(podcast.getId());

        if(!existing.isPresent()) {
            return new StatusResponse("Podcast episode does not exist");
        }

        podcastEpisodeRepository.save(podcast);

        return new StatusResponse();
    }
}
