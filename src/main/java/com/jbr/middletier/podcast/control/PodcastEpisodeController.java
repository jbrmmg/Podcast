package com.jbr.middletier.podcast.control;

import com.jbr.middletier.podcast.data.Podcast;
import com.jbr.middletier.podcast.data.PodcastEpisode;
import com.jbr.middletier.podcast.data.Reminder;
import com.jbr.middletier.podcast.data.StatusResponse;
import com.jbr.middletier.podcast.dataaccess.PodcastEpisodeRepository;
import com.jbr.middletier.podcast.dataaccess.PodcastEpisodeSpecifications;
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

    @Autowired
    public PodcastEpisodeController(
            PodcastEpisodeRepository podcastEpisodeRepository) {
        this.podcastEpisodeRepository = podcastEpisodeRepository;
    }

    @RequestMapping(path="/ext/podcastepisode", method= RequestMethod.DELETE) public @ResponseBody
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

    @RequestMapping(path="/ext/podcastepisode", method= RequestMethod.GET)
    public @ResponseBody
    List getPodcasts(@RequestParam(value="podcastId", defaultValue="UNKN") String podcastId) {
        LOG.info("Request for podcast episodes.");
        //noinspection unchecked
        return podcastEpisodeRepository.findAll(Specification.where(PodcastEpisodeSpecifications.episodeParentPodcast(podcastId)).and(PodcastEpisodeSpecifications.notIgnored()));
    }

    @RequestMapping(path="/int/podcastepisode", method= RequestMethod.PUT)
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