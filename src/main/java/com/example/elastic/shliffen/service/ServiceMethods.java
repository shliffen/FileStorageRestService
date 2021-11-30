package com.example.elastic.shliffen.service;

import com.example.elastic.shliffen.dto.FileDataFromJson;
import com.example.elastic.shliffen.exception.CustomException;
import com.example.elastic.shliffen.model.FileForShowing;
import com.example.elastic.shliffen.model.base.FileInDB;
import com.example.elastic.shliffen.model.base.FileTag;
import com.example.elastic.shliffen.model.ResultPage;
import com.example.elastic.shliffen.repository.FilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class ServiceMethods {

    @Autowired
    FilesRepository filesRepository;

    public void checkingFieldsOfDataToUpload(FileDataFromJson fileData) {
        if ((fileData.getName() == null || fileData.getName().isEmpty()) && (fileData.getSize() <= 0)) {
            CustomException customException = new CustomException("you didn't entered the values of filename and size");
            customException.setStatus(HttpStatus.BAD_REQUEST);
            throw customException;
        } else if (fileData.getSize() < 0) {
            CustomException customException = new CustomException("file size cannot be negative");
            customException.setStatus(HttpStatus.BAD_REQUEST);
            throw customException;
        } else if (fileData.getSize() == 0) {
            CustomException customException = new CustomException("file size cannot be empty or equal 0");
            customException.setStatus(HttpStatus.BAD_REQUEST);
            throw customException;
        } else if ((fileData.getName() == null) || (fileData.getName().isEmpty())) {
            CustomException customException = new CustomException("file name cannot be empty");
            customException.setStatus(HttpStatus.BAD_REQUEST);
            throw customException;
        }
    }

    public void checkFileExistInDb(Long fileId) {
        Optional<FileInDB> fileInDB = filesRepository.findById(fileId);
        if (fileInDB.isEmpty()) {
            CustomException customException = new CustomException("file not found");
            customException.setStatus(HttpStatus.NOT_FOUND);
            throw customException;
        }
    }

    /**
     * Transforming file from form of object inside DB to the single object desired form in response
     *
     * @param fileInDB
     * @return
     */
    public FileForShowing transformFileFromDbToShowFormat(FileInDB fileInDB) {
        File currentFile = new File(fileInDB.getName());
        FileForShowing fileForShowing = new FileForShowing();
        fileForShowing.setId(fileInDB.getId());
        fileForShowing.setName(currentFile.getName());
        fileForShowing.setSize(fileInDB.getSize());
        Set<String> setOdTags = fileInDB.getFileTags().stream().map(FileTag::getNameOfTag).collect(Collectors.toSet());
        fileForShowing.setTags(setOdTags);
        return fileForShowing;
    }

    /**
     * Making new empty Tags for desirable file
     *
     * @param fileId         - id of file to assign tags
     * @param quantityOfTags No comments =))
     */
    public void creatingBlankTagsAndSaveToFile(Long fileId, int quantityOfTags) {
        checkFileExistInDb(fileId);
        Optional<FileInDB> file = filesRepository.findById(fileId);
        Set<FileTag> currentTags = file.get().getFileTags();
        for (int i = 0; i < quantityOfTags; i++) {
            currentTags.add(new FileTag());
        }
        file.get().setFileTags(currentTags);
        filesRepository.save(file.get());
    }

    /**
     * Filling the empty tags from method above by names
     *
     * @param fileId
     * @param listOfTags
     */
    public void fillingTagsInFileAndSaveToDB(Long fileId, List<FileTag> listOfTags) {
        checkFileExistInDb(fileId);
        Optional<FileInDB> file = filesRepository.findById(fileId);
        Set<FileTag> currentTags = file.get().getFileTags();
        int indexInList = 0;
        for (FileTag tag : currentTags) {
            if (tag.getNameOfTag().equals("")) {
                tag.setNameOfTag(listOfTags.get(indexInList).getNameOfTag());
                indexInList++;
            }
        }
        file.get().setFileTags(currentTags);
        filesRepository.save(file.get());
    }

    /**
     * Divides the list into Pages by size, sort objects by their id, and return results from desirable page
     *
     * @param page            - desirable page (start from 0)
     * @param size            - size of page (quantity objects inside)
     * @param listOfFiles     - list of all files which satisfying the conditions
     * @param totalCountFiles - No comments =))
     * @return Result page for ResponseEntity in Controller
     */
    public ResultPage pagingAndSortingFiles(Integer page, Integer size, List<FileForShowing> listOfFiles,
                                            AtomicInteger totalCountFiles) {
        listOfFiles.sort(Comparator.comparingLong(FileForShowing::getId));
        if (page <= listOfFiles.size() / size) {
            int indexFirstElementResult = page * size;
            List<FileForShowing> subListFromPage;
            if (indexFirstElementResult + size >= listOfFiles.size()) {
                subListFromPage = listOfFiles.subList(indexFirstElementResult, listOfFiles.size());
            } else {
                subListFromPage = listOfFiles.subList(indexFirstElementResult, indexFirstElementResult + size);
            }
            return new ResultPage(totalCountFiles.getAndIncrement(), subListFromPage);
        } else {
            int number = listOfFiles.size() / size;
            CustomException customException = new CustomException("the page number is too large for the number of " +
                                                                  "items. Please enter a number less than " + number);
            customException.setStatus(HttpStatus.BAD_REQUEST);
            throw customException;
        }
    }

}
