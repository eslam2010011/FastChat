package com.fastchat.utils.FirebaseUpload;


public class Progress {

    private long transferredBytes;
    private long totalBytes;

    public float asFraction() {
        return (float) transferredBytes / (float) totalBytes;
    }

    public void set(long totalBytes, long transferredBytes) {
        this.transferredBytes = transferredBytes;
        this.totalBytes = totalBytes;
    }

    public Progress add (Progress result) {
        transferredBytes += result.transferredBytes;
        totalBytes += result.totalBytes;
        return this;
    }

}