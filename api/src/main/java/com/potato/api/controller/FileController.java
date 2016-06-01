package com.potato.api.controller;

import com.potato.api.framework.bean.Response;
import com.potato.api.framework.util.PropsUtil;
import com.potato.api.model.UploadResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by zhangcs on 2016/5/25.
 */
@RestController
public class FileController {

    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    public Response upload(HttpServletRequest request) throws IOException {
        String path = request.getParameter("path");
        String basePath = request.getSession().getServletContext().getRealPath("/WEB-INF/file/") + "\\" + path;

        List<String> fileNameList = new ArrayList<>();

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        if (fileMap != null && fileMap.size() > 0) {
            for (String fName : fileMap.keySet()) {
                MultipartFile mFile = fileMap.get(fName);
                UploadResult uploadResult = upload(mFile, basePath);
                if(uploadResult.isSuccess()){
                    fileNameList.add("/WEB-INF/file/" + path + "/" + uploadResult.getFileNmae());
                }else {
                    fileNameList.add(uploadResult.getFileNmae());
                }
            }
        }
        return new Response().success(fileNameList);
    }

    private UploadResult upload(MultipartFile mFile, String basePath) throws IOException {
        UploadResult uploadResult=new UploadResult();
        File file = new File(basePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        // 得到上传的文件的文件名
        String originalFilename = mFile.getOriginalFilename();
        String exName = getExtensionName(originalFilename);

        if (!validateExName(exName)) {
            uploadResult.setIsSuccess(false);
            uploadResult.setFileNmae("不支持的文件类型");
            return uploadResult;
        }

        String filename = UUID.randomUUID().toString() + "." + exName;
        InputStream inputStream = mFile.getInputStream();
        byte[] b = mFile.getBytes();
        int length = inputStream.read(b);
        String path = basePath + "\\" + filename;
        // 文件流写到服务器端
        FileOutputStream outputStream = new FileOutputStream(path);
        outputStream.write(b, 0, length);
        inputStream.close();
        outputStream.close();

        uploadResult.setIsSuccess(true);
        uploadResult.setFileNmae(filename);
        return uploadResult;
    }


    /**
     * 验证扩展名
     */
    private boolean validateExName(String exName) {
        String exNames = PropsUtil.getString(PropsUtil.loadProps("config.properties"), "upload.file_type");
        String[] arrEx = exNames.split(",");
        for (String ex : arrEx) {
            if (exName.equalsIgnoreCase(ex)) {
                return true;
            }
        }
        return false;
    }

    private String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }
}
