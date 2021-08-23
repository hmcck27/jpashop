package jpabook.jpashop.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UpdateItemDto {

    private int price;

    private String name;

    private int stockQuantity;

    @Builder
    public UpdateItemDto(int price, String name, int stockQuantity) {
        this.price = price;
        this.name = name;
        this.stockQuantity = stockQuantity;
    }

    public static UpdateItemDto createUpdateItemDto(int price, String name, int stockQuantity) {
        UpdateItemDto updateItemDto = new UpdateItemDto()
                .builder()
                .price(price)
                .name(name)
                .stockQuantity(stockQuantity)
                .build();

        return updateItemDto;

    }

}
