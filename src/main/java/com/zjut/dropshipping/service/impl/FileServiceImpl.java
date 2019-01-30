package com.zjut.dropshipping.service.impl;

import com.zjut.dropshipping.service.FileService;
import com.zjut.dropshipping.utils.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zjxjwxk
 */
@Service("FileService")
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upload(MultipartFile file, String path, String type, String phone) {
        String fileName = file.getOriginalFilename();
        // 扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = phone + "-" + type + "." + fileExtensionName;
        logger.info("开始上传文件，上传文件的文件名:{}，上传的路径:{}，新文件名:{}", fileName, path, uploadFileName);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);

        try {
            file.transferTo(targetFile);
            // 文件已经上传成功了

            List<File> fileList = new ArrayList<>();
            fileList.add(targetFile);
            FTPUtil.upload(type, fileList, phone);
            // 已经上传到ftp服务器上

            targetFile.delete();

        } catch (IOException e) {
            logger.error("上传文件异常", e);
            return null;
        }

        return targetFile.getName();
    }
}
