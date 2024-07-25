package uz.pdp.g42.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.g42.bot.service.InlineMarkupService;
import uz.pdp.g42.common.dao.CategoryDao;
import uz.pdp.g42.common.dao.OrderDao;
import uz.pdp.g42.common.dao.ProductDao;
import uz.pdp.g42.common.model.Category;
import uz.pdp.g42.common.model.Order;
import uz.pdp.g42.common.model.Product;
import uz.pdp.g42.common.service.CategoryService;
import uz.pdp.g42.common.service.FileService;
import uz.pdp.g42.common.service.OrderService;
import uz.pdp.g42.common.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UzumBot extends TelegramLongPollingBot {
    private static final String USERNAME = "ali_uzum_market_bot";
    private static final String BOT_TOKEN = "7080826136:AAGULarh4lFEbhR1tQvIB5MUmPGZZC7aJWA";

    FileService<Category> fileService = new FileService<>();
    CategoryDao categoryDao = new CategoryDao(fileService);
    CategoryService categoryService = new CategoryService(categoryDao);
    InlineMarkupService<Category> inlineMarkupService = new InlineMarkupService<>(categoryService);


    FileService<Product> productFileService = new FileService<>();
    ProductDao productDao = new ProductDao(productFileService);
    ProductService productService = new ProductService(productDao);
    InlineMarkupService<Product> productInlineMarkupService = new InlineMarkupService<>(productService);

    FileService<Order> orderFileService = new FileService<>();
    OrderDao orderDao = new OrderDao(orderFileService);
    OrderService orderService = new OrderService(orderDao);
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            String text = message.getText();
            try {
                switch (text) {
                    case "/start" -> executeJob(message.getChatId(), "Xush kelibsiz!",
                            replyKeyboard(List.of("order", "basket", "history"), 2));

                    case "order" ->  executeJob(message.getChatId(), "choose category",
                            inlineMarkupService.mainInlineKeyboardMarkup(3));

                    case "basket" -> {
                        SendMessage sendMessage = new SendMessage();
                        List<Order> myOrders = orderService.getMyOrders(message.getChatId());
                        StringBuilder sb = new StringBuilder();
                        sb.append("\tMy Basket \n");
                        double sum = 0;
                        for (Order myOrder : myOrders) {
                            sb.append(myOrder.getProduct().getName() + "\t$" + myOrder.getProduct().getPrice() + "\n");
                            sum += myOrder.getProduct().getPrice();
                        }
                        sb.append("Total : $" + sum);
                        sendMessage.setChatId(update.getMessage().getChatId());
                        sendMessage.setText(sb.toString());
                        execute(sendMessage);
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }else if (update.hasCallbackQuery()) {
            try {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                String categoryId = callbackQuery.getData();

                /////My Logic
                Optional<Product> productOptional = productService.getProductById(UUID.fromString(categoryId));
                if (productOptional.isPresent()){
                    Product product = productOptional.get();
                    String addressOfPhoto = product.getAddressOfPhoto();
                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setPhoto(new InputFile(addressOfPhoto));
                    sendPhoto.setCaption("name : " + product.getName() + "\n" + "price : $" + product.getPrice());
                    sendPhoto.setChatId(callbackQuery.getMessage().getChatId());

                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rows = new ArrayList<>();

                    List<InlineKeyboardButton> row = new ArrayList<>();
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText("Add Basket");
                    button.setCallbackData(product.getPurchaseId().toString());
                    row.add(button);
                    rows.add(row);
                    inlineKeyboardMarkup.setKeyboard(rows);
                    sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
                    execute(sendPhoto);
                    return;
                }

                Optional<Product> productByPurchaseId = productService.getProductByPurchaseId(UUID.fromString(categoryId));
                if (productByPurchaseId.isPresent()){
                    Product product = productByPurchaseId.get();
                    Long chatId = callbackQuery.getMessage().getChatId();
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(chatId);
                    if (!orderService.hasProduct(chatId, product.getId())){
                    orderService.add(new Order(chatId, product));
                    sendMessage.setText(product.getName() + " has been added to your basket ✅");
                    }else{
                        sendMessage.setText("Sorry, You have already bought this product ❌");
                    }
                    execute(sendMessage);
                    return;
                }

                /////
                boolean hasChildCategory = categoryService.hasChildCategory(UUID.fromString(categoryId));
                if (hasChildCategory) {
                    executeJob(callbackQuery.getMessage().getChatId(),
                            "choose child category",
                            inlineMarkupService.subInlineKeyboardMarkup(UUID.fromString(categoryId), 3)
                    );
                } else {
                    executeJob(callbackQuery.getMessage().getChatId(),
                            "choose product",
                            productInlineMarkupService.subInlineKeyboardMarkup(UUID.fromString(categoryId), 4)
                    );
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    private void executeJob(Long chatId, String text, ReplyKeyboard r) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage(chatId.toString(), text);
        sendMessage.setReplyMarkup(r);
        execute(sendMessage);
    }

    private ReplyKeyboard replyKeyboard(List<String> menus, int col) {
        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        replyKeyboard.setKeyboard(rows);

        KeyboardRow row = new KeyboardRow();
        for (int i = 1; i <= menus.size(); i++) {
            row.add(new KeyboardButton(menus.get(i-1)));
            if (i % col == 0) {
                rows.add(row);
                row = new KeyboardRow();
            }
        }

        if (!row.isEmpty()) {
            rows.add(row);
        }

        replyKeyboard.setResizeKeyboard(true);
        return replyKeyboard;
    }
}