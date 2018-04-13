package com.example.anuj_ilm.connect_2;

/**
 * Created by Anuj-ILM on 1/15/2018.
 */

public class FacultyOutboxMessage
{
    private String  recepients, body , attachmentUrls , subject , timestamp ;

    public FacultyOutboxMessage(String recepients, String body, String attachmentUrls, String subject, String timestamp) {
        this.recepients = recepients;
        this.body = body;
        this.attachmentUrls = attachmentUrls;
        this.subject = subject;
        this.timestamp = timestamp;
    }

    public FacultyOutboxMessage()
    {}

    public String getRecepients() {
        return recepients;
    }

    public String getBody() {
        return body;
    }

    public String getAttachmentUrls() {
        return attachmentUrls;
    }

    public String getSubject() {
        return subject;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
