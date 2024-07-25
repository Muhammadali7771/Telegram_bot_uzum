package uz.pdp.g42.common.dao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.ws.rs.GET;

@AllArgsConstructor
@Getter
public enum FilePath {
    CATEGORY("E:\\Bot\\MyUzumBotProject\\json\\category.json"),
    PRODUCT("E:\\Bot\\MyUzumBotProject\\json\\product.json"),
    ORDER("E:\\Bot\\MyUzumBotProject\\json\\order.json");
    private String path;
}
