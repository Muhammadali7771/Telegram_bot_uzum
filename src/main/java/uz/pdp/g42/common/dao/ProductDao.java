package uz.pdp.g42.common.dao;

import lombok.RequiredArgsConstructor;
import uz.pdp.g42.common.dao.enums.FilePath;
import uz.pdp.g42.common.model.Product;
import uz.pdp.g42.common.service.FileService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ProductDao implements BaseDao<Product>{

    private final FileService<Product> fileService;

    @Override
    public void create(Product product) throws IOException {
        fileService.create(product, FilePath.PRODUCT, Product[].class);
    }

    @Override
    public Product getById(UUID id) {
        return null;
    }

    @Override
    public List<Product> list() throws IOException {
        return fileService.read(FilePath.PRODUCT, Product[].class);
    }
}
