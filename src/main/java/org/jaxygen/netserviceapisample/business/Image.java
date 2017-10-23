package org.jaxygen.netserviceapisample.business;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.Status;
import org.jaxygen.dto.Downloadable;
import org.jaxygen.dto.Uploadable;
import org.jaxygen.mime.MimeTypeAnalyser;
import org.jaxygen.netserviceapisample.business.dto.AddImageRequestDTO;
import org.jaxygen.network.DownloadableFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Image {

    private final static String TMP = System.getProperty("java.io.tmpdir");
    private final static File SHARED_DIR = new File(TMP);
    private static String sharedFileMimeType = "";
    private static Uploadable upladedFile;
    private static File sharedFile;


    @NetAPI(description = "This function return all images names",
            status = Status.GenerallyAvailable,
            version = "1.0"
    )
    public List<String> names() {
        File folder = new File("/var/www/html/img");
        File[] listOfFiles = folder.listFiles();
        return Arrays.asList(listOfFiles)
                .stream()
                .map(File::getName)
                .collect(Collectors.toList());

    }

    @NetAPI(description = "Function demonstrates how to make the file downloadable. "
            + "The file is transfered using inline dispostion. "
            + "That mean it is shown directly in the browser window.",
            status= Status.ReleaseCandidate,
            version="1.0")
    public Downloadable showFile(GetImageRequestDTO request) {
        String path = "/var/www/html/img/" + request.getImageName();
        String sharedFileMimeType = MimeTypeAnalyser.getMimeForExtension(new File(path));
        return new DownloadableFile(new File(path), Downloadable.ContentDisposition.inline, sharedFileMimeType);
    }

    @NetAPI(description = "Function demonstrates usage of the UploadedFile class usage",
            status= Status.ReleaseCandidate,
            version="1.0")
    public String saveUploadedFile(AddImageRequestDTO myFile) throws IOException {
        String name = myFile.getFile().getFile().getName();
        FileUtils.copyFile(myFile.getFile().getFile(), new File("/var/www/html/" + name));
        return myFile.getFile().getFile().getPath();
    }

//    public File getFile(String name) throws IOException {
//        String path = "/var/www/html/img/" + request.getImageName();
//        String sharedFileMimeType = MimeTypeAnalyser.getMimeForExtension(new File(path));
//        return new DownloadableFile(new File(path), Downloadable.ContentDisposition.inline, sharedFileMimeType);
//        return file;
//    }

}
