/**
 * Copyright (c) 2013-Now http://winh-ai.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.cms.service;

import com.jeesite.modules.cms.entity.Article;
import com.jeesite.modules.cms.entity.Category;
import com.jeesite.modules.cms.entity.Site;

/**
 * 页面缓存服务接口
 * @author Winhai
 * @version 2023-4-7
 */
public interface PageCacheService {

	/**
	 * 根据文章清理页面缓存
	 * @author Winhai
	 */
	void clearCache(Article article);

	/**
	 * 根据栏目清理页面缓存
	 * @author Winhai
	 */
	void clearCache(Category category);

	/**
	 * 根据栏目清理页面缓存
	 * @author Winhai
	 */
	void clearCache(Site site);

}