package com.jbr.middletier.podcast.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="podcast")
public class ApplicationProperties {
    private String serviceName;
    private String logType;
    private String deletedDirectory;
    private int daysToKeep;

    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public void setLogType(String logType) { this.logType = logType; }

    public void setDeletedDirectory(String deletedDirectory) { this.deletedDirectory = deletedDirectory; }

    public void setDaysToKeep(int daysToKeep) { this.daysToKeep = daysToKeep; }

    public String getServiceName() { return this.serviceName; }

    public String getLogType() { return this.logType; }

    public String getDeletedDirectory() { return this.deletedDirectory; }

    public int getDaysToKeep() { return this.daysToKeep; }
}
