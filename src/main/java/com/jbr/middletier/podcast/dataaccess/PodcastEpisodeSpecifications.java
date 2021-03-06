package com.jbr.middletier.podcast.dataaccess;

import com.jbr.middletier.podcast.data.Podcast;
import com.jbr.middletier.podcast.data.PodcastEpisode;
import org.springframework.data.jpa.domain.Specification;

/**
 * Created by jason on 31/12/16.
 */
public class PodcastEpisodeSpecifications {
    public static Specification<PodcastEpisode> episodeToDownload() {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("fileExists"),false);
    }

    public static Specification<PodcastEpisode> episodeParentPodcast(Podcast podcast) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("podcast"), podcast);
    }

    public static Specification<PodcastEpisode> notIgnored() {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("ignore"),false);
    }
}
