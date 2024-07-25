package uz.pdp.g42.common.service;

import lombok.RequiredArgsConstructor;
import uz.pdp.g42.common.dao.ProductDao;
import uz.pdp.g42.common.model.Product;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class ProductService implements BaseService<Product>{
    private final ProductDao productDao;

    public void create(Product product) throws IOException {
        productDao.create(product);
    }


    @Override
    public List<Product> list() throws IOException {
        return productDao.list();
    }

    @Override
    public List<Product> getById(UUID id) throws IOException {
        List<Product> list = productDao.list();
        return list.stream()
                .filter(product -> product.getCategoryId().equals(id))
                .toList();
    }

    ////////My Logic
    public Optional<Product> getProductById(UUID id) throws IOException {
        List<Product> list = productDao.list();
        return list.stream().filter(product -> product.getId().equals(id)).findFirst();
    }

    ////////My Logic
    public Optional<Product> getProductByPurchaseId(UUID id) throws IOException {
        List<Product> list = productDao.list();
        return list.stream().filter(product -> product.getPurchaseId().equals(id)).findFirst();
    }
}
