package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        /**
         * 여기서 id가 없으면 새로운 아이템이니까, 당연히 id가 없고, 이때는 persis
         * id가 있으면 당연히 업데이트니까 merge 사용.
         */
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

   public List<Item> findAll() {
       return em.createQuery("select i from Item i", Item.class)
               .getResultList();
   }

}
