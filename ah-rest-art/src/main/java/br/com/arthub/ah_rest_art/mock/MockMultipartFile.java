package br.com.arthub.ah_rest_art.mock;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MockMultipartFile implements MultipartFile{
	private byte[] fileContent;
	private String fileName;
	private String contentType;
	
	
	@Override
	public String getName() {
		return fileName;
	}
	
	@Override
	public String getOriginalFilename() {
		return fileName;
	}
	
	@Override
	public boolean isEmpty() {
		return fileContent == null || fileContent.length == 0;
	}
	
	@Override
	public long getSize() {
		return fileContent.length;
	}
	
	@Override
	public byte[] getBytes() throws IOException {
		return fileContent;
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(fileContent);
	}
	
	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
		try(FileOutputStream fos = new FileOutputStream(dest)) {
			fos.write(fileContent);
		}
	}
}
