package com.example.elastic.shliffen.service;

import com.example.elastic.shliffen.dto.FileDataFromJson;
import com.example.elastic.shliffen.exception.CustomException;
import com.example.elastic.shliffen.model.*;
import com.example.elastic.shliffen.model.base.FileInDB;
import com.example.elastic.shliffen.model.base.FileTag;
import com.example.elastic.shliffen.model.base.FilenameExtension;
import com.example.elastic.shliffen.repository.FilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class FileStorageService {

    @Autowired
    FilesRepository filesRepository;
    @Autowired
    ServiceMethods serviceMethods;
    FilenameExtension filenameExtension = new FilenameExtension();
    private static AtomicInteger idGenerator = new AtomicInteger(1);
    private AtomicInteger totalCountFiles;

    public long uploadFileToStorageAndGetId(FileDataFromJson fileData) {
        try {
            File currentFile = new File(fileData.getName());
            int index = fileData.getName().lastIndexOf('.');
            String extension = "";
            Set<FileTag> setOfTags = new HashSet<>();
            FileTag firstTag;
            if (index > 0) {
                extension = fileData.getName().substring(index + 1);
            }
            // setting the first tag by checking file extension
            if (!extension.isEmpty()) {
                if (filenameExtension.getVideoExtensions().contains(extension)) {
                    firstTag = new FileTag("video");
                    setOfTags.add(firstTag);
                } else if (filenameExtension.getAudioExtensions().contains(extension)) {
                    firstTag = new FileTag("audio");
                    setOfTags.add(firstTag);
                } else if (filenameExtension.getArchiveExtensions().contains(extension)) {
                    firstTag = new FileTag("archive");
                    setOfTags.add(firstTag);
                } else if (filenameExtension.getDocumentExtensions().contains(extension)) {
                    firstTag = new FileTag("document");
                    setOfTags.add(firstTag);
                } else if (filenameExtension.getImageExtensions().contains(extension)) {
                    firstTag = new FileTag("image");
                    setOfTags.add(firstTag);
                } else {
                    firstTag = new FileTag("other");
                    setOfTags.add(firstTag);
                }
            }
            if (fileData.getSize() < 0) {
                throw new CustomException("file size has negative value");
            } else if ((currentFile.exists()) && (fileData.getSize() == currentFile.length())) {
                FileInDB file = new FileInDB(setOfTags, fileData.getName(), fileData.getSize());
                file.setId(idGenerator.getAndIncrement());
                filesRepository.save(file);
                return file.getId();
            } else if (!currentFile.exists()) {
                CustomException customException = new CustomException("file not exist");
                customException.setStatus(HttpStatus.BAD_REQUEST);
                throw customException;
            } else {
                CustomException customException = new CustomException("file size does not match the entered value");
                customException.setStatus(HttpStatus.BAD_REQUEST);
                throw customException;
            }
        } catch (IllegalArgumentException exception){
            throw new CustomException("no such file exists");
        }
    }

    public void deleteFileFromStorage(Long fileId) {
        serviceMethods.checkFileExistInDb(fileId);
        filesRepository.deleteById(fileId);
    }

    public void assignTagsToFile(Long fileId, List<String> tagsList) {
        List<FileTag> fileTags;
        fileTags = tagsList.stream().map(FileTag::new).collect(Collectors.toList());
        fileTags.forEach(x -> x.setKey((long) idGenerator.getAndIncrement()));
        serviceMethods.creatingBlankTagsAndSaveToFile(fileId, fileTags.size());
        serviceMethods.fillingTagsInFileAndSaveToDB(fileId, fileTags);
    }

    public void removeTagsFromFile(Long fileId, List<String> desiredTagNames) {
        serviceMethods.checkFileExistInDb(fileId);
        Optional<FileInDB> file = filesRepository.findById(fileId);
        Set<FileTag> currentTags;
        currentTags = file.get().getFileTags();
        boolean isEveryTagIsValid = true;
        List<String> listOfCurrentTagNames =
                file.get().getFileTags().stream().map(FileTag::getNameOfTag).collect(Collectors.toList());
        //checking file has all desired tag names for removing
        for (String tagName : desiredTagNames) {
            if (!listOfCurrentTagNames.contains(tagName)) {
                isEveryTagIsValid = false;
                break;
            }
        }
        if (isEveryTagIsValid) {
            //removing tags from fileDb
            currentTags.removeIf(x -> desiredTagNames.contains(x.getNameOfTag()));
            //save result fileDb to repo
            file.get().setFileTags(currentTags);
            filesRepository.save(file.get());
        } else {
            CustomException customException = new CustomException("tag not found on file");
            customException.setStatus(HttpStatus.BAD_REQUEST);
            throw customException;
        }

    }

    public ResultPage listFilesWithPaginationAndNamesOptionally(List<String>tags, String q,
                                                                Integer page, Integer size){
        List<FileForShowing>listOfFiles;
        if (page==null) page=0;
        if (size==null) size=10;

        if (tags==null||tags.isEmpty()){
            if (q==null||q.isEmpty()) {
                listOfFiles = getAllFiles();
            } else {
                listOfFiles = getAllFilesWithFilename(q);
            }
        } else {
            if (q==null||q.isEmpty()) {
                listOfFiles = getAllFilesWithDesiredTags(tags);
            } else {
                listOfFiles = getAllFilesWithDesiredTagsAndFilename(tags,q);
            }
        }
        return serviceMethods.pagingAndSortingFiles(page, size, listOfFiles, totalCountFiles);
    }

    public List<String> getFileTagsOnly(Long fileId) {
        serviceMethods.checkFileExistInDb(fileId);
        Optional<FileInDB> file = filesRepository.findById(fileId);
        FileForShowing fileForShowing = serviceMethods.transformFileFromDbToShowFormat(file.get());
        return new ArrayList<>(fileForShowing.getTags());
    }

    //methods used

    public List<FileForShowing> getAllFiles() {
        List<FileForShowing> listForResult = new ArrayList<>();
        Iterable<FileInDB> listOfFilesInDb = filesRepository.findAll();
        totalCountFiles=new AtomicInteger(0);
        for (FileInDB fileInDB : listOfFilesInDb) {
            listForResult.add(serviceMethods.transformFileFromDbToShowFormat(fileInDB));
            totalCountFiles.incrementAndGet();
        }
        return listForResult;
    }

    /**
     * get list of files with desirable charset inside their names
     * @param charset - desirable charset inside filenames
     * @return list of all files with charset in names, in the required form
     */
    private List<FileForShowing> getAllFilesWithFilename(String charset) {
        List<FileForShowing> resultList = new ArrayList<>();
        Iterable<FileInDB> files = filesRepository.findAll();
        totalCountFiles=new AtomicInteger(0);
        for (FileInDB currentFile : files) {
            String filename = currentFile.getName().toLowerCase();
            if (filename.contains(charset.toLowerCase())) {
                FileForShowing fileForShowing = serviceMethods.transformFileFromDbToShowFormat(currentFile);
                resultList.add(fileForShowing);
                totalCountFiles.incrementAndGet();
            }
        }
        return resultList;
    }

    /**
     * get list of files which was filtered by desirable tags list
     * @param desirableTags - list of tag names
     * @return list of files with tags, in the required form
     */
    private List<FileForShowing> getAllFilesWithDesiredTags(List<String> desirableTags) {
        Iterable<FileInDB> files = filesRepository.findAll();
        List<FileForShowing> resultList = new ArrayList<>();
        List<String> fileTagNamesList;
        totalCountFiles = new AtomicInteger(0);
        for (FileInDB currentFile : files) {
            fileTagNamesList = currentFile.getFileTags().stream().map(FileTag::getNameOfTag).collect(Collectors.toList());
            if (fileTagNamesList.containsAll(desirableTags)) {
                FileForShowing fileForShowing = serviceMethods.transformFileFromDbToShowFormat(currentFile);
                resultList.add(fileForShowing);
                totalCountFiles.incrementAndGet();
            }
        }
        return resultList;
    }

    /**
     * get list of files which was filtered by desirable tags list and charset inside the name of files
     * @param desirableTags - list of tag names
     * @param charset - desirable charset inside filenames
     * @return list of files with tags and charset inside names, in the required form
     */
    private List<FileForShowing> getAllFilesWithDesiredTagsAndFilename(List<String> desirableTags, String charset) {
        Iterable<FileInDB> files = filesRepository.findAll();
        List<FileForShowing> resultList = new ArrayList<>();
        List<String> fileTagNamesList;
        totalCountFiles = new AtomicInteger(0);
        for (FileInDB currentFile : files) {
            String currentFilename = currentFile.getName().toLowerCase();
            fileTagNamesList = currentFile.getFileTags().stream().map(FileTag::getNameOfTag).collect(Collectors.toList());
            if ((fileTagNamesList.containsAll(desirableTags))&&(currentFilename.contains(charset.toLowerCase()))) {
                FileForShowing fileForShowing = serviceMethods.transformFileFromDbToShowFormat(currentFile);
                resultList.add(fileForShowing);
                totalCountFiles.incrementAndGet();
            }
        }
        return resultList;
    }
    // getter for counter of objects inside 'filtered' lists
    public AtomicInteger getTotalCountFiles() {
        return totalCountFiles;
    }
}
