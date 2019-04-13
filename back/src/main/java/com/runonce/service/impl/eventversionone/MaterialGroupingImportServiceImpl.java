package com.runonce.service.impl.eventversionone;

import com.runonce.ReturnCode;
import com.runonce.exception.CustomException;
import com.runonce.httpbean.assets.resbean.DepartmentalMattersAndEventRes;
import com.runonce.httpbean.assets.resbean.ImportExcelRes;
import com.runonce.model.http.MaterialGroupAddParam;
import com.runonce.model.http.MaterialGroupVersionParam;
import com.runonce.service.eventversionone.MaterialGroupingImportService;
import com.runonce.service.eventversionone.VersionmanagementService;
import com.runonce.util.EXUtil;
import com.runonce.util.ImportUtil;
import com.runonce.util.SysUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MaterialGroupingImportServiceImpl implements MaterialGroupingImportService {

    @Autowired
    private VersionmanagementService versionmanagementService;


    @Override
    public void materialGroupingImport(Workbook workbook, Sheet sheet, String  version, DepartmentalMattersAndEventRes departmentalMattersAndEventRes, ImportExcelRes importExcelRes) {

        MaterialGroupVersionParam materialGroupVersionParam=new MaterialGroupVersionParam();
        List<MaterialGroupAddParam> materialGroupingsList = new ArrayList<>();


        try {
            // 为跳过第一行目录设置count
            int isRead = 100;
            int count = 0;
            int isReadEnd = 0;
            int startNum = 0;
            int startNum2 = 0;
            //备注列num
            int remarkNum = 0;
            //材料提供方式num
            int materialProvideWayNum = 0;
            //预受理num
            int acceptanceNum = 0;
            //是否属于可容缺的材料num
            int isShortageNum = 0;
            //材料份数
            int countNum = 0;

            String currType = "";


            for (Row row : sheet) {
                MaterialGroupAddParam materialGrouping = new MaterialGroupAddParam();
                if(row == null){
                    return;
                }
                int lastRowNum = sheet.getLastRowNum();
                if(row.getRowNum() > lastRowNum){
                    return;
                }
                try {
                    //查找从哪一行读取
                    if(row.getCell(0) != null ){
                        if("1".equals(row.getCell(0).toString())||"1.0".equals(row.getCell(0).toString())){
                            isRead = row.getRowNum();
                        }
                    }else{
                        continue;
                    }

                    if (isRead != 100) {
                        if (row.getCell(0) == null) {
                            isReadEnd = row.getRowNum();
                        }
                    }
                    //查找从哪一列读取
                    if (count < isRead) {
                        for (int i = 0; i < row.getLastCellNum(); i++) {
                            String str = ""+getValue(row, i) .replaceAll("\\s*","");
                            if ("窗口提交材料要求".equals(str)) {
                                if(startNum != 0){
                                    continue;
                                }
                                startNum = i;
                            }
                            if ("审批输出物".equals(str) || "审批的输出物".equals(str)) {
                                if(startNum2 != 0){
                                    continue;
                                }
                                startNum2 = i;
                            }
                            if (str.indexOf("备注") == 0) {
                                if(remarkNum != 0){
                                    continue;
                                }
                                remarkNum = i;
                            }
                            if ("材料提供方式".equals(str)) {
                                if(materialProvideWayNum != 0){
                                    continue;
                                }
                                materialProvideWayNum = i;
                            }
                            if ("预受理".equals(str)) {
                                if(acceptanceNum != 0){
                                    continue;
                                }
                                acceptanceNum = i;
                            }
                            if ("是否属于可容缺的材料".equals(str)) {
                                if(isShortageNum != 0){
                                    continue;
                                }
                                isShortageNum = i;
                            }
                            if (str.indexOf("份数") > -1 || "数量".equals(str)) {
                                if(countNum != 0){
                                    continue;
                                }
                                countNum = i;
                            }
                        }
                        count++;
                        continue;
                    }

                    if (isReadEnd != 0) {
                        if (row.getRowNum() == isReadEnd) {
                            continue;
                        }
                    }

                    materialGrouping.setId(SysUtil.getUUID());
                    System.out.println(materialGroupingsList.size());

                    if("".equals(getValue(row, 0))){
                        continue;
                    }
                    materialGrouping.setEventId(departmentalMattersAndEventRes.getId());
                    materialGrouping.setOrderNum(getValue(row, 0));

                    if (getValue(row, 1) != null && !"".equals(getValue(row, 1))) {
                        currType = getType(getValue(row, 1));
                    }

                    materialGrouping.setType(currType);
                    materialGrouping.setNumber(getValue(row, 2));
                    materialGrouping.setIo(getValue(row, 3));
                    materialGrouping.setName(getValue(row, 4));

                    //原件提供
                    String str = ""+getValue(row, startNum).replaceAll("\\s*","");
                    if (str != null && str.equals("√")) {
                        materialGrouping.setOriginalScriptProvide(true);
                    } else {
                        materialGrouping.setOriginalScriptProvide(false);
                    }

                    //复印件提供
                    str = ""+getValue(row, startNum + 1).replaceAll("\\s*","");
                    if (str != null && str.equals("√")) {
                        materialGrouping.setCopyProvide(true);
                    } else {
                        materialGrouping.setCopyProvide(false);
                    }

                    //电子件提供
                    str = ""+getValue(row, startNum + 2).replaceAll("\\s*","");
                    if (str != null && str.equals("√")) {
                        materialGrouping.setElectronicProvide(true);
                    } else {
                        materialGrouping.setElectronicProvide(false);
                    }

                    if( startNum2 != 0){
                        //审核输出原件
                        str = ""+getValue(row, startNum2).replaceAll("\\s*","");
                        if (str != null && str.equals("√")) {
                            materialGrouping.setOriginalScriptOutput(true);
                        } else {
                            materialGrouping.setOriginalScriptOutput(false);
                        }

                        //审核输出电子件
                        str = ""+getValue(row, startNum2 + 1).replaceAll("\\s*","");
                        if (str != null && str.equals("√")) {
                            materialGrouping.setElectronicOutput(true);
                        } else {
                            materialGrouping.setElectronicOutput(false);
                        }
                    }
                    //材料要求份数
                    String ii = getValue(row, countNum);
                    if (ii != null && !"".equals(ii)) {
                        Double f = 0.0;
                        try{
                            f = Double.valueOf(ii);
                        }catch(NumberFormatException e){
                            e.printStackTrace();
                        }
                        Integer countInter = (int) Math.ceil(f);
                        materialGrouping.setCount(countInter);
                    }

                    //备注
                    if(remarkNum != 0 && getValue(row, remarkNum) != null){
                        materialGrouping.setRemarks(getValue(row, remarkNum).trim());
                    }
                    //是否预受理
                    if(acceptanceNum != 0 && getValue(row, acceptanceNum) != null){
                        materialGrouping.setPreAcceptance(getValue(row, acceptanceNum).trim());
                    }

                    //材料提供方式
                    if(materialProvideWayNum != 0 && getValue(row, materialProvideWayNum) != null){
                       materialGrouping.setProvideWay(getValue(row, materialProvideWayNum).trim());
                    }

                    //是否属于可容缺的材料
                    if(isShortageNum != 0 && getValue(row, isShortageNum) != null){
                        if(Arrays.asList(new String[]{"是","√"}).contains(getValue(row, isShortageNum).trim())){
                            materialGrouping.setIsItIndispensable(true);
                        }else{
                            materialGrouping.setIsItIndispensable(false);
                        }
                    }
                    //样表
                    List<String> sampleTableList = new ArrayList();
                    Map<String,List> map = new HashMap<>();
                    map.put("imgs",sampleTableList);
                    JSONObject jsonObject = new JSONObject(map);
                    materialGrouping.setSampleTable(jsonObject.toString());
                    if ("2.0".equals(version)) {
                        //材料来源(职能部门/自制)
                        materialGrouping.setSource(getValue(row, startNum2 + 4) == null ? "" : getValue(row, startNum2 + 4).trim());
                        //职能部门名称
                        materialGrouping.setDepartmrntName(getValue(row, startNum2 + 5) == null ? "" : getValue(row, startNum2 + 5).trim());
                        //职能部门系统名称
                        materialGrouping.setDepartmrntSystemName(getValue(row, startNum2 + 6) == null ? "" : getValue(row, startNum2 + 6).trim());
                        //职能部门系统网址
                        materialGrouping.setDepartmrntSystemUrl(getValue(row, startNum2 + 7) == null ? "" : getValue(row, startNum2 + 7).trim());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //是否预受理
                materialGrouping.setAcceptance(new ArrayList<>());
                materialGrouping.setApproval(new ArrayList<>());
                materialGroupingsList.add(materialGrouping);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        materialGroupVersionParam.setList(materialGroupingsList);
        //查询当前版本
        materialGroupVersionParam.setVersion(versionmanagementService.getSheetVersion(departmentalMattersAndEventRes.getId(),2));

        importExcelRes.setMaterialGroupVersionParam(materialGroupVersionParam);
    }

    private static String getType(String Type) {

        if (Type != null && !"".equals(Type)) {
            if (Type.contains("证件")) {
                return "证件";
            }
            if (Type.contains("证明")) {
                return "证明";
            }
            if (Type.contains("申请表")) {
                return "申请表";
            }
        }
        return "";
    }

    private static String getValue(Row row, int cellNumber) {

        Cell cell = row.getCell(cellNumber);
        if (cell == null) {
            return "";
        }
        String string = ImportUtil.getValue(cell);

        return string;
    }



}
