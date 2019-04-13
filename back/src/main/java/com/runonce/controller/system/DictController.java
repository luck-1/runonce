
package com.runonce.controller.system;


import com.runonce.ReturnCode;
import com.runonce.httpbean.assets.reqbean.ImportDictReq;
import com.runonce.model.system.Dict;
import com.runonce.service.system.DictDataService;
import com.runonce.service.system.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



/**
 * @author Exrick
 */
@RestController
@Api(description = "字典管理接口")
@RequestMapping("/xboot/dict")
@Transactional(rollbackFor = Exception.class)
public class


DictController{

    @Autowired
    private DictService dictService;

    @Autowired
    private DictDataService dictDataService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping(value = "/getAll",method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public ReturnCode getAll(){

        List<Dict> list = dictService.getAll();
        return new ReturnCode.Builder().success().msg("返回成功").object(list).build();
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加")
    public ReturnCode add(@RequestBody Dict dict){

        if(dictService.findByType(dict.getType())!=null){
            return new ReturnCode.Builder().failed().msg("字典类型Type已存在").build();
        }
        dictService.save(dict);
         return new ReturnCode.Builder().success().msg("成功").build();
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ApiOperation(value = "编辑")
    public ReturnCode edit(@RequestBody Dict dict){

        Dict old = dictService.get(dict.getId());
         //若type修改判断唯一
        if(!old.getType().equals(dict.getType())&&dictService.findByType(dict.getType())!=null){
            return new ReturnCode.Builder().failed().msg("字典类型Type已存在").build();
        }
        dictService.update(dict);
        return new ReturnCode.Builder().success().msg("编辑成功").build();
    }

    @RequestMapping(value = "/delByIds/{id}",method = RequestMethod.DELETE)
    @ApiOperation(value = "通过id删除")
    public ReturnCode delAllByIds(@PathVariable String id){


        dictService.delete(id);
        dictDataService.deleteByDictId(id);
        return new ReturnCode.Builder().success().msg("删除成功").build();
    }

    @RequestMapping(value = "/search",method = RequestMethod.GET)
    @ApiOperation(value = "搜索字典")
    public ReturnCode searchPermissionList(@RequestParam String key){

        List<Dict> list = dictService.findByTitleOrTypeLike(key);
        return new ReturnCode.Builder().success().msg("搜索成功").object(list).build();
    }


    @RequestMapping(value = "/dict", method = RequestMethod.GET)
    @ApiOperation(value = "推送所有字典")
    public ReturnCode sendDict() {
        return dictService.sendDict();
    }


}

