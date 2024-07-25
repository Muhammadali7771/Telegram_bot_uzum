package uz.pdp.g42.common.dao;

import lombok.RequiredArgsConstructor;
import uz.pdp.g42.common.dao.enums.FilePath;
import uz.pdp.g42.common.model.Order;
import uz.pdp.g42.common.service.FileService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
@RequiredArgsConstructor
public class OrderDao implements BaseDao<Order>{
    private final FileService<Order> fileService;
    @Override
    public void create(Order order) throws IOException {
        fileService.create(order, FilePath.ORDER, Order[].class);
    }

    @Override
    public Order getById(UUID id) {
        return null;
    }

    @Override
    public List<Order> list() throws IOException {
        return fileService.read(FilePath.ORDER, Order[].class);
    }
}
