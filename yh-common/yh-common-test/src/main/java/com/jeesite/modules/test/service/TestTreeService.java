/**
 * Copyright (c) 2013-Now http://winh-ai.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.test.service;

import com.jeesite.common.service.TreeService;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.test.dao.TestTreeDao;
import com.jeesite.modules.test.entity.TestTree;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 测试树表Service
 * @author Winhai
 * @version 2018-04-22
 */
@Service
public class TestTreeService extends TreeService<TestTreeDao, TestTree> {
	
	/**
	 * 获取单条数据
	 * @param testTree
	 * @return
	 */
	@Override
	public TestTree get(TestTree testTree) {
		return super.get(testTree);
	}
	
	/**
	 * 查询列表数据
	 * @param testTree
	 * @return
	 */
	@Override
	public List<TestTree> findList(TestTree testTree) {
		return super.findList(testTree);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param testTree
	 */
	@Override
	@Transactional
	public void save(TestTree testTree) {
		super.save(testTree);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(testTree, testTree.getId(), "testTree_image");
		// 保存上传附件
		FileUploadUtils.saveFileUpload(testTree, testTree.getId(), "testTree_file");
	}
	
	/**
	 * 更新状态
	 * @param testTree
	 */
	@Override
	@Transactional
	public void updateStatus(TestTree testTree) {
		super.updateStatus(testTree);
	}
	
	/**
	 * 删除数据
	 * @param testTree
	 */
	@Override
	@Transactional
	public void delete(TestTree testTree) {
		testTree.sqlMap().markIdDelete(); // 逻辑删除时标记ID值
		super.delete(testTree);
	}
	
}