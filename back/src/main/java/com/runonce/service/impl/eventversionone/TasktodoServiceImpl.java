package com.runonce.service.impl.eventversionone;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.model.eventpublic.EventsTable;
import com.runonce.model.eventversionone.DepartmentalMatters;
import com.runonce.model.http.SelectDepartmentalMattersListParam;
import com.runonce.model.http.TasktodoReq;
import com.runonce.model.system.Department;
import com.runonce.service.eventversionone.DepartmentalMattersService;
import com.runonce.service.system.DepartmentService;
import com.runonce.util.SysUtil;
import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.TasktodoService;
import com.runonce.dao.eventversionone.TasktodoDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversionone.Tasktodo;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* taskToDo
*
* Created by xuxueli on '2019-02-21 11:20:11'.
*/
@Service
public class TasktodoServiceImpl extends AbstractService<Tasktodo> implements TasktodoService  {

	@Resource
	private TasktodoDao tasktodoDao;

	@Resource
	private DepartmentalMattersService departmentalMattersService;

	@Resource
	private DepartmentService departmentService;

	@Override
	public PageInfo<Tasktodo>  findByUserId(String userId, TasktodoReq tasktodoReq) {
		PageHelper.startPage(tasktodoReq.getCurrentPage(), tasktodoReq.getPageSize());
		List<Tasktodo> tasktodoList = tasktodoDao.findByUserId(userId);
		PageInfo<Tasktodo> pageInfo = new PageInfo<>(tasktodoList);
		//return pageInfo;
		return pageInfo;
	}

	@Transactional
	@Override
	public Map clickTodo(String id, HttpServletRequest request) {
		//置状态为2
		Tasktodo tasktodo= new Tasktodo();
		tasktodo.setId(id);
		tasktodo.setState(2);
		tasktodoDao.updateByPrimaryKeySelective(tasktodo);
		//查询todo
		Tasktodo tasktodoReturn=tasktodoDao.selectOne(tasktodo);
		String dmId=tasktodoReturn.getDmId();
		Integer type=tasktodoReturn.getType();

		//根据类型返回数据
		if(type==1){
			SelectDepartmentalMattersListParam selectDepartmentalMattersListParam=new SelectDepartmentalMattersListParam();
			selectDepartmentalMattersListParam.setDmId(dmId);
			selectDepartmentalMattersListParam.setShowTree(false);
			selectDepartmentalMattersListParam.setPage(1);
			selectDepartmentalMattersListParam.setSize(10);
			return departmentalMattersService.selectDepartmentalMattersListByParam(selectDepartmentalMattersListParam,request);

		}else {
			SelectDepartmentalMattersListParam selectDepartmentalMattersListParam=new SelectDepartmentalMattersListParam();
			selectDepartmentalMattersListParam.setDmId(dmId);
			selectDepartmentalMattersListParam.setShowTree(false);
			selectDepartmentalMattersListParam.setPage(1);
			selectDepartmentalMattersListParam.setSize(10);
			return departmentalMattersService.selectAllByParam(selectDepartmentalMattersListParam);

		}
		//返回数据
		//return null;
	}



	/**
	 * 给领导发送审批通知
	 *
	 * @param dmId
	 * @return
	 */
	public Boolean sendTodo(String dmId,String eventName ,Integer type, List<String > userIds,String content) {

		if(userIds!=null&&userIds.size()>0){
		//推送给人员
		if (userIds != null && userIds.size() > 0) {
			for (String userId : userIds) {
				Tasktodo tasktodo = new Tasktodo();
				tasktodo.setDmId(dmId);
				tasktodo.setId(SysUtil.getUUID());
				tasktodo.setState(1);
				tasktodo.setType(type);
				tasktodo.setWho(userId);
				tasktodo.setContent(eventName + content);
				tasktodoDao.insert(tasktodo);
			}
			return true;
		}}
		return false;
	}



}
