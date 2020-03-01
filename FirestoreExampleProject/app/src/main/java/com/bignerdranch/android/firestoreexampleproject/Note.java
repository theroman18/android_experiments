package com.bignerdranch.android.firestoreexampleproject;

import com.google.firebase.firestore.Exclude;

public class Note {
    private String documentId;
    private String title;
    private String description;

    public Note () {
        // public no-arg constructor needed for firestore
    }

    public Note( String title, String description) {
        this.documentId = documentId;
        this.title = title;
        this.description = description;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
