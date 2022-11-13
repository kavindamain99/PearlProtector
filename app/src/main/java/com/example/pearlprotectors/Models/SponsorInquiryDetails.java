package com.example.pearlprotectors.Models;

public class SponsorInquiryDetails {

    String id,image,title,venue,date,subject,email,inquiry;

    public SponsorInquiryDetails() {
    }

    public SponsorInquiryDetails(String id, String image, String title, String venue, String date, String subject, String email, String inquiry) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.venue = venue;
        this.date = date;
        this.subject = subject;
        this.email = email;
        this.inquiry = inquiry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInquiry() {
        return inquiry;
    }

    public void setInquiry(String inquiry) {
        this.inquiry = inquiry;
    }
}
