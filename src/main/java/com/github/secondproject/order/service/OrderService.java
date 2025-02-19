package com.github.secondproject.order.service;

import com.github.secondproject.order.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {

    public OrderEntity createOrder(Integer userId, List<Integer> cartItemIds) {

        return null;

    }
}
