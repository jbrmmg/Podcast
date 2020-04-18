package com.jbr.middletier.podcast.dataaccess;

import com.jbr.middletier.podcast.data.Podcast;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jason on 25/12/16.
 */
@Repository
public interface PodcastRepository extends  CrudRepository<Podcast, String> {
}
