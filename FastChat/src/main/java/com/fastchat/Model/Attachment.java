//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fastchat.Model;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Attachment {
    private String fileName;
    private String fileExtension;
    private int fileSize;
    private String fileMimeType;
    private String filePath;
    private String fileUrl;

    public Attachment() {
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtension() {
        return this.fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public int getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileMimeType() {
        return this.fileMimeType;
    }

    public void setFileMimeType(String fileMimeType) {
        this.fileMimeType = fileMimeType;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public static Attachment fromJson(JSONObject attachmentObject) {
        Attachment attachment = new Attachment();

        try {
            if (attachmentObject.has("name")) {
                attachment.setFileName(attachmentObject.getString("name"));
            }

            if (attachmentObject.has("extension")) {
                attachment.setFileExtension(attachmentObject.getString("extension"));
            }

            if (attachmentObject.has("size")) {
                attachment.setFileSize(attachmentObject.getInt("size"));
            }

            if (attachmentObject.has("mimeType")) {
                attachment.setFileMimeType(attachmentObject.getString("mimeType"));
            }

            if (attachmentObject.has("url")) {
                attachment.setFileUrl(attachmentObject.getString("url"));
            }
        } catch (JSONException var3) {
            var3.printStackTrace();
        }

        return attachment;
    }

    JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        if (this.getFileUrl() != null) {
            jsonObject.put("url", this.getFileUrl());
        }

        if (this.getFileExtension() != null) {
            jsonObject.put("extension", this.getFileExtension());
        }

        if (this.getFileMimeType() != null) {
            jsonObject.put("mimeType", this.getFileMimeType());
        }

        if (this.getFileName() != null) {
            jsonObject.put("name", this.getFileName());
        }

        if (this.getFileSize() > 0) {
            jsonObject.put("size", this.getFileSize());
        }

        return jsonObject;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.fileName, this.fileSize, this.fileExtension, this.fileMimeType, this.fileSize, this.fileUrl});
    }

    public boolean equals(@Nullable Object obj) {
        Attachment a = (Attachment)obj;
        return a.getFileName().equalsIgnoreCase(this.getFileName()) && a.getFileExtension().equalsIgnoreCase(this.getFileExtension()) && a.getFileMimeType().equalsIgnoreCase(this.getFileMimeType()) && a.getFileSize() == this.getFileSize() && a.getFileUrl().equalsIgnoreCase(this.getFileUrl());
    }

    public String toString() {
        return "Attachment{fileName='" + this.fileName + '\'' + ", fileExtension='" + this.fileExtension + '\'' + ", fileSize=" + this.fileSize + ", fileMimeType='" + this.fileMimeType + '\'' + ", fileUrl='" + this.fileUrl + '\'' + '}';
    }
}
