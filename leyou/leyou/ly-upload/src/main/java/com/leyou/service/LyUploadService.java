package com.leyou.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
@Service
public class LyUploadService {

    @Autowired
    private FastFileStorageClient client;

    public String uploadImage(MultipartFile file) {
        String url = null;
/*
        File f1 = new File("G:\\upload");
        if (!f1.exists()) {
            f1.mkdir();
        }
        try {
            file.transferTo(new File(f1,file.getOriginalFilename()));
            url="http://image.leyou.com/"+file.getOriginalFilename();
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;*/
        String originalFilename=file.getOriginalFilename();
        String ext=StringUtils.substringAfter(originalFilename,".");
        try {
            StorePath storePath = client.uploadFile(file.getInputStream(), file.getSize(), ext, null);

            url="http://image.leyou.com/"+storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }
}
