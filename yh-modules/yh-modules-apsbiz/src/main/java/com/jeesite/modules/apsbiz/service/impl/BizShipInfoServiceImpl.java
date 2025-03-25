
package com.jeesite.modules.apsbiz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeesite.modules.apsbiz.service.BizShipInfoService;
import com.jeesite.common.base.YhServiceImpl;
import com.jeesite.common.utils.MybatisPlusUtils;
import com.jeesite.modules.apsbiz.entity.BizShipInfo;
import com.jeesite.modules.apsbiz.mapper.BizShipInfoMapper;
import org.springframework.stereotype.Service;

/**
 * 缆柱表
 *
 * @author zhudi
 * @date 2024-12-08 21:19:17
 */
@Service
public class BizShipInfoServiceImpl extends YhServiceImpl<BizShipInfoMapper, BizShipInfo> implements BizShipInfoService {

	@Override
	public BizShipInfo infoByCode(String code) {
		BizShipInfo form = new BizShipInfo();
		form.setShipCode(code);
		QueryWrapper<BizShipInfo> queryWrapper = MybatisPlusUtils.getQueryWrapper(form, null);
		return getOne(queryWrapper);
	}

	@Override
	public BizShipInfo infoByName(String name) {
		BizShipInfo form = new BizShipInfo();
		form.setShipNameCn(name);
		QueryWrapper<BizShipInfo> queryWrapper = MybatisPlusUtils.getQueryWrapper(form, null);
		return getOne(queryWrapper);
	}
}
