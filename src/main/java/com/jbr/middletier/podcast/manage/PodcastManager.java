package com.jbr.middletier.podcast.manage;

import com.jbr.middletier.podcast.config.ApplicationProperties;
import com.jbr.middletier.podcast.data.Podcast;
import com.jbr.middletier.podcast.data.PodcastEpisode;
import com.jbr.middletier.podcast.dataaccess.PodcastEpisodeRepository;
import com.jbr.middletier.podcast.dataaccess.PodcastRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import static com.jbr.middletier.podcast.dataaccess.PodcastEpisodeSpecifications.episodeParentPodcast;
import static com.jbr.middletier.podcast.dataaccess.PodcastEpisodeSpecifications.episodeToDownload;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Created by jason on 31/05/17.
 */

@Component
public class PodcastManager {
    final static private Logger LOG = LoggerFactory.getLogger(PodcastManager.class);


    private final ApplicationProperties applicationProperties;
    private final PodcastRepository podcastRepository;
    private final PodcastEpisodeRepository podcastEpisodeRepository;

    @Autowired
    public PodcastManager(
            ApplicationProperties applicationProperties,
            PodcastRepository podcastRepository,
            PodcastEpisodeRepository podcastEpisodeRepository) {
        this.applicationProperties = applicationProperties;
        this.podcastRepository = podcastRepository;
        this.podcastEpisodeRepository = podcastEpisodeRepository;

        LOG.info("Podcast Manager started up.");
    }

    private void downloadData(List<Podcast> podcastList) {
        for (Podcast podcast : podcastList) {
            try {
                RSSPodcastFeedParser parser = new RSSPodcastFeedParser(applicationProperties.getLogType(), podcastEpisodeRepository, podcast);
                LOG.info(String.format("Find podcast %s.", podcast.getSource()));
                parser.readFeed(podcast);
            } catch(Exception error) {
                LOG.error("Failed to download data.", error);
            }
        }
    }

    private void deleteFileIfExists(String filename) {
        try {
            File f = new File(filename);
            Files.deleteIfExists(f.toPath());
        } catch(Exception e) {
            LOG.warn(String.format("Error deleting file: %s", filename));
        }
    }

    private void downloadFile(Podcast podcast, PodcastEpisode episode) {
        try {
            if(episode.ignore()) {
                LOG.info(String.format("Ignore %s",episode.getId()));
                return;
            }

            LOG.info(String.format("Download %s",episode.getId()));

            URL website = new URL(episode.getSource());

            ReadableByteChannel rbc = Channels.newChannel(website.openStream());

            String filename = String.format("%s//%s",podcast.getDirectory(),episode.getFilename());
            deleteFileIfExists(filename);

            FileOutputStream fos = new FileOutputStream(filename);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            episode.fileCreated();
            podcastEpisodeRepository.save(episode);

            LOG.info(String.format("%s downloaded.",String.format("%s//%s",podcast.getDirectory(),episode.getFilename())));
        } catch (Exception error) {
            LOG.error(String.format("Failed to download file %s.",episode.getId()), error);
        }
    }

    @SuppressWarnings("unchecked")
    private void downloadFiles(List<Podcast> podcastList) {
        try {
            for (Podcast podcast : podcastList) {
                List<PodcastEpisode> episodeList = (List<PodcastEpisode>) podcastEpisodeRepository.findAll(Specification.where(episodeToDownload()).and(episodeParentPodcast(podcast.getId())));

                for (PodcastEpisode episode : episodeList) {
                    downloadFile ( podcast, episode );
                }
            }
        } catch (Exception error) {
            LOG.error("Failed to download files.", error);
        }
    }

    private boolean isFileInList(List<PodcastEpisode> episodes, String filename) {
        // Is this file already in the episode list?
        for(PodcastEpisode podcastEpisode : episodes) {
            if(filename.equalsIgnoreCase(podcastEpisode.getFilename())) {
                return true;
            }
        }

        return false;
    }

    private void findExtraFiles(List<Podcast> podcastList) {
        try {
            for (Podcast podcast : podcastList) {
                @SuppressWarnings("unchecked")
                List<PodcastEpisode> episodeList = (List<PodcastEpisode>) podcastEpisodeRepository.findAll(Specification.where(episodeParentPodcast(podcast.getId())));

                // Get a list of files in the directory.
                File folder = new File(podcast.getDirectory());
                File[] listOfFiles = folder.listFiles();

                if(listOfFiles != null) {
                    for (File listOfFile : listOfFiles) {
                        if (listOfFile.isFile()) {
                            if (!isFileInList(episodeList, listOfFile.getName())) {
                                // Add this file to the list of episodes.
                                podcastEpisodeRepository.save(new PodcastEpisode(podcast, listOfFile.getName()));
                                LOG.info("Found extra file {}", listOfFile.getName());
                            }
                        }
                    }
                } else {
                    LOG.info("No files found.");
                }
            }
        } catch (Exception error) {
            LOG.error("Failed to find extra files.", error);
        }
    }

    private void updateOrDeleteRecord(PodcastEpisode episode) {
        // If no source or not updated recently then delete, otherwise update.
        if( (episode.getSource() == null) || (episode.getSource().length() == 0) ) {
            LOG.info("Delete the database record - no source");
            podcastEpisodeRepository.delete(episode);
        } else if (!episode.updatedRecently()) {
            LOG.info("Delete the database record - no updated");
            podcastEpisodeRepository.delete(episode);
        } else {
            LOG.info("Update the database record");
            episode.fileDeleted();
            podcastEpisodeRepository.save(episode);
        }
    }

    private void touchFile(String filename) {
        try {
            File fileToTouch = new File(filename);
            if(fileToTouch.setLastModified(System.currentTimeMillis())) {
                LOG.info(String.format("Touched %s file",filename));
            } else {
                LOG.warn(String.format("Touched %s file FAILED",filename));
            }
        } catch (Exception error) {
            LOG.error(String.format("Failed to touch file %s",filename), error);
        }
    }

    private void deleteFile(Podcast podcast, PodcastEpisode episode) {
        try {
            // Move this file to the directory for old files.
            File podcastFile = new File(String.format("%s/%s",podcast.getDirectory(),episode.getFilename()));
            LOG.info(String.format("About to delete %s",podcastFile.getName()));

            // Does the file exist?
            if(podcastFile.exists()) {
                Path sourceFile = Paths.get(String.format("%s/%s",podcast.getDirectory(),episode.getFilename()));

                // Does the old podcasts directory exist?
                File directoryForOldPods = new File(applicationProperties.getDeletedDirectory());
                if(!directoryForOldPods.exists()) {
                    throw new Exception(String.format("%s does not exist", applicationProperties.getDeletedDirectory()));
                }
                Path directory = Paths.get(String.format("%s/%s",applicationProperties.getDeletedDirectory(),episode.getFilename()));

                // Move the file - to the old directory
                Files.move(sourceFile,directory,REPLACE_EXISTING);
                LOG.info(String.format("Deleted %s (moved)",podcastFile.getName()));

                // Set the date of file.
                touchFile(String.format("%s/%s",applicationProperties.getDeletedDirectory(),episode.getFilename()));
            }

            // Should the record be removed or just updated?
            updateOrDeleteRecord(episode);
        } catch (Exception error) {
            LOG.error(String.format("Failed to delete file %s",episode.getId()), error);
        }
    }

    private void checkForFilesToRemove(List<Podcast> podcastList) {
        try {
            // Check that the directory for old files exists.
            File directory = new File(applicationProperties.getDeletedDirectory());
            if(!directory.exists()) {
                if(!directory.mkdir()) {
                    throw new IllegalStateException(String.format("Old Pods Directory %s does not exist.", applicationProperties.getDeletedDirectory()));
                }
            }

            for (Podcast podcast : podcastList) {
                @SuppressWarnings("unchecked")
                List<PodcastEpisode> episodeList = (List<PodcastEpisode>) podcastEpisodeRepository.findAll(Specification.where(episodeParentPodcast(podcast.getId())));

                for(PodcastEpisode podcastEpisode : episodeList) {
                    // Does the file for this episode exist?
                    if(podcastEpisode.fileExists()) {
                        // Does it need to be deleted?
                        if(podcastEpisode.deleteFile()) {
                            // This file needs to be deleted.
                            deleteFile(podcast, podcastEpisode);
                        }
                    } else {
                        // File doesn't exist, should it be removed from the database?
                        if(!podcastEpisode.ignore()) {
                            updateOrDeleteRecord(podcastEpisode);
                        }
                    }
                }
            }
        } catch (Exception error) {
            LOG.error("Failed to check for files to remove.", error);
        }
    }

    private void removeOldFiles() {
        try {
            // Check that the directory for old files exists.
            File directory = new File(applicationProperties.getDeletedDirectory());
            if(!directory.exists()) {
                if(!directory.mkdir()) {
                    throw new IllegalStateException(String.format("Old Pods Directory %s does not exist.", applicationProperties.getDeletedDirectory()));
                }
            }

            // Get files in the directory.
            File folder = new File(applicationProperties.getDeletedDirectory());
            File[] listOfFiles = folder.listFiles();

            if(listOfFiles != null ) {
                for (File oldPodFile : listOfFiles) {
                    // How old is this file?
                    long diff = new Date().getTime() - oldPodFile.lastModified();

                    if (diff > applicationProperties.getDaysToKeep() * 24 * 60 * 60 * 1000) {
                        if(oldPodFile.delete()) {
                            LOG.info(String.format("Delete %s", oldPodFile.getName()));
                        } else {
                            LOG.warn(String.format("Delete %s FAILED", oldPodFile.getName()));
                        }
                    }
                }
            } else {
                LOG.info("No files in folder.");
            }
        } catch (Exception error) {
            LOG.error("Failed to remove old pod files.", error);
        }
    }

    public void download() {
        List<Podcast> podcastList = (List<Podcast>) podcastRepository.findAll();

        // Download the data.
        downloadData(podcastList);

        // Download files.
        downloadFiles(podcastList);

        // Find files that are not in the database.
        findExtraFiles(podcastList);

        // Remove old files.
        removeOldFiles();

        // Check for files to remove.
        checkForFilesToRemove(podcastList);
    }
}
