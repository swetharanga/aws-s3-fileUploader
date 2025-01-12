package org.example.s3fileuploader.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileServiceInterface {

    String saveS3File(MultipartFile file) throws IOException;

    String deleteS3File(String filename);

    // needs to download file as a byte array (more memory efficient if byte array is used)
    byte[] downloadS3File(String filename);

    // return all files in the S3 bucket
    List<String> showAllS3Files();
}
