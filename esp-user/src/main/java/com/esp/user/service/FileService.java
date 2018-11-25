package com.esp.user.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Service
public class FileService {
	
	@Value("${image.file.path:}")
	private String filePath;

	/**
	 * 把一个上传文件的列表中每一个文件上传指定路径下面最后这些文件相对路径组成的集合
	 * @param files
	 * @return
	 */
	public List<String> getImgPath(List<MultipartFile> files) {
	    if (Strings.isNullOrEmpty(filePath)) {
            filePath = getResourcePath();
        }
		List<String> paths = Lists.newArrayList();
		files.forEach(file -> {
			File localFile = null;
			if (!file.isEmpty()) {
				try {
					localFile =  saveToLocal(file);
					// D:/vip/images/a/ab/b/aaa.png
					// a/ab/b/aaa.png
					String path = StringUtils.substringAfterLast(localFile.getAbsolutePath(), filePath);
					if (path.contains("\\")) {
						path = path.replace("\\", "/");
					}
					paths.add(path);
				} catch (IOException e) {
					throw new IllegalArgumentException(e);
				}
			}
		});
		return paths;
	}
	
	public static String getResourcePath(){
	  File file = new File(".");
	  String absolutePath = file.getAbsolutePath();
	  return absolutePath;
	}

	/**
	 * 把文件保存到本地(图片服务: 提供上传接口)
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private File saveToLocal(MultipartFile file) throws IOException {
	 File newFile = new File(filePath + "/" + Instant.now().getEpochSecond() +"/"+file.getOriginalFilename());
	 if (!newFile.exists()) {
		 newFile.getParentFile().mkdirs();
		 newFile.createNewFile();
	 }
	 Files.write(file.getBytes(), newFile);
     return newFile;
	}
}
