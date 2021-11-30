package com.example.elastic.shliffen.controller;

import com.example.elastic.shliffen.FileStorageApplication;
import com.example.elastic.shliffen.dto.FileDataFromJson;
import com.example.elastic.shliffen.repository.FilesRepository;
import com.example.elastic.shliffen.service.FileStorageService;
import com.example.elastic.shliffen.service.ServiceMethods;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FileStorageApplication.class)
@ContextConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
@AutoConfigureMockMvc
public class FileStorageControllerTest {

    @InjectMocks
    FileStorageController fileStorageController;

    @Mock
    private ServiceMethods serviceMethods;
    @Mock
    private FilesRepository filesRepository;
    @Mock
    private FileStorageService storageService;
    @Autowired
    private MockMvc mockMvc;
    private FileDataFromJson fileDataFromJson = new FileDataFromJson();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(fileStorageController).build();
        init();
    }

    private void init(){
        fileDataFromJson = new FileDataFromJson();
        fileDataFromJson.setName("E:\\some.txt");
        fileDataFromJson.setSize(9);
    }

    @Test
    void testUploadFile() throws Exception {
        doNothing().when(serviceMethods).checkingFieldsOfDataToUpload(any());
        when(storageService.uploadFileToStorageAndGetId(any())).thenReturn(1L);
        mockMvc.perform(post("/file").contentType(MediaType.APPLICATION_JSON)
                                                                   .content(
                                                                           "{\n     \"name\": \"E:\\\\song.mp3\",\n     \"size\": 11610950\n}"))
                                             .andExpect(status().isOk()).andExpect(content().string("{\"id\":\"1\"}"));
    }
    @Test
    void testDeleteFile()throws Exception{
        doNothing().when(storageService).deleteFileFromStorage(any());
        mockMvc.perform(delete("/file/1"))
               .andExpect(status().isOk()).andExpect(content().string("{\"success\":\"true\"}"));
    }


}
