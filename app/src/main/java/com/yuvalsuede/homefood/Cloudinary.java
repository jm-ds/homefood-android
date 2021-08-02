package com.yuvalsuede.homefood;

import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Cloudinary {

    public static String getImageLinkFromCloudinary(String path) {
        com.cloudinary.Cloudinary cloudinary = new com.cloudinary.Cloudinary();
        cloudinary.config.apiKey = "938874431354215";
        cloudinary.config.apiSecret = "aNXlgV6Ay2F9umUb1qUt1aZkWAE";
        cloudinary.config.cloudName = "home-food";
        return cloudinary.url().generate(path);
    }

    public static String uploadImageAndGetId(File file) throws IOException {
        com.cloudinary.Cloudinary cloudinary = new com.cloudinary.Cloudinary();
        cloudinary.config.apiKey = "938874431354215";
        cloudinary.config.apiSecret = "aNXlgV6Ay2F9umUb1qUt1aZkWAE";
        cloudinary.config.cloudName = "home-food";
        Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        return (String) uploadResult.get("public_id");
    }

}
