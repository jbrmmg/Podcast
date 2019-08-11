package com.jbr.middletier.podcast.dataaccess;

import com.jbr.middletier.podcast.data.PodcastEpisode;
import org.springframework.data.jpa.domain.Specification;

/**
 * Created by jason on 02/01/17.
 */
public class PodcastEpisodeSpecifications {
    public static Specification<PodcastEpisode> episodeParentPodcast(String podcastId) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("podcastId"),podcastId);
    }

    public static Specification<PodcastEpisode> notIgnored() {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("ignore"),"N");
    }
}
