package uz.pdp.g42.common.service;

import lombok.RequiredArgsConstructor;
import uz.pdp.g42.common.dao.OrderDao;
import uz.pdp.g42.common.model.Order;
import uz.pdp.g42.common.model.Product;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class OrderService {
    private final OrderDao orderDao;
    public void add(Order order) throws IOException {
        orderDao.create(order);
    }
    public List<Order> list() throws IOException {
       return orderDao.list();
    }


    //My Logic
    public boolean hasProduct(Long chatId, UUID productId) throws IOException {
       return orderDao.list().stream()
                .filter(order -> order.getChatId().equals(chatId))
                .anyMatch(order -> order.getProduct().getId().equals(productId));
    }

    public List<Order> getMyOrders(Long chatId) throws IOException {
        return orderDao.list().stream()
                .filter(order -> order.getChatId().equals(chatId))
                .toList();
    }
}
