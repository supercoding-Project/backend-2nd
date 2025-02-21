package com.github.secondproject.mypage.dto;

import com.github.secondproject.order.entity.OrderEntity;
import lombok.*;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MyPageOrderHistoryDto {
    private Optional<OrderEntity> orderHistory;
}
