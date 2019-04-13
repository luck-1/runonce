package com.runonce.dao.eventversionone;

import com.runonce.dao.MyMapper;
import com.runonce.model.http.PdfToImageModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PdfToImageModelDao  extends MyMapper<PdfToImageModel> {

    List<PdfToImageModel> findByMaterialId(@Param("materialId") String materialId);

    PdfToImageModel findBySourceName(@Param("sourceName")String sourceName);
}
