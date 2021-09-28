package jpabook.jpashop.controller;

import jpabook.jpashop.Dto.UpdateItemDto;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) {

        /**
         * 여기서도 당연히 객체 생성을 set방식으로 하면 안좋다.
         * order 처럼 createBook(param1, param2,....) 이런식으로 설계하는게 가장 좋다.
         *
         */

        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.save(book);
        return "redirect:/";
    }

    @GetMapping(value = "/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }






    @GetMapping(value = "items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId);

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());
        form.setStockQuantity(item.getStockQuantity());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping(value = "items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") BookForm form, @PathVariable Long itemId) {

        /**
         * 고민되는 사항은 우리가 form을 전략적으로 웹 계층에서만 사용하자라고 정의했다.
         * 따라서 폼 자체를 서비스 계층으로 넘기는 작업은 지저분하고 귀찮다. 값 세팅을 서비스에서 해줘야 한다는 의미니까.
         * 그래서 처음에는 컨트롤러에서 엔티티를 생성했다. 근데 컨트롤러에서도 가능하다면 엔티티를 생성하지 말아야 한다.
         * 어설프게 객체를 생성해서 서비스로 넘기기 보다 필요한 데이터만 넘기는게 좋다.
         */

//        Book book = new Book();
//
//        book.setId(form.getId());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());
//
//        itemService.updateItem(book.getId(), book);

        /**
         * 근데 여기서 너무 넘겨야 하는 데이터가 많다는 생각이 든다면, 중간 매핑 DTO를 만들자 !
         *
         */

//        itemService.updateItem(itemId, form.getPrice(), form.getName(), form.getStockQuantity());

        UpdateItemDto updateItemDto = UpdateItemDto.createUpdateItemDto(form.getPrice(), form.getName(), form.getStockQuantity());
        itemService.updateItem(itemId, updateItemDto);

        return "redirect:/items";


    }
}

