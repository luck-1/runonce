package com.runonce.dao.eventversionone;

import com.runonce.dao.MyMapper;
import com.runonce.model.eventversionone.ProcessDescriptionPic;

import java.util.List;

public interface ProcessDescriptionPicDao  extends MyMapper<ProcessDescriptionPic> {
    List<ProcessDescriptionPic> findByEventId(String eventId);
    int updateEvent(ProcessDescriptionPic ProcessDescriptionPic);
    int deleteByEventId(String EventId);
}
