package com.jeesite.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jeesite.common.annotation.JhyjDeleted;
import com.jeesite.common.annotation.JhyjField;
import com.jeesite.common.base.CommonConstants;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class MybatisPlusOracleUtils {

	/**
	 * 组织修改条件
	 * @param t
	 * @return
	 */
	public static <T> UpdateWrapper<T> getUpdateWrapper(T t){
		// 新建修改对象
		UpdateWrapper<T> updateWrapper = new UpdateWrapper();
		// 获取待修改对象所有字段
		Field[] fields = ReflectUtil.getFields(t.getClass());
		// 遍历每一个字段
		for(Field field : fields){
			// 查看该字段是否含有JhyjField标签，如果没有标签过滤
			JhyjField jhyjField = field.getDeclaredAnnotation(JhyjField.class);
			if(jhyjField != null) {
				// 是否为表中的字段
				if(!jhyjField.tableField()){
					continue;
				}

				// 获取表中字段名，默认规则为 将bean中对象名改为下划线格式
				String fieldName = jhyjField.fieldName();
				if(StrUtil.isEmpty(fieldName)){
					fieldName = UnderlineToCamelUtils.camelToUnderline(field.getName()).toLowerCase(Locale.ROOT);
				}

				// 获取该字段的值
				Object value = ReflectUtil.getFieldValue(t, field);
				// 如果该字段值不为空，则走各项匹配规则
				if (ObjectUtil.isNotNull(value)) {
					// 过滤非字段类属性... 后续可在此处追加过滤内容
					if (field.getName().indexOf("serialVersionUID") > -1) {
						continue;
					}
					if (field.getType().getName().indexOf("Class") > -1) {
						continue;
					}
					// 如果符合清空标识，将该字段清空
					if (CommonConstants.CLEAR_FLAG.equals(Convert.toStr(value))
							|| CommonConstants.CLEAR_NUM_FLAG.equals(Convert.toStr(value))
							|| Convert.toStr(CommonConstants.CLEAR_DATE_FLAG).equals(Convert.toStr(value))) {
						updateWrapper.set(fieldName, null);
					} else {
						updateWrapper.set(fieldName, value);
					}
				}
			}

			// 获取主键标签，通过该标签进行修改
			TableId tableId = field.getDeclaredAnnotation(TableId.class);
			if(tableId != null) {
				updateWrapper.eq(UnderlineToCamelUtils.camelToUnderline(field.getName()).toLowerCase(Locale.ROOT), ReflectUtil.getFieldValue(t, field));
			}
		}
		return updateWrapper;
	}

	/**
	 * 组织查询条件
	 * @param t 待查询条件
	 * @param alias 表 别名
	 * @return
	 */
	public static <T> QueryWrapper<T> getQueryWrapper(T t, String alias) {
		if(StrUtil.isEmpty(alias)){
			alias = "";
		}else{
			alias += ".";
		}
		QueryWrapper<T> queryWrapper = new QueryWrapper();
		Field[] fields = ReflectUtil.getFields(t.getClass());
		List<String> fileNames = new ArrayList<>();
		TableId tableId = null;
		Field fieldId = null;
		for(Field field : fields){
			String finalAlias = alias;
			// 获取字段值
			Object value = ReflectUtil.getFieldValue(t, field);

			// 处理删除标识
			JhyjDeleted jhyjDeleted = field.getDeclaredAnnotation(JhyjDeleted.class);
			// 默认添加deleted条件。如果deleted设置为-1；标识不添加假删限制
			if(jhyjDeleted != null && StrUtil.isNotEmpty(jhyjDeleted.fieldName()) && (null == value || Convert.toInt(value) != -1)){
				queryWrapper.eq(finalAlias + jhyjDeleted.fieldName(), jhyjDeleted.available());
			}

			// 处理查询表示
			JhyjField jhyjField = field.getDeclaredAnnotation(JhyjField.class);
			StringBuilder fieldName = new StringBuilder();
			if(jhyjField != null){
				fieldName.append(jhyjField.fieldName());
				if(fieldName.length() == 0){
					fieldName.append(UnderlineToCamelUtils.camelToUnderline(field.getName()).toLowerCase(Locale.ROOT));
				}
				fileNames.add(fieldName.toString());

				String fieldAlias = jhyjField.fieldAlias();
				if(StrUtil.isNotEmpty(fieldAlias)){
					finalAlias = fieldAlias + '.';
				}
			}

			if((ObjectUtil.isNotNull(value) && !"".equals(value))
					&& null != jhyjField){
				// 相等
				if(jhyjField.eq()){
					queryWrapper.eq(finalAlias + fieldName, value);
				}

				// 小于
				if(jhyjField.lt()){
					queryWrapper.lt(finalAlias + fieldName, value);
				}

				// 小于等于
				if(jhyjField.le()){
					queryWrapper.le(finalAlias + fieldName, value);
				}

				// 大于
				if(jhyjField.gt()){
					queryWrapper.gt(finalAlias + fieldName, value);
				}

				// 大于等于
				if(jhyjField.ge()){
					queryWrapper.ge(finalAlias + fieldName, value);
				}

				// 包含
				if(jhyjField.in()){
					if(Convert.toStr(value).indexOf(",") > -1){
						String[] splitValues = Convert.toStr(value).split(",");
						List<Object> listValues = new ArrayList<>();
						for(String splitValue : splitValues){
							listValues.add(splitValue);
						}
						queryWrapper.in(finalAlias + fieldName, listValues);
					}else{
						queryWrapper.in(finalAlias + fieldName, value);
					}
				}

				// 模糊查询
				if(jhyjField.like()){
					queryWrapper.like(finalAlias + fieldName, value);
				}

				// 左模糊查询
				if(jhyjField.likeLeft()){
					queryWrapper.likeLeft(finalAlias + fieldName, value);
				}

				// 右模糊查询
				if(jhyjField.likeRight()){
					queryWrapper.likeRight(finalAlias + fieldName, value);
				}

				// 不等查询
				if(jhyjField.ne()){
					queryWrapper.ne(finalAlias + fieldName, value);
				}

				// 日期相等
				if(jhyjField.dateEq()){
					queryWrapper.apply( "TO_CHAR("+finalAlias + fieldName+",'YYYY-MM-DD') = TO_CHAR({0},'YYYY-MM-DD')", value);
				}

				// 日期大于等于
				if(jhyjField.dateGe()){
					queryWrapper.apply("TO_CHAR("+finalAlias + fieldName+",'YYY-MM-DD') >= TO_CHAR({0},'YYY-MM-DD')",value);
				}

				// 日期小于等于
				if(jhyjField.dateLe()){
					queryWrapper.apply("TO_CHAR("+finalAlias + fieldName+",'YYY-MM-DD') <= TO_CHAR({0},'YYY-MM-DD')",value);
				}

				// 日期大于
				if(jhyjField.dateGt()){
					queryWrapper.apply("TO_CHAR("+finalAlias + fieldName+",'YYY-MM-DD') > TO_CHAR({0},'YYY-MM-DD')",value);
				}

				// 日期小于
				if(jhyjField.dateLt()){
					queryWrapper.apply("TO_CHAR("+finalAlias + fieldName+",'YYY-MM-DD') < TO_CHAR({0},'YYY-MM-DD')",value);
				}

				// 日期时间相等
				if(jhyjField.datetimeEq()){
					queryWrapper.apply("TO_CHAR("+finalAlias + fieldName+",'YYY-MM-DD HH24:MI:SS') = TO_CHAR({0},'YYY-MM-DD HH24:MI:SS')",value);
				}

				// 日期时间大于等于
				if(jhyjField.datetimeGe()){
					queryWrapper.apply("TO_CHAR("+finalAlias + fieldName+",'YYYY-MM-DD HH24:MI:SS') >= TO_CHAR({0},'YYYY-MM-DD HH24:MI:SS')",value);
				}

				// 日期时间小于等于
				if(jhyjField.datetimeLe()){
					queryWrapper.apply("TO_CHAR("+finalAlias + fieldName+",'YYYY-MM-DD HH24:MI:SS') <= TO_CHAR({0},'YYYY-MM-DD HH24:MI:SS')",value);
				}

				// 日期时间大于
				if(jhyjField.datetimeGt()){
					queryWrapper.apply("TO_CHAR("+finalAlias + fieldName+",'YYY-MM-DD HH24:MI:SS') > TO_CHAR({0},'YYY-MM-DD HH24:MI:SS')",value);
				}

				// 日期时间小于
				if(jhyjField.datetimeLt()){
					queryWrapper.apply("TO_CHAR("+finalAlias + fieldName+",'YYY-MM-DD HH24:MI:SS') < TO_CHAR({0},'YYY-MM-DD HH24:MI:SS')",value);
				}


				// 日期 年份 相等
				if(jhyjField.dateYearEq()){
					queryWrapper.eq("YEAR("+finalAlias + fieldName+")", value);
				}

				// 日期 月份 相等
				if(jhyjField.dateMonthEq()){
					queryWrapper.eq("MONTH("+finalAlias + fieldName+")", value);
				}

				// 日期 日 相等
				if(jhyjField.dateDayEq()){
					queryWrapper.eq("DAY("+finalAlias + fieldName+")", value);
				}

				// 日期 季度 相等
				if(jhyjField.dateQuarterEq()){
					queryWrapper.eq("QUARTER("+finalAlias + fieldName+")", value);
				}

				// 组合查询
				if(jhyjField.searchLike()){
					String searchAlias = finalAlias;
					String[] fieldNameArr = fieldName.toString().split(",");
					Consumer<QueryWrapper<T>> consumer = new Consumer<QueryWrapper<T>>(){
						@Override
						public void accept(QueryWrapper<T> wrapperSon) {
							Boolean needOr = false;
							for(String fieldName : fieldNameArr){
								if(needOr) { wrapperSon.or(); }
								wrapperSon.like(searchAlias +fieldName, value);
								needOr = true;
							}
						}
					};
					queryWrapper.and(consumer);
				}

				// 组合查询
				if(jhyjField.nullField()){
					queryWrapper.isNull(alias + value);
				}
			}

			TableId temp = field.getDeclaredAnnotation(TableId.class);
			if(temp != null){
				tableId = temp;
				fieldId = field;
			}
		}


		// 处理排序
		Object orderBy = ReflectUtil.getFieldValue(t, "orderBy");
		List<String> orderByList = null;
		if(orderBy != null){
			orderByList = (List<String>)orderBy;
		}
		if(CollUtil.isNotEmpty(orderByList)){
			for(String order : orderByList) {
				if(order.indexOf(";") > -1) {
					List<String> orders = StrUtil.split(order, ';');
					String orderField = "";
					if(orders.get(0).indexOf(".") > -1){
						orderField = StrUtil.split(orders.get(0), '.').get(1);
					}else{
						orderField = orders.get(0);
					}

					if(fileNames.contains(orderField)){
						if("asc".equals(orders.get(1))) {
							queryWrapper.orderByAsc(orders.get(0));
						}else {
							queryWrapper.orderByDesc(orders.get(0));
						}
					}
				}
			}
		}else{
			if(tableId != null) {
				queryWrapper.orderByDesc(alias + (StrUtil.isEmpty(tableId.value()) ? fieldId.getName() : tableId.value()));
			}
		}

		// 处理分组
		Object groupBy = ReflectUtil.getFieldValue(t, "groupBy");
		List<String> groupByList = null;
		if(groupBy != null){
			groupByList = (List<String>)groupBy;
		}
		if(CollUtil.isNotEmpty(groupByList)){
			for(String group : groupByList) {
				String groupAlias = "";
				String groupField = "";
				if(group.indexOf(".") > -1){
					List<String> groups = StrUtil.split(group, '.');
					groupAlias = groups.get(0) + ".";
					groupField = groups.get(1);
				}else{
					groupField = group;
				}
				if(fileNames.contains(groupField)) {
					queryWrapper.groupBy(groupAlias+groupField);
				}
			}
		}

		return queryWrapper;
	}



	/**
	 * 组织（逻辑）删除条件
	 * @param t 待查询条件
	 * @return
	 */
	public static <T> UpdateWrapper<T> getDeleteWrapper(T t) {
		UpdateWrapper<T> updateWrapper = new UpdateWrapper();

		Field[] fields = ReflectUtil.getFields(t.getClass());
		for(Field field : fields){
			JhyjDeleted jhyjDeleted = field.getDeclaredAnnotation(JhyjDeleted.class);
			if(jhyjDeleted != null && StrUtil.isNotEmpty(jhyjDeleted.fieldName())){
				updateWrapper.set(jhyjDeleted.fieldName(), jhyjDeleted.deleted());
			}
		}
		return updateWrapper;
	}

	public static <T> QueryWrapper<T> getReadWrapper(String queryFields, T t) {
		// 填写查询字段
		QueryWrapper<T> readWrapper = new QueryWrapper<>();
		Field[] fields = ReflectUtil.getFields(t.getClass());
		List<String> objFields = new ArrayList<>();
		for(Field field : fields){
			TableField tableField = field.getDeclaredAnnotation(TableField.class);
			if(tableField == null || tableField.exist()){
				objFields.add(field.getName());
			}
		}
		String[] queryFieldArr = queryFields.split(",");
		List<String> queryFieldList = new ArrayList<>();
		for(String queryField : queryFieldArr){
			String filed = queryField;
			String alias = "";
			if(queryField.indexOf(".") > -1){
				List<String> s = StrUtil.split(queryField,'.');
				filed = s.get(1);
				alias = s.get(0)+".";
			}
			if(objFields.contains(filed)){
				queryFieldList.add(alias+UnderlineToCamelUtils.camelToUnderline(filed));
			}
		}
		if(CollectionUtils.isEmpty(queryFieldList)){
			return null;
		}
		readWrapper.last(StrUtil.join(",", queryFieldList));
		return readWrapper;
	}

	//public static <T> void setClearFlag(T t) {
	//	// 获取待修改对象所有字段
	//	Field[] fields = ReflectUtil.getFields(t.getClass());
	//	// 遍历每一个字段
	//	for(Field field : fields){
	//		// 获取该字段的值
	//		Object value = ReflectUtil.getFieldValue(t, field);
	//		if(null == value){
	//			if(field.getType().getName().contains("String")){
	//				ReflectUtil.setFieldValue(t, field, CommonConstants.CLEAR_FLAG);
	//			}
	//			if(StrUtil.containsAny(field.getType().getName(), "Integer","Float","Double")){
	//				ReflectUtil.setFieldValue(t, field, CommonConstants.CLEAR_FLAG);
	//			}
	//		}
	//	}
	//}
}
