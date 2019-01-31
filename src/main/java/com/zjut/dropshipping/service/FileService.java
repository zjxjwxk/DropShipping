package com.zjut.dropshipping.service;

import com.zjut.dropshipping.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zjxjwxk
 */
public interface FileService {

    /**
     * 上传文件到FTP服务器
     * @param file 上传的文件
     * @param path 本地暂存路径
     * @param type 上传文件类别(需要进入的文件夹名)
     * @param id 用户id(需要创建的文件夹名)
     * @param identityNumber 身份证号(包含在文件名中)
     * @return 服务响应对象
     */
    ServerResponse IDCardUpload(MultipartFile file, String path, String type, Integer id, String identityNumber);
}
