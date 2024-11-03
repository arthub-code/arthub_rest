package br.com.arthub.ah_rest_art.service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.arthub.ah_rest_art.constants.ContentType;
import br.com.arthub.ah_rest_art.dto.ArtImageProductData;
import br.com.arthub.ah_rest_art.dto.FileData;
import br.com.arthub.ah_rest_art.entity.ArtEntity;
import br.com.arthub.ah_rest_art.entity.ArtImageProductEntity;
import br.com.arthub.ah_rest_art.repository.ArtImageProductRepository;
import br.com.arthub.ah_rest_art.utils.MultipartFileUtils;

@Service
public class ArtImageProductService {
	@Autowired
	private ArtImageProductRepository imgProductRepository;
	
	/**
	 * @param art
	 * @param image
	 * 
	 * <p>Adicona uma imagem de produto a arte no sistema. Realiza validações no arquivo de imagem.</p>
	 * */
	public void doAddImageProductToArt(ArtEntity art, MultipartFile image) {
		if(this.imgProductRepository.existsByArtId(art.getArtId()))
			throw new RuntimeException("Artwork already has the associated product image. Try updating it instead of adding it.");
		try {
			MultipartFileUtils.validateContentTypes(image, ContentType.JPEG, ContentType.PNG, ContentType.WEBP);
			ArtImageProductEntity imgProd = new ArtImageProductEntity();
			imgProd.setImageBytes(image.getBytes());	
			imgProd.setArtParent(art);
			imgProd.setFileName(image.getOriginalFilename());
			imgProd.setContentType(image.getContentType());
			ArtImageProductEntity registered = this.imgProductRepository.saveAndFlush(imgProd);
			
			registered.setImageLink(buildImageLink(registered.getArtImageProductId()));
			this.imgProductRepository.saveAndFlush(registered);
		} catch(IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not save image bytes to artwork product image.");
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("An unexpected error occurred while trying to add the product image to the artwork.");
		}
	}
	
	public void doDeleteByArtId(UUID artId) {
		Optional<ArtImageProductEntity> op = this.imgProductRepository.getImageProductByArtId(artId);
		if(op.isEmpty())
			throw new RuntimeException("Product image not associated with art.");
		this.imgProductRepository.delete(op.get());
	}
	
	public MultipartFile getImageProdByArtId(UUID artId) {
		Optional<ArtImageProductEntity> op = this.imgProductRepository.getImageProductByArtId(artId);
		if(op.isEmpty())
			throw new RuntimeException("Product image not associated with art.");
		
		FileData fileData = new FileData();
		fileData.setFileName(op.get().getFileName());
		fileData.setContentType(op.get().getContentType());
		
		return MultipartFileUtils.convertToMultipartfile(fileData, op.get().getImageBytes());
	}
	
	public void doUpdateByArtId(UUID artId, MultipartFile image) {
		Optional<ArtImageProductEntity> op = this.imgProductRepository.getImageProductByArtId(artId);
		if(op.isEmpty())
			throw new RuntimeException("Product image not associated with art.");
		
		ArtImageProductEntity imgProd = op.get();
		try {			
			imgProd.setFileName(image.getOriginalFilename());
			imgProd.setContentType(image.getContentType());
			imgProd.setImageBytes(image.getBytes());
		} catch(IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not save image bytes to artwork product image.");
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("An unexpected error occurred while trying to update the product image to the artwork.");
		}
		
		this.imgProductRepository.saveAndFlush(imgProd);
	}
	
	public ArtImageProductData getImageProductByArtId(UUID artId) {
		Optional<ArtImageProductEntity> op = this.imgProductRepository.getImageProductByArtId(artId);
		if(op.isEmpty())
			return null;
		
		return new ArtImageProductData(op.get());
	}
	
	public boolean haveRef(UUID artId) {
		return this.imgProductRepository.existsByArtId(artId);
	}
	
	private String buildImageLink(UUID refId) {
		return "imageProd/get-image/" + refId;
	}
}
