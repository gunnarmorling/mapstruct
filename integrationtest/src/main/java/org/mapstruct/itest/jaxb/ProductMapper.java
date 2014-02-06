package org.mapstruct.itest.jaxb;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.itest.jaxb.category.Category;
import org.mapstruct.itest.jaxb.product.ObjectFactory;
import org.mapstruct.itest.jaxb.product.Product;

@Mapper( componentModel = "cdi", uses = { ObjectFactory.class, org.mapstruct.itest.jaxb.category.ObjectFactory.class } )
public interface ProductMapper {

    Product productEntityToJaxbProduct(ProductEntity product);

    Category categoryEntityToJaxbCategory(CategoryEntity category);

    List<Category> categoryEntityToJaxbCategory(List<CategoryEntity> category);
}
