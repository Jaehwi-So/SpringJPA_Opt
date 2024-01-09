package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
//상속 관계 테이블의 전략을 지정해주어야함
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)   //한 테이블에 다 때려박는 Strategy 사용
@DiscriminatorColumn(name = "dtype")    //싱글 테이블 구분방법 지정
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

//    @Version
//    private Long version;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // == 비즈니스 로직

    /**
     * 제고 증가
     * @param quantity
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /**
     * 제고 감소
     * @param quantity
     */
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){
            throw new NotEnoughStockException("Need More Stock");
        }
        this.stockQuantity = restStock;
    }
}
