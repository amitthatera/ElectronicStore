package com.estore.services;

import java.io.InputStream;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.estore.entities.Images;

public interface FileServices {
	
	String uploadFile(MultipartFile file, String path);
	
	Set<Images> uploadFile(MultipartFile[] files, String path);
	
	InputStream serveImage(String imageFolder, String fileName);

}
