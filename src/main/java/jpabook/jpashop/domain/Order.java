package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    /**
     * protected로 적어놓으면, 생성자를 사용할 수 없게 되고, 다 로직적으로 접근해야만 한다.
     * 항상 코드는 제약이 많이야지 유지보수에 유리하다.
     */

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    //==연관 관계 편의 메소드==
    /*
        양방향 연관 관계에서는 서로가 서로를 갖고 있다.
        주문에서 회원을 추가하고 동시에 회원이 갖고 있는 주문 리스트에 현재 주문을 추가해야되는데, 이러면 두번의 set을 호출해야 되니까
        귀찮다.. 따라서 미리 주문의 setMember에다가 오버라이딩해서 멤버의 주문 리스트에 주문을 추가하는 형태의 메소드를 만든다.
        이러면 두번 챙길 필요가 없으니까

        이 메소드의 위치는 핵심적인 컨트롤의 주권을 가진 객체 쪽에 넣는게 좋다.
     */
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 로직==//

    /**
     * 주문은 하나만 생성하면 안된다. 주문을 생성하면, 그에 따르는 배송과 orderItem도
     * 만들어야 한다. 이를 단순히 order를 생성하고 하는게 아니라
     * createOrder method를 통해서 다 묶어서 생성할 수 있게 한다.
     * 앞으로 생서하는 로직을 수정하게 되면 여기만 보면 된다.
     * 이게 도메인에 생성 로직을 넣는 이유.
     */

    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//

    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다.");
        }
        this.setOrderStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : this.orderItems) {
            orderItem.cancel();
        }
    }

    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = this.orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
        return totalPrice;
    }

}
