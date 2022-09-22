package com.taf.testautomation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@PropertySources( {
        @PropertySource("classpath:application.properties")
})

@ConfigurationProperties(prefix="prop")
public class TestAutomationProperties {
    private String url;
    private String almUsername;
    private String almPassword;
    private String defectID;
    private String testRunID;
    private String updateURL;
    private String deleteURL;
    private String updateKeyPass;
    private String updateValuePass;
    private String updateKeyFail;
    private String updateValueFail;
    private String imageFilePath;
    private String imageContentType;
    private String docFilePath;
    private String docContentType;
    private String textFilePath;
    private String textContentType;
    private String pdfFilePath;
    private String pdfContentType;
    private String excelFilePath;
    private String excelContentType;
}
