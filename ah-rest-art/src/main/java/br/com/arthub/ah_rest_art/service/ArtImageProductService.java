package br.com.arthub.ah_rest_art.service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.arthub.ah_rest_art.dto.ArtImageProductData;
import br.com.arthub.ah_rest_art.entity.ArtEntity;
import br.com.arthub.ah_rest_art.entity.ArtImageProductEntity;
import br.com.arthub.ah_rest_art.repository.ArtImageProductRepository;

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
			ArtImageProductEntity imgProd = new ArtImageProductEntity();
			imgProd.setImageBytes(image.getBytes());	
			imgProd.setArtParent(art);
			ArtImageProductEntity registered = this.imgProductRepository.saveAndFlush(imgProd);
			
			registered.setImageLink(buildImageLink(registered.getArtImageProductId()));
			this.imgProductRepository.saveAndFlush(registered);
		} catch(IOException e) {
			throw new RuntimeException("Could not save image bytes to artwork product image.");
		} catch(Exception e) {
			throw new RuntimeException("An unexpected error occurred while trying to add the product image to the artwork.");
		}
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
		return "art/imageProd/get/" + refId;
	}
}
