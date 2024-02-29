package com.premade_dishes_system.controller.system;


import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.util.SaResult;
import com.premade_dishes_system.service.product.ProductInfoService;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/file")
@SaCheckRole("provider")
@CrossOrigin("*")
public class FileController {

    //    上传路径
    @Value("${upload-path}")
    private String uploadPath;

    @Autowired
    private ProductInfoService service;

    /**
     * 上传货物图片
     *
     * @param proImg 原始文件度睇下
     * @param pid    货物id
     * @return 结果
     */
    @SneakyThrows(value = IOException.class)
    @PostMapping("/uploadProImg")
    public SaResult uploadProImg(@RequestParam("img") MultipartFile proImg, int pid) {
        // 获取文件原始名称
        String fileName = proImg.getOriginalFilename();
        //获取文件后缀
        String extension = "." + FilenameUtils.getExtension(fileName);
        //生成新的文件名称
        String newFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + UUID.randomUUID().toString().replace("-", "") + extension;
        // 根据货物id生成目录
//        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dirPath = uploadPath + "/product/" + pid;
        File dir = new File(dirPath);
        if (!dir.exists())
            dir.mkdirs();
        System.out.println(newFileName + " " + dirPath);

        //清除原先图片
        FileUtils.cleanDirectory(dir);
        // 处理文件上传
        File resFile = new File(dir, newFileName);
        proImg.transferTo(resFile);

        if (service.updateImg("/src/assets/product/" + pid + "/" + newFileName, pid))
            return SaResult.ok("success");
        return SaResult.error("failed");
    }
}
