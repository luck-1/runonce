package com.runonce.controller.eventpublic;

import com.aliyun.oss.OSSClient;
import com.runonce.ReturnCode;
import com.runonce.util.OssClientUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Author: zhaolei
 * @Descriptions:
 * @Date: create at 2019/1/3 0003 下午 1:37
 */
@RestController
@RequestMapping("picture")
@Api(description ="图片获取")
public class GetPictureController {

    @GetMapping("getpicture")
    @ApiOperation(value = "根据图片路径获取")
    public ReturnCode add(@RequestParam String fileName) {
        if(StringUtils.isEmpty(fileName)){
            return new ReturnCode.Builder().failed().msg("图片路径为空").build();
        }
        String picBase64 = "";
        try {
            ByteArrayOutputStream bo = OssClientUtil.getObjectData(fileName);
            if(bo == null){
                return new ReturnCode.Builder().success().msg("图片不存在").build();
            }
            picBase64 = new String(Base64.encodeBase64(bo.toByteArray()));
        }catch (Exception e){
            return new ReturnCode.Builder().failed().msg("获取图片失败").build();
        }
        return new ReturnCode.Builder().success().msg("图片获取成功").object(picBase64).build();
    }

    @PostMapping("uploadfile/{suffix}")
    @ApiOperation("图片上传")
    public ReturnCode uploadPicture(@RequestBody MultipartFile file,@PathVariable String suffix){
        OSSClient client = OssClientUtil.getOssClient();
        String key = "defautSystemDocking/defaultSystemDockingPic"+suffix+".png";
        try {
            client.putObject("ikingrun",key,file.getInputStream());
        } catch (IOException e) {
            return new ReturnCode.Builder().failed().msg("图片上传失败").build();
        }
        return new ReturnCode.Builder().success().msg("图片上传成功").object(key).build();
    }
}
