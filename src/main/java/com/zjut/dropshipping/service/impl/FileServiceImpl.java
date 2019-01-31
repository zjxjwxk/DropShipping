package com.zjut.dropshipping.service.impl;

import com.zjut.dropshipping.common.Const;
import com.zjut.dropshipping.common.ResponseCode;
import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.service.FileService;
import com.zjut.dropshipping.utils.FTPUtil;
import com.zjut.dropshipping.utils.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zjxjwxk
 */
@Service("FileService")
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public ServerResponse IDCardUpload(MultipartFile file, String path, String type, Integer id, String identityNumber) {
        String fileName = file.getOriginalFilename();
        // 获取扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 最终上传到服务器的文件名
        String uploadFileName;
        if (type.equals(Const.UploadType.IDENTITY_CARD_1)) {
            uploadFileName = identityNumber + "-front" + "." + fileExtensionName;
        } else if (type.equals(Const.UploadType.IDENTITY_CARD_2)) {
            uploadFileName = identityNumber + "-back" + "." + fileExtensionName;
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

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
            FTPUtil.upload(Const.UploadType.IDENTITY, fileList, id, identityNumber);
            // 已经上传到ftp服务器上

            targetFile.delete();

        } catch (IOException e) {
            logger.error("文件上传失败", e);
            return ServerResponse.createByErrorMessage("文件上传失败");
        }

        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + uploadFileName;

        Map fileMap = new HashMap(2);
        fileMap.put("uri", uploadFileName);
        fileMap.put("url", url);

        return ServerResponse.createBySuccess(fileMap);
    }
}
