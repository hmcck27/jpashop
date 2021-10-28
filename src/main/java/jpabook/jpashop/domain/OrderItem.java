package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.mapping.ToOne;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@BatchSize(size = 1000)
@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    private int orderPrice;
    private int count;

    //==생성 메소드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {

        /**
         * 가격을 item으로 안하는 이유는 쿠폰이나 다른 것들이 적용되면 단순한 item의 가격대로 가지 않기 때문이다.
         * 따라서 orderPrice를 따로 가져간다.
         */

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    //==비즈니스 로직==//
    public void cancel() {
        this.getItem().addStockQuantity(this.count);
    }

    //==조회 로직==//
    /**
     * 주문 상품 전체 조회 로직
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
