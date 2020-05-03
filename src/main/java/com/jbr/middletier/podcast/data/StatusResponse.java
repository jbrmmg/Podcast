package com.jbr.middletier.podcast.data;

@SuppressWarnings("unused")
public class StatusResponse {
    private String status;
    private String message;

    public StatusResponse() {
        status = "OK";
        message = "";
    }

    public StatusResponse(String errorMsg) {
        status = "FAILURE";
        message = errorMsg;
    }
}
