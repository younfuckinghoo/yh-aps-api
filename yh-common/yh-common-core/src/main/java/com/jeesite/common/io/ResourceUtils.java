/**
 * Copyright (c) 2013-Now http://winh-ai.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.io;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.lang.ExceptionUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;

/**
 * 资源供给类
 * @author Winhai
 * @version 2016-9-16
 */
public class ResourceUtils extends org.springframework.util.ResourceUtils {
	
	private static final ResourceLoader resourceLoader;
	private static final ResourcePatternResolver resourceResolver;
	static{
		resourceLoader = new DefaultResourceLoader();
		resourceResolver = new PathMatchingResourcePatternResolver(resourceLoader);
	}
	
	/**
	 * 获取资源加载器（可读取jar内的文件）
	 * @author Winhai
	 */
	public static ResourceLoader getResourceLoader() {
		return resourceLoader;
	}
	
	/**
	 * 获取ClassLoader
	 */
	public static ClassLoader getClassLoader() {
		return resourceLoader.getClassLoader();
	}
	
	/**
	 * 获取资源加载器（可读取jar内的文件）
	 */
	public static Resource getResource(String location) {
		return resourceLoader.getResource(location);
	}
	
	/**
	 * 获取资源文件流（用后记得关闭）
	 * @param location
	 * @author Winhai
	 * @throws IOException 
	 */
	public static InputStream getResourceFileStream(String location) throws IOException{
		Resource resource = resourceLoader.getResource(location);
		return resource.getInputStream();
	}
	
	/**
	 * 获取资源文件内容
	 * @param location
	 * @author Winhai
	 */
	public static String getResourceFileContent(String location){
		try(InputStream is = ResourceUtils.getResourceFileStream(location)){
			return IOUtils.toString(is, EncodeUtils.UTF_8);
		}catch (IOException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}
	
	/**
	 * Spring 搜索资源文件
	 * @param locationPattern
	 * @author Winhai
	 */
	public static Resource[] getResources(String locationPattern){
		try {
			Resource[] resources = resourceResolver.getResources(locationPattern);
			return resources;
		} catch (IOException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}
	
}
