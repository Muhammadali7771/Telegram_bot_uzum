package uz.pdp.g42.admin;

import uz.pdp.g42.bot.service.InlineMarkupService;
import uz.pdp.g42.common.dao.CategoryDao;
import uz.pdp.g42.common.dao.ProductDao;
import uz.pdp.g42.common.model.Category;
import uz.pdp.g42.common.model.Product;
import uz.pdp.g42.common.service.CategoryService;
import uz.pdp.g42.common.service.FileService;
import uz.pdp.g42.common.service.ProductService;

import java.io.IOException;
import java.util.Calendar;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scannerStr = new Scanner(System.in);
        Scanner scannerInt = new Scanner(System.in);


        FileService<Category> fileService = new FileService<>();
        CategoryDao categoryDao = new CategoryDao(fileService);
        CategoryService categoryService = new CategoryService(categoryDao);

        FileService<Product> productFileService = new FileService<>();
        ProductDao productDao = new ProductDao(productFileService);
        ProductService productService = new ProductService(productDao);

        int stepCode = 10;
        while (stepCode != 0) {

            System.out.println("1. Add Category 2. Add Product");
            stepCode = scannerInt.nextInt();

            switch (stepCode) {
                case 1 -> {
                    Category category = new Category();
                    System.out.println("enter category name: ");
                    category.setName(scannerStr.nextLine());
                    System.out.println("parent id?");
                    String s = scannerStr.nextLine();
                    category.setParentId(s.length() > 1 ? UUID.fromString(s) : null);
                    categoryService.add(category);
                }

                case 2 -> {
                    Product product = new Product();
                    System.out.println("enter product name");
                    product.setName(scannerStr.nextLine());

                    System.out.println("enter price");
                    product.setPrice(scannerInt.nextDouble());

                    System.out.println("enter CategoryId");
                    product.setCategoryId(UUID.fromString(scannerStr.nextLine()));
                    //////////
                    System.out.println("Enter photo address");
                    product.setAddressOfPhoto(scannerStr.nextLine());
                    //////////
                    productService.create(product);
                }
            }
        }



    }
}
