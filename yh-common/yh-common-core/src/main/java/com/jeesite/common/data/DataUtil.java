package com.jeesite.common.data;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class DataUtil {


    /**
	 * 将一个list均分成n个list,主要通过偏移量来实现的
     * @param list 要拆分的list
     * @param n 拆分后每组数据最大值
     * @return
     */
	public static <T> List<List<T>> averageAssignByGroupNum(List<T> list,int n){
		List<List<T>> result=new ArrayList<List<T>>();
		// 集合为空或 n <= 0 直接返回
		if (CollectionUtils.isEmpty(list) || n <= 0) {
			return result;
		}
		int remaider=list.size()%n;  //(先计算出余数)
		int number=list.size()/n;  //然后是商
		int offset=0;//偏移量
		for(int i=0;i<n;i++){
			List<T> value=null;
			if(remaider>0){
				value=list.subList(i*number+offset, (i+1)*number+offset+1);
				remaider--;
				offset++;
			}else{
				value=list.subList(i*number+offset, (i+1)*number+offset);
			}
			result.add(value);
		}
		return result;
	}

	/**
	 * 将List拆分成多个List, 每个子list最大包含n个元素
	 * @param n 分成多少份
	 * @return
	 */
	public static <T> List<List<T>> averageAssignByEleNum(List<T> list, int n) {
		List<List<T>> result=new ArrayList<List<T>>();
		if (CollectionUtils.isEmpty(list) || n <= 0) {
			return result;
		}
		int size = list.size();
		if (size <= n) {
			// 数据量不足 n 指定的大小
			result.add(list);
		} else {
			int pre = size / n;
			int last = size % n;
			// 前面pre个集合，每个大小都是 n 个元素
			for (int i = 0; i < pre; i++) {
				List<T> itemList =  new ArrayList<>();
				for (int j = 0; j < n; j++) {
					itemList.add(list.get(i * n + j));
				}
				result.add(itemList);
			}
			// last的进行处理
			if (last > 0) {
				List<T> itemList = new ArrayList<>();
				for (int i = 0; i < last; i++) {
					itemList.add(list.get(pre * n + i));
				}
				result.add(itemList);
			}
		}
		return result;
	}


	/**
     * 将list转换成map
     * @param list
     * @return
     */
    public static <T> Map<Object, T> list2map(List<T> list, String colName) {
        Map<Object, T> map = new HashMap<Object, T>();
        list.forEach((t) ->{
			if(t != null){
				map.put(ReflectUtil.getFieldValue(t, colName), t);
			}
		});
        return map;
    }
	public static <T> Map<Object, List<T>> list2mapList(List<T> list, String colName) {
		Map<Object, List<T>> map = new HashMap<>();
		list.forEach((t) ->{
			if(t != null){
				Object key = ReflectUtil.getFieldValue(t, colName);
				List<T> l = map.get(key);
				if(null == l){
					l = new ArrayList<>();
				}
				l.add(t);
				map.put(key, l);
			}
		});
		return map;
	}

	/**
	 * 查询集合是否包含指定值
	 * @param value 指定值
	 * @param objectList 集合
	 * @param fieldName 指定字段名
	 * @return
	 */
	public static <T> Boolean isContain(String value, List<T> objectList, String fieldName) {
		if(StrUtil.isEmpty(value) || objectList == null || objectList.size() == 0){
			return false;
		}
		for(T obj : objectList){
			Object fieldValue = ReflectUtil.getFieldValue(obj, fieldName);
			if(value.equals(Convert.toStr(fieldValue))){
				return true;
			}
		}
		return false;
	}

	/**
	 * 为字典值类字段 赋名称
	 * @param data			指定值
	 * @param objectList	给定范围
	 * @param matchKey		给定范围 与指定值匹配的字段
	 * @param matchValue	匹配成功 返回的字段
	 * @return
	 */
	public static <T> String getLabel(String data, List<T> objectList, String matchKey, String matchValue, String defaultVale) {
		if(StrUtil.isEmpty(data) || objectList == null || objectList.size() == 0 || StrUtil.isEmpty(matchKey) || StrUtil.isEmpty(matchValue)){
			return StrUtil.isEmpty(defaultVale) ? "<数据缺失1>" : defaultVale; // 原始数据问题
		}
		for(T obj : objectList){
			Object fieldValue = ReflectUtil.getFieldValue(obj, matchKey);
			if(data.equals(Convert.toStr(fieldValue))){
				Object value = ReflectUtil.getFieldValue(obj, matchValue);
				return ObjectUtil.isNotNull(value) ? Convert.toStr(value) : (StrUtil.isEmpty(defaultVale) ? "<数据缺失2>" : defaultVale); // 指定值不在给定范围内
			}
		}
		return StrUtil.isEmpty(defaultVale) ? "<数据缺失3>" : defaultVale;
	}

	/**
	 * 获取map中第一个key值
	 * @param map 数据源
	 * @return
	 */
	public static String getKeyOrNull(Map<String, Object> map) {
		String obj = null;
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			obj = entry.getKey();
			if (obj != null) {
				break;
			}
		}
		return  obj;
	}

	/**
	 * 计算船舶类型
	 * @param type
	 * @return
	 */
	public static Integer getShipType(Integer type) {
		if (null == type) {
			return -1;
		} else {
			if (Convert.toStr(type).length() == 1) {
				if ("6".equals(Convert.toStr(type))) {
					return 60;
				}

				if ("7".equals(Convert.toStr(type))) {
					return 70;
				}

				if ("8".equals(Convert.toStr(type))) {
					return 80;
				}
			} else {
				String num1;
				String num2;
				if (Convert.toStr(type).length() != 2) {
					if (Convert.toStr(type).length() == 3) {
						num1 = type.toString().substring(0, 1);
						num2 = type.toString().substring(1, 2);
						String num3 = type.toString().substring(2, 3);
						if ("1".equals(num1)) {
							return getShipType(Convert.toInt(type.toString().substring(1, 3)));
						}

						return 0;
					}
				} else {
					num1 = type.toString().substring(0, 1);
					num2 = type.toString().substring(1, 2);
					if ("2".equals(num1) && (Convert.toInt(num2) < 1 || Convert.toInt(num2) > 4)) {
						return 20;
					}

					if ("4".equals(num1) && (Convert.toInt(num2) < 1 || Convert.toInt(num2) > 4)) {
						return 40;
					}

					if ("6".equals(num1) && (Convert.toInt(num2) < 1 || Convert.toInt(num2) > 4)) {
						return 60;
					}

					if ("7".equals(num1)) {
						if (0 < Convert.toInt(num2) && Convert.toInt(num2) < 5) {
							return type;
						}

						return 70;
					}

					if ("8".equals(num1)) {
						if (0 < Convert.toInt(num2) && Convert.toInt(num2) < 5) {
							return type;
						}

						return 80;
					}
				}
			}

			return -1;
		}
	}

	/**
	 * 获取指定格式日期字符串
	 * @param date
	 * @return
	 */
	public static String getYYYYMMDD(LocalDateTime date) {
		return LocalDateTimeUtil.format(date,"YYYYMMdd");
	}


	/**
	 * 返回实体的所有为 null 字段
	 * @param source
	 * @return
	 */
	public static String[] getNullPropertyNames (Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<>();
		for(java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}


	/**
	 * 判断一个SQL是否为查询SQL
	 * @param sql
	 * @return
	 */
	public static boolean isSelect(String sql) {
		String upperCaseSql = sql.trim().toUpperCase();
		return upperCaseSql.startsWith("SELECT");
	}

    public static <T> List<Map<String, Object>> dictList(List<T> data) {
		List<Map<String, Object>> list = new ArrayList<>();
		for(T t : data){
			Map<String, Object> map = new HashMap<>();
			map.put("label", ReflectUtil.getFieldValue(t, "label"));
			map.put("value", ReflectUtil.getFieldValue(t, "value"));
			list.add(map);
		}
		return list;
    }


	/**
	 * 获取增删改列表
	 * @param newList
	 * @param oldList
	 * @param key
	 * @return
	 * @param <T>
	 */
	public static <T> Map<String, List<T>> saveUpdateDelList(List<T> newList, List<T> oldList, String key) {
		Map<Object, T> oldMap =list2map(oldList, key);
		List<T> saveList = new ArrayList<>();
		List<T> updateList = new ArrayList<>();
		List<T> delList = new ArrayList<>();
		for(T newT : newList){
			Object oldKey = ReflectUtil.getFieldValue(newT,key);
			T oldT = oldMap.get(oldKey);
			if(null != oldT){
				ReflectUtil.setFieldValue(newT,"id",ReflectUtil.getFieldValue(oldT,"id"));
				updateList.add(newT);
				oldMap.remove(oldKey);
			}else{
				saveList.add(newT);
			}
		}
		delList = oldMap.values().stream().collect(Collectors.toList());

		Map<String, List<T>> map = new HashMap<>();
		map.put("save",saveList);
		map.put("update",updateList);
		map.put("del",delList);

		return map;
	}


	/**
	 * map的key类型转换为str
	 * @param map
	 * @return
	 * @param <T>
	 */
	public static <T> Map<Object, T> mapK2Str(Map<Object, T> map) {
		Map<Object, T> newMap = new HashMap<>();
		map.forEach((k,v) ->{
			newMap.put(Convert.toStr(k), v);
		});

		return newMap;
	}

//
//    /**
//     *
//     * @param list 待处理的list
//     * @param map 键值对结合
//     * @param fromColumn 来源字段名
//     * @param toColumn 指定字段名
//     * @param defaultV 默认值
//     */
//    public <T> void replaceColumn(
//            List<T> list,
//            Map<String, String> map,
//            String fromColumn,
//            String toColumn,
//            String defaultV
//    ) {
//        for (T obj : list) {
//            String r = StrUtil.isEmpty(defaultV) ? "" : defaultV;
//			Object v = ReflectUtil.getFieldValue(obj, fromColumn);
//
//            if (null != v && StrUtil.isNotEmpty(map.get(v.toString()))) {
//                r = map.get(v.toString());
//            }
//			ReflectUtil.setFieldValue(obj, toColumn, r);
//        }
//    }
//
//    /**
//     * 根据日期获取日期的YYYYMM
//     * @param date 要解析的日期
//     */
//    public static String getYYYYMM(Date date) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
//        return sdf.format(date);
//    }
//
//    /**
//     * 计算两个日期的天数
//     * @return
//     */
//    public static Integer getDayOff(Date start, Date end) {
//        if (start == null || end == null) {
//            return 0;
//        }
//        Long days = (end.getTime() - start.getTime()) / (1000 * 3600 * 24);
//        return days.intValue();
//    }
//
//
//
//    /**
//     * 判断字符串是否为数据
//     * @param str
//     * @return
//     */
//    public static boolean isNumeric(String str){
//        try {
//            Convert.toInt(str);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//
//
//
//	/**
//	 * 获取map中第一个数据值
//	 *
//	 * @param map 数据源
//	 * @return
//	 */
//	public static Object getFirstOrNull(Map<String, Object> map) {
//		Object obj = null;
//		for (Map.Entry<String, Object> entry : map.entrySet()) {
//			obj = entry.getValue();
//			if (obj != null) {
//				break;
//			}
//		}
//		return  obj;
//	}
}
