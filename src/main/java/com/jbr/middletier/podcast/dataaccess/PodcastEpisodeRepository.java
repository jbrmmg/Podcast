package com.jbr.middletier.podcast.dataaccess;

import com.jbr.middletier.podcast.data.PodcastEpisode;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jason on 29/12/16.
 */

@Repository
public interface PodcastEpisodeRepository extends CrudRepository<PodcastEpisode, String>, JpaSpecificationExecutor {
}
