/**
 * Copyright (c) 2013-Now http://winh-ai.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.sys.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.sys.entity.CompanyOffice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * 公司机构DAO接口
 * @author Winhai
 * @version 2017-03-23
 */
@MyBatisDao
@ConditionalOnProperty(name="user.enabled", havingValue="true", matchIfMissing=true)
public interface CompanyOfficeDao extends CrudDao<CompanyOffice> {
	
}