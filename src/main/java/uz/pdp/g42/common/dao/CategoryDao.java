package uz.pdp.g42.common.dao;

import lombok.RequiredArgsConstructor;
import uz.pdp.g42.common.dao.enums.FilePath;
import uz.pdp.g42.common.model.Category;
import uz.pdp.g42.common.service.FileService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class CategoryDao implements BaseDao<Category>{

    private final FileService<Category> fileService;

    @Override
    public void create(Category category) throws IOException {
        fileService.create(category, FilePath.CATEGORY, Category[].class);
    }

    @Override
    public Category getById(UUID id) {
        return null;
    }

    @Override
    public List<Category> list() throws IOException {
        return fileService.read(FilePath.CATEGORY, Category[].class);
    }
}
