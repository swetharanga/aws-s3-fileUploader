package org.example.s3fileuploader.controller;

import org.example.s3fileuploader.service.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

@RestController
public class S3Controller {

    @Autowired
    private FileServiceImpl s3;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        return s3.saveS3File(file);

    }

    // Make sure we return the file as a response entity in order to be able to download the actual file

    @GetMapping("download/{filename}")
    public ResponseEntity<byte[]> download(@PathVariable("filename") String filename) {

        //HTTP headers are essential for conveying metadata about the response
        HttpHeaders header = new HttpHeaders();
        // header with the name "ContentType" and a value of MediaType.ALL_VALUE.
        header.add("Content-Type", MediaType.ALL_VALUE);
        // header, which tells the browser how to handle the response.
        header.add("Content-Disposition", "attachment; filename=" + filename);

        byte[] data = s3.downloadS3File(filename);

        // constructs an HTTP response using ResponseEntity, which allows you to customize the response status, headers, and body.

        return ResponseEntity.status(HTTP_OK).headers(header).body(data);
    }


    @DeleteMapping("delete/{filename}")
    public String delete(@PathVariable("filename") String filename) {
        return s3.deleteS3File(filename);
    }

    @GetMapping("list")
    public List<String> getAllFiles() {
        return s3.showAllS3Files();
    }

}
