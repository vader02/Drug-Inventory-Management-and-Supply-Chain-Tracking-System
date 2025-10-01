package com.inventorymanagement.dto;

import java.math.BigDecimal;
import java.util.regex.Pattern;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;
import com.inventorymanagement.model.Product;

public class ProductAddRequest {
	
	private int id;
    private String title;
	private String description;
	private int quantity;
    private BigDecimal price;
    private int categoryId;
    private BigDecimal purchasePrice;
    private int supplierId;
    private MultipartFile image;
    
    // Validation patterns
    private static final Pattern TITLE_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s-]{2,50}$");
    private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s,.()-]{2,200}$");
    private static final Pattern IMAGE_PATTERN = Pattern.compile(".*\\.(jpg|jpeg|png)$");
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public MultipartFile getImage() {
		return image;
	}
	public void setImage(MultipartFile image) {
		this.image = image;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public int getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}
	
	public static Product toEntity(ProductAddRequest dto) {
		Product entity = new Product();
		BeanUtils.copyProperties(dto, entity, "image", "categoryId");		
		return entity;
	}
	
    @Override
	public String toString() {
		return "ProductAddRequest [id=" + id + ", title=" + title + ", description=" + description + ", quantity="
				+ quantity + ", price=" + price + ", categoryId=" + categoryId + ", purchasePrice=" + purchasePrice
				+ ", supplierId=" + supplierId + ", image=" + image + "]";
	}
	
	public static boolean validateProduct(ProductAddRequest request) {
        // Check for null values
        if (request == null || 
            request.getTitle() == null || 
            request.getDescription() == null || 
            request.getPrice() == null ||
            request.getPurchasePrice() == null ||
            request.getCategoryId() <= 0 || 
            request.getSupplierId() <= 0 ||
            request.getQuantity() < 0) {
            return false;
        }

        // Regex validations
        if (!TITLE_PATTERN.matcher(request.getTitle()).matches()) {
            // Title: 2-50 chars, alphanumeric, spaces, and hyphens
            return false;
        }

        if (!DESCRIPTION_PATTERN.matcher(request.getDescription()).matches()) {
            // Description: 2-200 chars, alphanumeric, spaces, and common punctuation
            return false;
        }

        if (request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            // Price must be positive
            return false;
        }

        if (request.getPurchasePrice().compareTo(BigDecimal.ZERO) <= 0) {
            // Purchase price must be positive
            return false;
        }

        // Image validation (only if image is provided)
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            if (!IMAGE_PATTERN.matcher(request.getImage().getOriginalFilename().toLowerCase()).matches()) {
                // Image must be jpg, jpeg, or png
                return false;
            }
            if (request.getImage().getSize() > 5 * 1024 * 1024) {
                // Image size must be less than 5MB
                return false;
            }
        }

        return true;
    }
	
	public static boolean validateUpdateProduct(ProductAddRequest request) {
        // Check for null values and id
        if (request == null || 
            request.getId() <= 0 ||
            request.getTitle() == null || 
            request.getDescription() == null || 
            request.getPrice() == null ||
            request.getPurchasePrice() == null ||
            request.getCategoryId() <= 0 || 
            request.getSupplierId() <= 0) {
            return false;
        }

        // Regex validations
        if (!TITLE_PATTERN.matcher(request.getTitle()).matches()) {
            // Title: 2-50 chars, alphanumeric, spaces, and hyphens
            return false;
        }

        if (!DESCRIPTION_PATTERN.matcher(request.getDescription()).matches()) {
            // Description: 2-200 chars, alphanumeric, spaces, and common punctuation
            return false;
        }

        if (request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            // Price must be positive
            return false;
        }

        if (request.getPurchasePrice().compareTo(BigDecimal.ZERO) <= 0) {
            // Purchase price must be positive
            return false;
        }

        // Image validation (only if image is provided)
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            if (!IMAGE_PATTERN.matcher(request.getImage().getOriginalFilename().toLowerCase()).matches()) {
                // Image must be jpg, jpeg, or png
                return false;
            }
            if (request.getImage().getSize() > 5 * 1024 * 1024) {
                // Image size must be less than 5MB
                return false;
            }
        }

        return true;
    }
}