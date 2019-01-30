package com.zjut.dropshipping.service;

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
     * @param phone 用户的电话(需要创建的文件夹名)
     * @return 上传文件的完整文件名
     */
    String upload(MultipartFile file, String path, String type, String phone);
}
