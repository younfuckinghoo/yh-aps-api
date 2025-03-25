
package com.jeesite.modules.apsbiz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jeesite.common.utils.MybatisPlusUtils;
import com.jeesite.modules.apsbiz.service.BizBerthInfoService;
import com.jeesite.common.base.YhServiceImpl;
import com.jeesite.modules.apsbiz.entity.BizBerthInfo;
import com.jeesite.modules.apsbiz.mapper.BizBerthInfoMapper;
import org.springframework.stereotype.Service;

/**
 * 泊位表
 *
 * @author zhudi
 * @date 2024-12-08 21:19:16
 */
@Service
public class BizBerthInfoServiceImpl extends YhServiceImpl<BizBerthInfoMapper, BizBerthInfo> implements BizBerthInfoService {

    @Override
    public BizBerthInfo infoByBerthNo(String berthNo) {
        BizBerthInfo form = new BizBerthInfo();
        form.setBerthNo(berthNo);
        QueryWrapper<BizBerthInfo> queryWrapper = MybatisPlusUtils.getQueryWrapper(form, null);
        return getOne(queryWrapper);
    }
}
