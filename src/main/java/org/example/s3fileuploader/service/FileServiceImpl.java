package org.example.s3fileuploader.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileServiceInterface {

    //constructor
    private final AmazonS3 s3;

    @Value("${bucketName}")
    private String bucketName;

    public FileServiceImpl(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public String saveS3File(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException();
        }
        System.out.println(bucketName);
        // Convert an S3 file into a MultipartFile for seamless web application handling        s3.putObject(bucketName,or);
        String filename = file.getOriginalFilename();
        // putObject needs a bucketName and the filename as a fileObject
        File convertedFile = getFileObjectFromMulripartfile(file);
        PutObjectResult putObjectResult = s3.putObject(bucketName, filename, convertedFile);
        // put the object as hash MD5 (Message-Digest Algorithm 5) is a cryptographic hash function that takes an input (such as a file, message, or string) and produces a fixed-length output of 128 bit
        return putObjectResult.getContentMd5();
    }

    public File getFileObjectFromMulripartfile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(convFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        return convFile;
    }

    @Override
    public String deleteS3File(String filename) {
        s3.deleteObject(bucketName, filename);
        return "File has been deleted";
    }

    @Override
    public byte[] downloadS3File(String filename) {
        S3Object object = s3.getObject(bucketName, filename);
        // Getting the object content from the
        S3ObjectInputStream s3ObjectContent = object.getObjectContent();
        try {
            return IOUtils.toByteArray(s3ObjectContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //listObjectsV2Request.getObjectSummaries() retrieves a list of S3ObjectSummary objects, each representing metadata about an object (file) in the bucket.
    //.stream() creates a stream for processing the list.
    //.map(S3ObjectSummary::getKey) extracts the Key (the file name or path) from each S3ObjectSummary object.
    //.collect(Collectors.toList()) collects the results into a List<String>

    @Override
    public List<String> showAllS3Files() {
        ListObjectsV2Result listObjectsV2Result = s3.listObjectsV2(bucketName);
        return listObjectsV2Result.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }

}
