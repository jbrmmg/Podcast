package com.jbr.middletier.podcast.dataaccess;

import com.jbr.middletier.podcast.data.PodcastEpisode;
import org.springframework.data.jpa.domain.Specification;

/**
 * Created by jason on 31/12/16.
 */
public class PodcastEpisodeSpecifications {
    public static Specification<PodcastEpisode> episodeToDownload() {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("fileExists"),"N");
    }

    public static Specification<PodcastEpisode> episodeParentPodcast(String podcastId) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("podcastId"),podcastId);
    }

    public static Specification<PodcastEpisode> notIgnored() {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("ignore"),"N");
    }
}
