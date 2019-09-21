package com.jbr.middletier.podcast.manage;

import com.jbr.middletier.podcast.data.Podcast;
import com.jbr.middletier.podcast.data.PodcastEpisode;
import com.jbr.middletier.podcast.dataaccess.PodcastEpisodeRepository;
import com.jbr.middletier.podcast.schedule.PodcastItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

/**
 * Created by jason on 27/12/16.
 */
class RSSPodcastFeedParser {
    final static private Logger LOG = LoggerFactory.getLogger(RSSPodcastFeedParser.class);

    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    static final String CHANNEL = "channel";
    private static final String LANGUAGE = "language";
    private static final String COPYRIGHT = "copyright";
    private static final String LINK = "link";
    private static final String AUTHOR = "author";
    private static final String ITEM = "item";
    private static final String PUB_DATE = "pubDate";
    private static final String GUID = "guid";
    private static final String MEDIA_CONTENT = "media:content";
    private static final String ENCLOSURE = "enclosure";

    private final URL url;

    private PodcastEpisodeRepository podcastRepository;
    private String logType;

    public RSSPodcastFeedParser(String logType, PodcastEpisodeRepository podcastRepository, com.jbr.middletier.podcast.data.Podcast source) {
        try {
            this.podcastRepository = podcastRepository;
            this.url = new URL(source.getSource());
            this.logType = logType;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void readFeed(Podcast sourcePodcast) {
        try {
            boolean isFeedHeader = true;
            // Set header values initial to the empty string
            String description = "";
            String title = "";
            String author = "";
            String publishDate = "";
            String guid = "";
            String sourceURL = "";
            int fileSize = 0;

            // First create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = read();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName()
                            .getLocalPart();
                    switch (localPart) {
                        case ITEM:
                            if (isFeedHeader) {
                                isFeedHeader = false;
                            }
                            break;
                        case TITLE:
                            title = getCharacterData(event, eventReader);
                            break;
                        case DESCRIPTION:
                            description = getCharacterData(event, eventReader);
                            break;
                        case GUID:
                            guid = getCharacterData(event, eventReader);
                            break;
                        case AUTHOR:
                            author = getCharacterData(event, eventReader);
                            break;
                        case PUB_DATE:
                            publishDate = getCharacterData(event, eventReader);
                            break;
                        case ENCLOSURE:
                            sourceURL = event.asStartElement().getAttributeByName(new QName("url")).getValue();
                            fileSize = Integer.parseInt(event.asStartElement().getAttributeByName(new QName("length")).getValue());
                            break;
                    }
                } else if (event.isEndElement()) {
                    if (Objects.equals(event.asEndElement().getName().getLocalPart(), ITEM)) {
                        PodcastItem podcastItem = new PodcastItem();
                        podcastItem.setAuthor(author);
                        podcastItem.setDescription(description);
                        podcastItem.setGuid(guid);
                        podcastItem.setTitle(title);
                        podcastItem.setEnclosure(sourceURL,fileSize);
                        podcastItem.setPublishDate(publishDate);
                        //noinspection UnusedAssignment
                        event = eventReader.nextEvent();

                        // Add or update this value in the database.
                        Optional<PodcastEpisode> episode = podcastRepository.findById(guid);
                        if(!episode.isPresent()) {
                            episode = Optional.of(new PodcastEpisode(sourcePodcast, podcastItem));
                        } else {
                            episode.get().update(podcastItem);
                        }

                        LOG.debug("Podcast {}",guid);

                        podcastRepository.save(episode.get());
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCharacterData(@SuppressWarnings("ParameterCanBeLocal") XMLEvent event, XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }

    private InputStream read() {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
