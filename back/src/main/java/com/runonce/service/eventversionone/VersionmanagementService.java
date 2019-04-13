package com.runonce.service.eventversionone;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventversionone.Versionmanagement;



/**
* 事项sheet页版本管理
*
* Created by xuxueli on '2019-01-28 15:06:51'.
*/
@Component
public interface VersionmanagementService extends Service<Versionmanagement> {

    /**
     * 查询sheet页版本
     * @param dmId
     * @param sheetNumber
     * @return
     */
     Integer getSheetVersion(String dmId,Integer sheetNumber);
    /**
     * 更新sheet页版本   更新版本时自增
     * @param dmId
     * @param sheetNumber
     * @return
     */
     Integer updateSheetVersion(String dmId, Integer sheetNumber);

}
