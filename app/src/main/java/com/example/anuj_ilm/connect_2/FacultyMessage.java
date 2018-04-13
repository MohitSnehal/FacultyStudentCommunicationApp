package com.example.anuj_ilm.connect_2;

/**
 * Created by Anuj-ILM on 1/15/2018.
 */

public class FacultyMessage
{
    private String  sender, body , attachmentUrls , subject , timestamp ;


    public FacultyMessage(){}

    public FacultyMessage(String  sender, String body, String attachmentUrls , String subject , String timestamp) {
        this.body = body;
        this.attachmentUrls = attachmentUrls;
        this.sender = sender ;
        this.subject = subject;
        this.timestamp = timestamp ;

    }

    public String getSubject() {return subject; }

    public String getBody() {
        return body;
    }

    public String getAttachmentUrls() {
        return attachmentUrls;
    }

    public String getSender() {
        return sender;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
