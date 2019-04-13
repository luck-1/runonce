package com.runonce.service.eventversionone;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import com.runonce.model.eventversionone.RevisionHistory;

import java.util.List;


/**
* 变更履历
*
* Created by xuxueli on '2019-02-19 10:28:47'.
*/
@Component
public interface RevisionHistoryService extends Service<RevisionHistory> {


    void add(RevisionHistory revisionhistory);

    List<RevisionHistory> findByDmId(String id);

    void updateInfo(RevisionHistory revisionhistory);
}
