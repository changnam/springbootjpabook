CREATE TABLE `book` (
  `id` varchar(255) NOT NULL,
  `category` varchar(255) NOT NULL,
  `create_at` datetime(6) NOT NULL,
  `name` varchar(255) NOT NULL,
  `sell_count` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;