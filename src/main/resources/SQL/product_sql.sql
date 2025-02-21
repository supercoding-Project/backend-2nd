CREATE TABLE `product` (
`product_id` bigint NOT NULL AUTO_INCREMENT,
`title` varchar(255) NOT NULL,
`author` varchar(255) NOT NULL,
`publisher` varchar(255) NOT NULL,
`original_price` double NOT NULL,
`sale_price` double NOT NULL,
`description` varchar(1000) DEFAULT NULL,
`stock_quantity` int NOT NULL,
`publish_date` date NOT NULL,
`started_at` date NOT NULL,
`terminated_at` date NOT NULL,
`image_url` varchar(255) NOT NULL,
`product_status` varchar(20) NOT NULL,
`user_id` bigint NOT NULL,
`created_at` datetime DEFAULT NULL,
PRIMARY KEY (`product_id`)
)