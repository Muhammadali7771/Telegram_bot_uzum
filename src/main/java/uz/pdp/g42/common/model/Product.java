package uz.pdp.g42.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.glassfish.grizzly.compression.lzma.impl.Base;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class Product extends BaseModel {
    public Product(){
        purchaseId = UUID.randomUUID();
    }
    private String name;
    private int quantity;
    private double price;
    private UUID categoryId;
    /////////////
    private String addressOfPhoto;
    private UUID purchaseId;
}
