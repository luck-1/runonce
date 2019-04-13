//package com.runonce.controller.export;
//
//import com.runonce.ReturnCode;
//import com.runonce.model.eventversionone.*;
//import com.runonce.service.export.*;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//
//@RestController
//@RequestMapping("export")
//@Api(description = "办事指南导出")
//
//public class BusinessGuideExportController {
//
//    @Resource
//    private BusinessGuideAdministrativeLicensingOneExportService businessGuideAdministrativeLicensingOneExportService;
//
//    @Resource
//    private PrequalificationExportService prequalificationExportService;
//
//    @ApiOperation(value = "办事指南导出")
//    @PostMapping("AdministrativeLicensingOneExport")
//    public ReturnCode AdministrativeLicensingOneExport(String eventId) throws Exception {
//        businessGuideAdministrativeLicensingOneExportService.businessGuideExport(eventId);
//        return new ReturnCode.Builder().msg("导出成功").success().build();
//    }
//
//    @ApiOperation(value = "导出资格预审")
//    @PostMapping("prequalificationExport")
//    public ReturnCode PrequalificationExport(String eventId) throws Exception{
//        Prequalification prequalification = prequalificationExportService.businessGuideExport(eventId);
//        return new ReturnCode.Builder().msg("导出成功").success().object(prequalification).build();
//    }
//    @ApiOperation(value = "导出图片")
//    @PostMapping("pictureExport")
//    public ReturnCode pictureExport(String image)throws Exception{
//        prequalificationExportService.pictureExport(image);
//        return new ReturnCode.Builder().msg("图片导出成功").success().build();
//    }
//}
//
//
