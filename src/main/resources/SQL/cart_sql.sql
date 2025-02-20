create table cart(
    cart_id bigint primary key auto_increment,
    user_id bigint unique,
    created_at timestamp default current_timestamp, -- 장바구니 생성 시간
    updated_at timestamp default  current_timestamp on update current_timestamp -- 장바구니 update 시간

) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

create table cart_item(
    cart_item_id bigint primary key auto_increment,
    cart_id bigint ,
    product_id bigint,
    quantity int,
    created_at timestamp default current_timestamp, -- 상품 추가 시간
    updated_at timestamp default  current_timestamp on update current_timestamp -- 삼품 업데이트 시간
)