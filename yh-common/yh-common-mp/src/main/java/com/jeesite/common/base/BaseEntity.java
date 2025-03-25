package com.jeesite.common.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.jeesite.common.annotation.JhyjField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BaseEntity<T extends BaseEntity<?>> implements Serializable {

	@JhyjField(tableField = false)
	@TableField(exist = false)
	public List<String> groupBy;

	@JhyjField(tableField = false)
	@TableField(exist = false)
	public List<String> orderBy;


}
