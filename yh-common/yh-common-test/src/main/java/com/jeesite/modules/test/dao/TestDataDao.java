/**
 * Copyright (c) 2013-Now http://winh-ai.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.test.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.test.entity.TestData;

import java.util.List;
import java.util.Map;

/**
 * 测试数据DAO接口
 * @author Winhai
 * @version 2018-04-22
 */
@MyBatisDao
public interface TestDataDao extends CrudDao<TestData> {
	
	/**
	 * 演示Map参数和返回值，支持分页
	 */
	List<Map<String, Object>> findListForMap(Map<String, Object> params);
	
}