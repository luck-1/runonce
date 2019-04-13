package com.runonce.service.eventversionone;
import com.runonce.ReturnCode;
import com.runonce.model.eventversionone.MaterialGrouping;
import com.runonce.model.http.FormDescriptionParam;
import com.runonce.model.http.FormThatParam;
import com.runonce.model.http.FormThatStateParm;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventversionone.FormDescription;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 表单说明
 *
 * Created by xuxueli on '2018-12-18 13:17:37'.
 */
@Component
public interface FormDescriptionService extends Service<FormDescription> {

    ReturnCode add(FormDescriptionParam  formDescriptionParam,HttpServletRequest request);

    JSONObject showPictureText(String objectName);

    FormThatStateParm findPhoto1(String eventId);

    List<FormDescription> findMaterial(FormThatParam formThatParam);
}
