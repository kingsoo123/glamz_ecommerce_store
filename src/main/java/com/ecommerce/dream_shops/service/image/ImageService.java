package com.ecommerce.dream_shops.service.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.dream_shops.dto.ImageDto;
import com.ecommerce.dream_shops.exceptions.ResourceNotFoundExcception;
import com.ecommerce.dream_shops.model.Image;
import com.ecommerce.dream_shops.model.Product;
import com.ecommerce.dream_shops.repository.ImageRepository;
import com.ecommerce.dream_shops.service.product.IProductService;

import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final IProductService productService;
    @Override
    public Image getImagebYId(Long id) {
       return imageRepository.findById(id).orElseThrow(()-> new ResourceNotFoundExcception("Image not found"));
    }

    @Override
    public void deleteImageById(Long id) {
       imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, ()->{
        throw new ResourceNotFoundExcception("No image found with ID: " + id);
       });
    }

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
       Product product = productService.getProductById(productId);
       List<ImageDto> savedImageDto = new ArrayList<>();

       for (MultipartFile file : files) {
        try {
            String imageUrl = "/api/v1/images/image/download/";
            Image image = Image.builder()
            .fileName(file.getName())
            .fileType(file.getContentType())
            .image(new SerialBlob(file.getBytes()))
            .product(product)
            .build();

            Image savedImage = imageRepository.save(image);
             Long id = savedImage.getId();

             savedImage.setDownloadUrl(imageUrl + id);

             imageRepository.save(savedImage);

             ImageDto imageDto = ImageDto.builder()
             .fileName(savedImage.getFileName())
             .downloadUrl(savedImage.getDownloadUrl())
             .build();

             savedImageDto.add(imageDto);
        } catch (SQLException | IOException e) {
          throw new RuntimeException(e.getMessage());
        }
       }

       return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
      Image image = getImagebYId(imageId);
      try {
        image.setFileType(file.getContentType());
        image.setFileName(file.getOriginalFilename());
        image.setImage(new SerialBlob(file.getBytes()));
        imageRepository.save(image);
      } catch (IOException | SQLException e) {
        throw new RuntimeException(e.getMessage());
      }
    }  
    
}
