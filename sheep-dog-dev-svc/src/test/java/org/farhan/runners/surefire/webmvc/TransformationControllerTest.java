package org.farhan.runners.surefire.webmvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.farhan.mbt.controller.TransformationController;
import org.farhan.mbt.convert.ConvertAsciidoctorToUML;
import org.farhan.mbt.convert.ObjectRepository;
import org.farhan.mbt.service.TransformationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ContextConfiguration(classes = WebMvcTestConfig.class)
@WebMvcTest(TransformationController.class)
public class TransformationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ObjectRepository repository;

    @MockitoBean
    private TransformationService service;

    @Test
    public void testClearConvertAsciidoctorToUMLObjects() throws Exception {
        doNothing().when(service).clearObjects(any(ConvertAsciidoctorToUML.class));

        mockMvc.perform(delete("/sheep-dog-dev-svc/clearConvertAsciidoctorToUMLObjects"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRunConvertAsciidoctorToUML() throws Exception {
        String fileName = "src/test/resources/asciidoc/stepdefs/blah application/Object page.asciidoc";
        String contents = "dummy content";

        doNothing().when(service).convertSourceObject(any(ConvertAsciidoctorToUML.class), any());

        mockMvc.perform(post("/sheep-dog-dev-svc/runConvertAsciidoctorToUML")
                .param("tags", "")
                .param("fileName", fileName)
                .content(contents)
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetConvertUMLToCucumberObjectNames() throws Exception {
        String tags = "";
        java.util.ArrayList<org.farhan.mbt.model.TransformableFile> mockList = new java.util.ArrayList<>();
        org.mockito.Mockito
                .when(service.getObjectNames(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(tags)))
                .thenReturn(mockList);

        mockMvc.perform(get("/sheep-dog-dev-svc/getConvertUMLToCucumberObjectNames")
                .param("tags", tags))
                .andExpect(status().isOk());
    }

    @Test
    public void testRunConvertUMLToCucumber_MultipleFiles() throws Exception {
        String[] fileNames = {
                "src-gen/test/resources/cucumber/specs/app/Process.feature",
                "src-gen/test/java/org/farhan/stepdefs/blah/BlahObjectPageSteps.java",
                "src-gen/test/java/org/farhan/objects/blah/ObjectPage.java"
        };
        String tags = "";
        String contents = "";

        org.farhan.mbt.model.TransformableFile mockFile = new org.farhan.mbt.model.TransformableFile();
        org.mockito.Mockito
                .when(service.convertObject(org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any()))
                .thenReturn(mockFile);

        for (String fileName : fileNames) {
            mockMvc.perform(post("/sheep-dog-dev-svc/runConvertUMLToCucumber")
                    .param("tags", tags)
                    .param("fileName", fileName)
                    .content(contents)
                    .contentType("application/json"))
                    .andExpect(status().isOk());
        }
    }
}
