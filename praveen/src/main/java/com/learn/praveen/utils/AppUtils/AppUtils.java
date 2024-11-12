package com.learn.praveen.utils.AppUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import java.awt.image.BufferedImage;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

// import com.nimbusds.jose.util.Resource;
public class AppUtils {
    public static String get_photo_upload_path(String fileName, String folder_name, Long album_id) throws Exception{
        String path = "src\\main\\resources\\static\\uploads\\" + folder_name + "\\" + album_id;
        Files.createDirectories(Paths.get(path));
        return new File(path).getAbsolutePath() + "\\" + fileName;

    }

    public static BufferedImage getThumbnai(MultipartFile originalFile, Integer width) throws Exception {
        BufferedImage thumbImg = null;
        BufferedImage img = ImageIO.read(originalFile.getInputStream());

        thumbImg = Scalr.resize(img, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, width, Scalr.OP_ANTIALIAS);

        return thumbImg;
    }

    public static Resource getFilesAsResource(long album_id, String folder_name, String file_name) throws Exception{
        String location = "src\\main\\resources\\static\\uploads\\" + folder_name + "\\" + album_id + "\\" + file_name;
        File file = new File(location);
        if(file.exists()){
            Path path = Paths.get(file.getAbsolutePath());
            return new UrlResource( path.toUri());
        } else {
            throw new Exception("File not found " + file_name);
        }
    }
}
