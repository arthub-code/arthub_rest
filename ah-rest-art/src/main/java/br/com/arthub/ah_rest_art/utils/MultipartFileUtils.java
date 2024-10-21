package br.com.arthub.ah_rest_art.utils;

import java.io.File;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Base64;

import org.springframework.web.multipart.MultipartFile;

import br.com.arthub.ah_rest_art.constants.ContentType;
import br.com.arthub.ah_rest_art.dto.FileData;
import br.com.arthub.ah_rest_art.mock.MockMultipartFile;

public class MultipartFileUtils {
	public static MultipartFile convertToMultipartfile(FileData file) {
		return new MockMultipartFile(Base64.getDecoder().decode(file.getBase64()), file.getFileName(), file.getContentType());
	}
	
	public static MultipartFile convertToMultipartfile(FileData file, byte[] bytes) {
		return new MockMultipartFile(bytes, file.getFileName(), file.getContentType());
	}
	
	public static void validateContentTypes(FileData file, ContentType... types) {
		 if (file.getContentType() == null || file.getContentType().isBlank()) {
	        throw new RuntimeException("The file type is required.");
	    }
	    
	    boolean isValidType = false;
	    for (ContentType type : types) {
	        if (file.getContentType().equals(type.getType())) {
	            isValidType = true;
	            break;
	        }
	    }

	    if (!isValidType) {
	        throw new RuntimeException("The file type \"" + file.getContentType() + "\" is invalid for this operation.");
	    }
	}
	
	public static void validateContentTypes(MultipartFile file, ContentType... types) {
	    if (file.getContentType() == null || file.getContentType().isBlank()) {
	        throw new RuntimeException("The file type is required.");
	    }
	    
	    boolean isValidType = false;
	    for (ContentType type : types) {
	        if (file.getContentType().equals(type.getType())) {
	            isValidType = true;
	            break;
	        }
	    }

	    if (!isValidType) {
	        throw new RuntimeException("The file type \"" + file.getContentType() + "\" is invalid for this operation.");
	    }
	}
	
	public static String getBase64FileFromResources(String fileName) throws Exception {
	    try (InputStream is = MultipartFileUtils.class.getResourceAsStream("/dataset-test/" + fileName)) {
	        if (is == null) {
	            throw new IllegalArgumentException("File not found: " + fileName);
	        }
	        byte[] fileBytes = is.readAllBytes();
	        return Base64.getEncoder().encodeToString(fileBytes);
	    }
	}

	public static String getContentTypeFileFromResources(String fileName) throws Exception {
	    try (InputStream is = MultipartFileUtils.class.getResourceAsStream("/dataset-test/" + fileName)) {
	        if (is == null) {
	            throw new IllegalArgumentException("File not found: " + fileName);
	        }
	        return URLConnection.guessContentTypeFromStream(is);
	    }
	}

	public static FileData getFileDataFromResources(String fileName) throws Exception {
	    FileData fileData = new FileData();
	    fileData.setBase64(getBase64FileFromResources(fileName));
	    fileData.setFileName(fileName);
	    fileData.setContentType(getContentTypeFileFromResources(fileName));
	    return fileData;
	}
}
