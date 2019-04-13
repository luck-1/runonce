package com.runonce.dao.eventversionone;

import com.runonce.model.eventversionone.ApplicationTypeField;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExportExcelDao {

    ApplicationTypeField selectApplicationTypeFileByEventIdAndParentId(ApplicationTypeField S);
    Integer selectApplicationTypeFileLeafNodeCountByEventId(ApplicationTypeField S);


}
