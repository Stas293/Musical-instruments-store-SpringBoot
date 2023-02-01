CREATE DATABASE IF NOT EXISTS Musical_instruments_store DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE Musical_instruments_store;

CREATE TABLE `role` (
                        `role_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                        `code` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
                        `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
                        PRIMARY KEY (`role_id`),
                        UNIQUE KEY `role_idx1` (`name`),
                        UNIQUE KEY `role_idx2` (`code`)
) ENGINE=InnoDB;

CREATE TABLE `user_list` (
                             `user_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                             `login` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
                             `first_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
                             `last_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
                             `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
                             `phone` char(13) COLLATE utf8_unicode_ci NOT NULL,
                             `password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
                             `enabled` tinyint(1) NOT NULL DEFAULT '1',
                             `date_created` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                             `date_modified` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             PRIMARY KEY (`user_id`),
                             UNIQUE KEY `user_list_idx1` (`login`),
                             UNIQUE KEY `user_list_idx2` (`email`),
                             UNIQUE KEY `user_list_idx3` (`phone`)
) ENGINE=InnoDB;

CREATE TABLE `user_role` (
                             `user_id` bigint unsigned NOT NULL,
                             `role_id` bigint unsigned NOT NULL,
                             PRIMARY KEY (`user_id`,`role_id`),
                             KEY `fk_user_role_role` (`role_id`),
                             CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`),
                             CONSTRAINT `fk_user_role_user_list` FOREIGN KEY (`user_id`) REFERENCES `user_list` (`user_id`)
) ENGINE=InnoDB;

CREATE TABLE `status` (
                          `status_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                          `code` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
                          `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
                          `closed` tinyint(1) NOT NULL DEFAULT '1',
                          PRIMARY KEY (`status_id`),
                          UNIQUE KEY `status_idx1` (`name`),
                          UNIQUE KEY `status_idx2` (`code`)
) ENGINE=InnoDB;

CREATE TABLE `next_status` (
                               `status_id` bigint unsigned NOT NULL,
                               `next_status_id` bigint unsigned NOT NULL,
                               KEY `fk_next_status_status` (`status_id`),
                               KEY `fk_next_status_status1` (`next_status_id`),
                               CONSTRAINT `fk_next_status_status` FOREIGN KEY (`status_id`) REFERENCES `status` (`status_id`),
                               CONSTRAINT `fk_next_status_status1` FOREIGN KEY (`next_status_id`) REFERENCES `status` (`status_id`)
) ENGINE=InnoDB;

CREATE TABLE `instrument_list` (
                                   `instrument_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                                   `date_created` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                   `date_updated` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                   `description` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
                                   `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
                                   `status_id` bigint unsigned NOT NULL,
                                   `price` decimal(17,2) NOT NULL DEFAULT '0.00',
                                   PRIMARY KEY (`instrument_id`),
                                   UNIQUE KEY `instrument_list_idx1` (`title`),
                                   UNIQUE KEY `instrument_list_idx2` (`description`),
                                   KEY `fk_instrument_list_status` (`status_id`),
                                   CONSTRAINT `fk_instrument_list_status` FOREIGN KEY (`status_id`) REFERENCES `status` (`status_id`)
) ENGINE=InnoDB;

CREATE TABLE `order_list` (
                              `order_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                              `date_created` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                              `user_id` bigint unsigned NOT NULL,
                              `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
                              `status_id` bigint unsigned NOT NULL,
                              `closed` tinyint(1) NOT NULL DEFAULT '0',
                              PRIMARY KEY (`order_id`),
                              KEY `fk_order_list_user_list` (`user_id`),
                              KEY `fk_order_list_status` (`status_id`),
                              CONSTRAINT `fk_order_list_status` FOREIGN KEY (`status_id`) REFERENCES `status` (`status_id`),
                              CONSTRAINT `fk_order_list_user_list` FOREIGN KEY (`user_id`) REFERENCES `user_list` (`user_id`)
) ENGINE=InnoDB;

CREATE TABLE `instrument_order` (
                                    `instrument_id` bigint unsigned NOT NULL,
                                    `order_id` bigint unsigned NOT NULL,
                                    `price` decimal(17,2) NOT NULL DEFAULT '0.00',
                                    `quantity` tinyint NOT NULL DEFAULT '0',
                                    PRIMARY KEY (`instrument_id`,`order_id`),
                                    KEY `fk_instrument_order_order_list` (`order_id`),
                                    CONSTRAINT `fk_instrument_order_instrument_list` FOREIGN KEY (`instrument_id`) REFERENCES `instrument_list` (`instrument_id`),
                                    CONSTRAINT `fk_instrument_order_order_list` FOREIGN KEY (`order_id`) REFERENCES `order_list` (`order_id`)
) ENGINE=InnoDB;

CREATE TABLE `order_history` (
                                 `history_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                                 `date_created` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                 `user_id` bigint unsigned NOT NULL,
                                 `total_sum` decimal(17,2) NOT NULL DEFAULT '0.00',
                                 `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
                                 `status_id` bigint unsigned NOT NULL,
                                 PRIMARY KEY (`history_id`),
                                 KEY `fk_order_history_status` (`status_id`),
                                 KEY `fk_order_history_user` (`user_id`),
                                 CONSTRAINT `fk_order_history_status` FOREIGN KEY (`status_id`) REFERENCES `status` (`status_id`),
                                 CONSTRAINT `fk_order_history_user` FOREIGN KEY (`user_id`) REFERENCES `user_list` (`user_id`)
) ENGINE=InnoDB;

INSERT INTO `Musical_instruments_store`.`role`(`code`, `name`)
VALUES ("ROLE_USER", "user");

INSERT INTO `Musical_instruments_store`.`role`(`code`, `name`)
VALUES ("ROLE_SELLER", "seller");

INSERT INTO `Musical_instruments_store`.`role`(`code`, `name`)
VALUES ("ROLE_ADMIN", "admin");

INSERT INTO `Musical_instruments_store`.`status`(`code`,`name`,`closed`)
VALUES("NOT_AVAILABLE","Not available",1);

INSERT INTO `Musical_instruments_store`.`status`(`code`,`name`,`closed`)
VALUES("NOT_PROCESSED","Not processed",0);

INSERT INTO `Musical_instruments_store`.`status`(`code`,`name`,`closed`)
VALUES("AVAILABLE","Available",0);

INSERT INTO `Musical_instruments_store`.`status`(`code`,`name`,`closed`)
VALUES("ORDER_PROCESSING","Order processing",0);

INSERT INTO `Musical_instruments_store`.`status`(`code`,`name`,`closed`)
VALUES("ARRIVED","Arrived",0);

INSERT INTO `Musical_instruments_store`.`status`(`code`,`name`,`closed`)
VALUES("CANCELED","Canceled",0);

INSERT INTO `Musical_instruments_store`.`next_status`(`status_id`,`next_status_id`)
VALUES(1,2);

INSERT INTO `Musical_instruments_store`.`next_status`(`status_id`,`next_status_id`)
VALUES(2,3);

INSERT INTO `Musical_instruments_store`.`next_status`(`status_id`,`next_status_id`)
VALUES(3,4);

INSERT INTO `Musical_instruments_store`.`next_status`(`status_id`,`next_status_id`)
VALUES(4,5);







