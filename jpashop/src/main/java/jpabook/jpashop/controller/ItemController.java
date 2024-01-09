package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form){

        Book book = new Book();
        //Setter 대신 Create하는 메서드를 엔티티에 만드는 것이 더 좋긴 함!!
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor((form.getAuthor()));
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/";

    }

    @GetMapping("/items")
    public String list(Model model){
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }


    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){
        Book item = (Book) itemService.findOne(itemId);
        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setIsbn(item.getIsbn());
        form.setAuthor(item.getAuthor());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, BookForm form) {

        //book은 새로운 객체지만 데이터베이스에서 얻어온 준영속 엔티티임, 기존의 식별자를 가지면 준영속 엔티티로 볼 수 있음
        //준영속 엔티티는 JPA가 관리하고 있지 않기 때문에 값을 변경해도 반영되지 않음

          //어설프게 Entity를 컨트롤러에서 사용하지 않는 것이 좋음. 클래스를 사용하고 싶으면 DTO를 사용하는 것이 좋음
//        Book book = new Book();
//        book.setId(form.getId());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor((form.getAuthor()));
//        book.setIsbn(form.getIsbn());

          //Merge를 이용한 방법은 위험하여 사용하지 않는 것이 좋음
//        itemService.saveItem(book);

        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());

        return "redirect:/items";
    }
}
