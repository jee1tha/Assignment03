package com.dbs.service.impl;

import com.dbs.service.GoogleDriveService;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

/**
 * This class consists of the implementations to handle google drive API Requests
 *
 * @author  Jeewantha
 */
@Service
public class GoogleDriveServiceImpl implements GoogleDriveService {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleDriveServiceImpl.class);

    private static final String APPLICATION_NAME = "";

    /** E-mail address of the service account. */
    @Value("${google.account}")
    private String serviceAccountEmail;

    @Value("${google.filename}")
    private String filename;

    private static final String p12 = "/home/jee1tha/MyProjects/DBS/Assignment03/src/main/resources/credentialsp12.p12" ;
    private static HttpTransport httpTransport;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();


    /** Download file from google drive */
    public String downloadFile(){
        LOG.info("------- Starting file download from Google drive -------");
        java.io.File xls = new java.io.File(filename);
        try {
                httpTransport = GoogleNetHttpTransport.newTrustedTransport();

                // Creating google credential for service account
                GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
                        .setJsonFactory(JSON_FACTORY)
                        .setServiceAccountId(serviceAccountEmail)
                        .setServiceAccountScopes(Collections.singleton(DriveScopes.DRIVE))
                        .setServiceAccountPrivateKeyFromP12File(new java.io.File(p12))
                        .build();

                Drive service = new Drive.Builder(httpTransport, JSON_FACTORY, credential)
                        .setApplicationName(APPLICATION_NAME).build();

                FileList result = service.files().list()
                        .setPageSize(10)
                        .setFields("nextPageToken, files(id, name)")
                        .execute();
                List<File> files = result.getFiles();
                LOG.info("Files available : " + files.size());
                OutputStream outputStream = new ByteArrayOutputStream();

                files.forEach(file -> {
                        LOG.info("File Name : " + file.getName());
                    try {
                        if(file.getName().equals("assignment-trade.xlsx")){
                            service.files().get(file.getId())
                                    .executeMediaAndDownloadTo(outputStream);
                            OutputStream outputStream2 = new FileOutputStream(xls);
                            ((ByteArrayOutputStream) outputStream).writeTo(outputStream2);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.info("------- Finished file download from Google drive -------");
        return xls.getAbsolutePath();
    }
}
