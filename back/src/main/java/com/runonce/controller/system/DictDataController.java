
package com.runonce.controller.system;


import com.runonce.ReturnCode;
import com.runonce.model.PageVo;
import com.runonce.model.system.Dict;
import com.runonce.model.system.DictData;
import com.runonce.service.system.DictDataService;
import com.runonce.service.system.DictService;
import com.runonce.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



/**
 * @author Exrick
 */
@RestController
@Api(description = "字典数据管理接口")
@RequestMapping("/xboot/dictData")
@CacheConfig(cacheNames = "dictData")
@Transactional(rollbackFor = Exception.class)
public class DictDataController{

    @Autowired
    private DictService dictService;

    @Autowired
    private DictDataService dictDataService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping(value = "/getByCondition",method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取用户列表")
    public ReturnCode getByCondition(@ModelAttribute DictData dictData,
                                     @ModelAttribute PageVo pageVo){

        Page<DictData> page = dictDataService.findByCondition(dictData, PageUtil.initPage(pageVo));
        return new ReturnCode.Builder().success().msg("获取成功").object(page).build();
    }

    @RequestMapping(value = "/getByType/{type}",method = RequestMethod.GET)
    @ApiOperation(value = "通过类型获取")
    public ReturnCode getByType(@PathVariable String type){

        Dict dict = dictService.findByType(type);
        if (dict == null) {
            return new ReturnCode.Builder().failed().msg("字典类型Type不存在").build();
        }
        List<DictData> list = dictDataService.findByDictId(dict.getId());
        return new ReturnCode.Builder().success().msg("查询成功").object(list).build();
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加")
    public ReturnCode add(@RequestBody DictData dictData){

        Dict dict = dictService.get(dictData.getDictId());
        if (dict == null) {
            return new ReturnCode.Builder().failed().msg("字典类型id不存在").build();
        }
        dictDataService.save(dictData);
        return new ReturnCode.Builder().success().msg("添加成功").build();
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ApiOperation(value = "编辑")
    public ReturnCode edit(@RequestBody DictData dictData){

        dictDataService.update(dictData);
        // 删除缓存
        Dict dict = dictService.get(dictData.getDictId());
        return new ReturnCode.Builder().success().msg("编辑成功").build();
    }

    @RequestMapping(value = "/delByIds/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public ReturnCode delByIds(@PathVariable String[] ids){

        for(String id : ids){
            Dict dict = dictService.get(id);
            dictDataService.delete(id);
        }
        return new ReturnCode.Builder().success().msg("批量通过id删除数据成功").build();
    }
}

