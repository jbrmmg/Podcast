package com.jbr.middletier.podcast.health;

import com.jbr.middletier.podcast.data.Podcast;
import com.jbr.middletier.podcast.dataaccess.PodcastRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jason on 26/04/17.
 */

@SuppressWarnings("WeakerAccess")
@Component
public class ServiceHealthIndicator implements HealthIndicator {
    final static private Logger LOG = LoggerFactory.getLogger(ServiceHealthIndicator.class);

    @SuppressWarnings("WeakerAccess")
    final PodcastRepository podcastRepository;

    @Value("${middle.tier.podcast.logtype}")
    private String logType;

    @Value("${middle.tier.service.name}")
    private String serviceName;

    @Autowired
    public ServiceHealthIndicator(PodcastRepository podcastRepository) {
        this.podcastRepository = podcastRepository;
    }

    @Override
    public Health health() {
        try {
            List<Podcast> podcastList = (List<Podcast>) podcastRepository.findAll();
            LOG.info(String.format("Check Database %s.", podcastList.size()));

            return Health.up().withDetail("service", serviceName).withDetail("Podcast Types",podcastList.size()).build();
        } catch (Exception ignored) {

        }

        return Health.down().withDetail("service", serviceName).build();
    }
}
