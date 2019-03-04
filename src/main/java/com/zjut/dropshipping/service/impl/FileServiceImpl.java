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
import org.springframework.util.StringUtils;
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
        ServerResponse response = this.getFileExtensionName(file);
        if (response.isError()) {
            return response;
        } else {
            // 最终上传到服务器的文件名
            String uploadFileName;
            if (type.equals(Const.UploadType.IDENTITY_CARD_1)) {
                uploadFileName = identityNumber + "-front" + "." + response.getData();
            } else if (type.equals(Const.UploadType.IDENTITY_CARD_2)) {
                uploadFileName = identityNumber + "-back" + "." + response.getData();
            } else {
                return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
            }
            // 上传文件
            return this.upload(file, path, Const.UploadType.IDENTITY, id, uploadFileName);
        }
    }

    @Override
    public ServerResponse uploadGoodsImage(MultipartFile file, String path, Integer id, Integer number) {
        ServerResponse response = this.getFileExtensionName(file);
        if (response.isError()) {
            return response;
        } else {
            // 最终上传到服务器的文件名
            String uploadFileName = number + "." + response.getData();
            return this.upload(file, path, Const.UploadType.GOODS, id, uploadFileName);
        }
    }

    private ServerResponse getFileExtensionName(MultipartFile file) {
        if (file == null) {
            return ServerResponse.createByErrorMessage("文件不能为空");
        }
        // 获取源文件名
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return ServerResponse.createByErrorMessage("上传的文件名不能为空");
        }
        // 获取扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (StringUtils.isEmpty(fileExtensionName)) {
            return ServerResponse.createByErrorMessage("文件格式有误");
        }
        return ServerResponse.createBySuccess(fileExtensionName);
    }

    private ServerResponse upload(MultipartFile file, String path, String type, Integer id, String uploadFileName) {

        logger.info("开始上传文件，新文件名:{}", uploadFileName);

        /*
         创建本地暂存文件夹
         */
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);

        /*
         上传文件到服务器
         */
        try {
            // 上传到本地暂存文件夹
            file.transferTo(targetFile);

            List<File> fileList = new ArrayList<>();
            fileList.add(targetFile);
            // 上传到ftp服务器上
            FTPUtil.upload(type, fileList, id);

            // 删除本地暂存文件
            targetFile.delete();

        } catch (IOException e) {
            logger.error("文件上传失败", e);
            return ServerResponse.createByErrorMessage("文件上传失败");
        }

        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + type + "/" + id + "/" + uploadFileName;

        Map<String, String> fileMap = new HashMap<>(2);
        fileMap.put("uri", uploadFileName);
        fileMap.put("url", url);

        return ServerResponse.createBySuccess(fileMap);
    }
}
