CREATE TABLE mypage_user (
mypage_user_id INT PRIMARY KEY,
user_name VARCHAR(50) NOT NULL,
email VARCHAR(50) NOT NULL UNIQUE,
about_me TEXT,
address VARCHAR(255),
phone_number VARCHAR(20),
FOREIGN KEY (mypage_user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE mypage_user_cart_list (
    cart_list_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_price INT NOT NULL,
    product_quantity INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES mypage_user(mypage_user_id) ON DELETE CASCADE
);

CREATE TABLE my_page_user_order_history (
    order_history_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_price INT NOT NULL,
    product_quantity INT NOT NULL,
    order_date_time DATETIME NOT NULL,
    order_status VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES mypage_user(mypage_user_id) ON DELETE CASCADE
);