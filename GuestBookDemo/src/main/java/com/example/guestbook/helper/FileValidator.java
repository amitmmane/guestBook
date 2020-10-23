package com.example.guestbook.helper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public class FileValidator {
	
	

	public static boolean validateBySize(MultipartFile file) {

		if (!file.isEmpty()) {
			Double sizeInMb = getFileSizeMegaBytes(file);
			if (sizeInMb > 1) {
				return false;
			}
			return true;
		}
		return false;
	}

	public static boolean validateByExtension(MultipartFile file) {
		List<String> allowedFileExtenstion = Arrays.asList("png", "jpg", "jpeg");
		Optional<String> fileExtension = getExtensionByStringHandling(file.getOriginalFilename());

		if (fileExtension.isPresent()) {
			allowedFileExtenstion.contains(fileExtension.get().toLowerCase());
			return true;
		}
		return false;
	}
	
	private static double getFileSizeMegaBytes(MultipartFile file) {
		return (double) file.getSize() / (1024 * 1024);
	}

	private static Optional<String> getExtensionByStringHandling(String filename) {
		return Optional.ofNullable(filename).filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}

}