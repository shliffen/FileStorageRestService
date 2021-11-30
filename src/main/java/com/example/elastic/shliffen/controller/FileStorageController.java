package com.example.elastic.shliffen.controller;


import com.example.elastic.shliffen.dto.FileDataFromJson;
import com.example.elastic.shliffen.dto.answers.IdAnswerObject;
import com.example.elastic.shliffen.dto.answers.SuccessAnswerObject;
import com.example.elastic.shliffen.model.FileForShowing;
import com.example.elastic.shliffen.model.ResultPage;
import com.example.elastic.shliffen.repository.FilesRepository;
import com.example.elastic.shliffen.service.FileStorageService;
import com.example.elastic.shliffen.service.ServiceMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class FileStorageController {

    @Autowired
    FileStorageService storageService;
    @Autowired
    FilesRepository filesRepository;
    @Autowired
    ServiceMethods serviceMethods;

    private static SuccessAnswerObject successAnswerObject = new SuccessAnswerObject("true");
    private static IdAnswerObject idAnswerObject;

    /**
     * upload file into storage
     * @param fileData - formatted JSON with file's data (name + size)
     * @return
     */
    @RequestMapping(value = "/file", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> uploadFile(@RequestBody FileDataFromJson fileData){
        serviceMethods.checkingFieldsOfDataToUpload(fileData);
        long fileId = storageService.uploadFileToStorageAndGetId(fileData);
        idAnswerObject = new IdAnswerObject(""+fileId);
        return new ResponseEntity<>(idAnswerObject,HttpStatus.OK);
    }

    /**
     * delete file from storage
     * @param ID - id of desirable file
     * @return
     */
    @RequestMapping(value = "/file/{ID}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteFile(@PathVariable Long ID) {
        storageService.deleteFileFromStorage(ID);
        return new ResponseEntity<>(successAnswerObject,HttpStatus.OK);
    }

    /**
     * assign tags to the file
     * @param ID - id of desirable file
     * @param list - - list of tags which we want to assign
     * @return
     */
    @RequestMapping(value = "/file/{ID}/tags", method = RequestMethod.POST)
    public ResponseEntity<Object> assignTags(@PathVariable Long ID, @RequestBody List<String>list){
        storageService.assignTagsToFile(ID, list);
        return new ResponseEntity<>(successAnswerObject,HttpStatus.OK);
    }

    /**
     * removing tags from file
     * @param ID - id of desirable file
     * @param list - list of tags which we want to remove
     * @return "success: true" "success: false"
     */
    @RequestMapping(value = "/file/{ID}/tags", method = RequestMethod.DELETE)
    public ResponseEntity<Object> removeTagsFromFile(@PathVariable Long ID, @RequestBody List<String>list){
        storageService.removeTagsFromFile(ID,list);
        return new ResponseEntity<>(successAnswerObject, HttpStatus.OK);
    }

    /**
     * get all tags of desirable file
     * @param ID - id of desirable file
     * @return
     */
    @RequestMapping(value = "/file/{ID}/tags", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllTagsOfFile(@PathVariable Long ID){
        List<String> listOfTagNames = storageService.getFileTagsOnly(ID);
        return new ResponseEntity<>(listOfTagNames,HttpStatus.OK);
    }

    /**
     * Get list of all files with all tags without pagination
     * @return
     */
    @RequestMapping(value = "/file/all", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllFilesList(){
        List<FileForShowing>listOfFiles = storageService.getAllFiles();
        ResultPage resultPage = new ResultPage(storageService.getTotalCountFiles().getAndIncrement(), listOfFiles);
        return new ResponseEntity<>(resultPage, HttpStatus.OK);
    }

    /**
     * Get list of files with pagination optionally filtered by tags
     * @param q - [optional] parameter that will apply a search over file name
     * @param tags - [optional] list of tags to filter by. Only files containing ALL of supplied tags will be returned.
     * @param page - [optional] the 0-based parameter for paging. 0 - is default
     * @param size - [optional] the page size parameter. 10 - is default
     * @return
     */
    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllFilesListWithPagination(@RequestParam(required = false) String q,
                                                                @RequestParam(required = false) List<String>tags,
                                                                @RequestParam(required = false) Integer page,
                                                                @RequestParam(required = false) Integer size){
        ResultPage resultPage = storageService.listFilesWithPaginationAndNamesOptionally(tags, q, page, size);
        return new ResponseEntity<>(resultPage, HttpStatus.OK);
    }

}
