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
import com.google.common.io.Files;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleDriveServiceImpl implements GoogleDriveService {
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


    public String downloadFile(){
        java.io.File xls = new java.io.File(filename);
        try {
                httpTransport = GoogleNetHttpTransport.newTrustedTransport();
                // check for valid setup
                if (serviceAccountEmail.startsWith("Enter ")) {
                    System.err.println(serviceAccountEmail);
                    System.exit(1);
                }
                String p12Content = Files.readFirstLine(new java.io.File(p12), Charset.defaultCharset());
                if (p12Content.startsWith("Please")) {
                    System.err.println(p12Content);
                    System.exit(1);
                }
                // service account credential (uncomment setServiceAccountUser for domain-wide delegation)
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
                OutputStream outputStream = new ByteArrayOutputStream();

                files.forEach(file -> {
                    System.out.printf("%s (%s)\n", file.getName(), file.getId());
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
        return xls.getAbsolutePath();
    }
}
