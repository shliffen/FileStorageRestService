package com.example.elastic.shliffen.model.base;

/**
 * All variations of file extensions that I have remembered =)
 */
public class FilenameExtension {

    private final String audioExtensions = "mp3 mp2 wav aac ogg aac m4p m4a flac wma cda";
    private final String videoExtensions = "wmv mkw mpg dvx flv avi m4v m2v mp4 nfv";
    private final String imageExtensions = "bmp png jpg gif jpeg tif tiff";
    private final String archiveExtensions = "rar 7z zip jar sfx";
    private final String documentExtensions = "txt doc docx pdf cbr djvu ott odt";

    public String getAudioExtensions() {
        return audioExtensions;
    }

    public String getVideoExtensions() {
        return videoExtensions;
    }

    public String getImageExtensions() {
        return imageExtensions;
    }

    public String getArchiveExtensions() {
        return archiveExtensions;
    }

    public String getDocumentExtensions() {
        return documentExtensions;
    }
}
