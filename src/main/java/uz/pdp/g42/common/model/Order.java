package uz.pdp.g42.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Order {
    private Long chatId;
    private Product product;

}
