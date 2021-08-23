package jpabook.jpashop.service;

import jpabook.jpashop.Dto.UpdateItemDto;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void save(Item item) {
        itemRepository.save(item);
    }

//    @Transactional
//    public void updateItem(Long itemId, Book param) {
//
//        /**
//         * 변경감지를 이용한 udpate
//         * findItem은 리포지토리에서 갖고왔기 때문에 jpa가 관리하는 영속성 객체이다.
//         * 여기서 값을 set으로 수정한다면, 변경감지 dirty checking에 의해서 디비에도 값이 변경된다.
//         * 이게 트랜잭션이 걸려있으니까, 이 함수가 끝나면 커밋을 날리게 된다. 그러면 jpa가 변경감지로 업데이트 쿼리를 알아서 날린다.
//         */
//        Item findItem = itemRepository.findOne(itemId);
//
//        /**
//         * 당연히 여기서도 set을 막 호출하는 건 지양해야 한다.
//         * 예를 들어서 개념적으로 바뀌는 친구들을 따로 지정하고
//         * findItem.changePrice()이런식으로 의미 있는 메소드를 호출해서 값을 바꿔야 한다.
//         * 변경할때도 당연히 엔티티 레벨에서 하자. 추적이 가능하게 해야 한다.
//         */
//        findItem.setPrice(param.getPrice());
//        findItem.setName(param.getName());
//        findItem.setStockQuantity(param.getStockQuantity());
//    }

//    @Transactional
//    public void updateItem(Long itemId, int price, String name, int stockQuantity) {
//
//        /**
//         * 변경감지를 이용한 udpate
//         * findItem은 리포지토리에서 갖고왔기 때문에 jpa가 관리하는 영속성 객체이다.
//         * 여기서 값을 set으로 수정한다면, 변경감지 dirty checking에 의해서 디비에도 값이 변경된다.
//         * 이게 트랜잭션이 걸려있으니까, 이 함수가 끝나면 커밋을 날리게 된다. 그러면 jpa가 변경감지로 업데이트 쿼리를 알아서 날린다.
//         */
//        Item findItem = itemRepository.findOne(itemId);
//
//        /**
//         * 당연히 여기서도 set을 막 호출하는 건 지양해야 한다.
//         * 예를 들어서 개념적으로 바뀌는 친구들을 따로 지정하고
//         * findItem.changePrice()이런식으로 의미 있는 메소드를 호출해서 값을 바꿔야 한다.
//         * 변경할때도 당연히 엔티티 레벨에서 하자. 추적이 가능하게 해야 한다.
//         */
//        findItem.setPrice(price);
//        findItem.setName(name);
//        findItem.setStockQuantity(stockQuantity);
//    }

    @Transactional
    public void updateItem(Long itemId, UpdateItemDto updateItemDto) {

        /**
         * 변경감지를 이용한 udpate
         * findItem은 리포지토리에서 갖고왔기 때문에 jpa가 관리하는 영속성 객체이다.
         * 여기서 값을 set으로 수정한다면, 변경감지 dirty checking에 의해서 디비에도 값이 변경된다.
         * 이게 트랜잭션이 걸려있으니까, 이 함수가 끝나면 커밋을 날리게 된다. 그러면 jpa가 변경감지로 업데이트 쿼리를 알아서 날린다.
         */
        Item findItem = itemRepository.findOne(itemId);

        /**
         * 당연히 여기서도 set을 막 호출하는 건 지양해야 한다.
         * 예를 들어서 개념적으로 바뀌는 친구들을 따로 지정하고
         * findItem.changePrice()이런식으로 의미 있는 메소드를 호출해서 값을 바꿔야 한다.
         * 변경할때도 당연히 엔티티 레벨에서 하자. 추적이 가능하게 해야 한다.
         */
        findItem.setPrice(updateItemDto.getPrice());
        findItem.setName(updateItemDto.getName());
        findItem.setStockQuantity(updateItemDto.getStockQuantity());
    }

    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

}
