package com.jbr.middletier.podcast.dataaccess;

import com.jbr.middletier.podcast.data.Podcast;
import org.springframework.data.repository.CrudRepository;

/**
 * Provides information about podcasts available.
 */

@SuppressWarnings("ALL")
public interface PodcastRepository extends CrudRepository<Podcast, String> {
}
