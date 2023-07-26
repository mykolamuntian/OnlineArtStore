package com.example.onlineartstore.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
@Table(name = "Bets")
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NonNull
    private LocalDateTime createdDate;
    @NonNull
    private BigDecimal amountMoney;
    @NonNull
    private Boolean active;

    @ManyToOne
    private Auction auction;

    @ManyToOne
    private User user;

    @PrePersist
    // - выполнить соответствующий метод при создании Entity и при каждом ее обновлении. Теперь нет нужды отдельно устанавливать эти даты
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

}
