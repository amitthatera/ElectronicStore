package com.estore.service_impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.estore.custom_exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.estore.custom_exception.FileNotSupportedException;
import com.estore.entities.Images;
import com.estore.repository.ImageRepository;
import com.estore.services.FileServices;
import com.estore.custom_exception.ResourceNotFoundException;

@Service
public class FileServiceImpl implements FileServices {

	private final ImageRepository imageRepo;

	public FileServiceImpl(ImageRepository imageRepo){
		this.imageRepo = imageRepo;
	}
	
	Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
	
	@Override
	public String uploadFile(MultipartFile file, String path) {
		String fileName = file.getOriginalFilename();
		String randomId = UUID.randomUUID().toString();
		
		String newFileName = randomId.concat(Objects.requireNonNull(fileName).substring(fileName.lastIndexOf(".")));
		
		String filePath = path + File.separator + newFileName;
		
		File f = new File(path);
		if(!f.exists()) {
			if (!f.mkdirs()) {
				throw new ApiException("Failed to create directories at " + path);
			}
		}
		
		if (!Objects.requireNonNull(file.getContentType()).equals("image/jpeg") && !file.getContentType().equals("image/png")
				&& !file.getContentType().equals("image/webp")) {
			throw new FileNotSupportedException("Image Not Supported !! Only JPEG/PNG/WEBP Files Allowed !! ");
		}
		
		try {
			file.transferTo(Paths.get(filePath));
			logger.info("File Transferred");
		}catch (IllegalStateException | IOException e) {
			logger.error("Error: {} ",e.getMessage());
		}
		return newFileName;
	}

	@Override
	public Set<Images> uploadFile(MultipartFile[] files, String path) {
		Set<Images> images = new HashSet<>();
		for(MultipartFile file : files) {

			String fileName = file.getOriginalFilename();
			String randomId = UUID.randomUUID().toString();
			
			String newFileName = randomId.concat(Objects.requireNonNull(fileName).substring(fileName.lastIndexOf(".")));
			
			Images image = new Images();
			image.setImageName(newFileName);
			image.setImageType(file.getContentType());
			image.setImageSize(file.getSize());
			images.add(image);
			
			String filePath = path + File.separator + newFileName;
			
			File f = new File(path);
			if(!f.exists()) {
				if (!f.mkdirs()) {
					throw new ApiException("Failed to create directories at " + path);
				}
			}
			
			if (!Objects.requireNonNull(file.getContentType()).equals("image/jpeg") && !file.getContentType().equals("image/png")
					&& !file.getContentType().equals("image/webp")) {
				throw new FileNotSupportedException("Image Not Supported !! Only JPEG/PNG/WEBP Files Allowed !! ");
			}
			
			try {
				file.transferTo(Paths.get(filePath));
			}catch(IllegalStateException | IOException e) {
				logger.error("Upload File Error: {}", e.getMessage());
			}
		}
		this.imageRepo.saveAll(images);
		return images;
	}

	@Override
	public InputStream serveImage(String imageFolder, String fileName) {
		String path = Paths.get(imageFolder + fileName).toString();
		InputStream stream;
		try {
			stream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			throw new ResourceNotFoundException("IMAGE NOT AVAILABLE !!");
		}
		return stream;
	}

}
