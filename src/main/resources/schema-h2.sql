-- MySQL Workbench Forward Engineering
-- -----------------------------------------------------
-- Schema gameshop
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Table gameshop.authorities
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS authorities (
                                           id INT NOT NULL,
                                           authority VARCHAR(50) NOT NULL,
                                           PRIMARY KEY (id)
                                       );
-- -----------------------------------------------------
-- Table gameshop.users
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
                                    id INT NOT NULL AUTO_INCREMENT,
                                    enabled BOOLEAN NOT NULL,
                                    password VARCHAR(100) NOT NULL,
                                    username VARCHAR(50) NOT NULL,
                                    email VARCHAR(50) NOT NULL,
                                    first_name VARCHAR(100) NOT NULL,
                                    last_name VARCHAR(50) NOT NULL,
                                    authority_id INT NULL DEFAULT NULL,
                                    PRIMARY KEY (id),
                                    FOREIGN KEY (authority_id)
                                    REFERENCES authorities (id)
                                 );
-- -----------------------------------------------------
-- Table gameshop.authors
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS authors (
                                    id INT NOT NULL,
                                    first_name VARCHAR(255) NULL DEFAULT NULL,
                                    last_name VARCHAR(255) NULL DEFAULT NULL,
                                    nationality VARCHAR(255) NULL DEFAULT NULL,
                                    PRIMARY KEY (id)
                                   );
-- -----------------------------------------------------
-- Table gameshop.baskets
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS baskets (
                                    id INT NOT NULL,
                                    cost DOUBLE NULL DEFAULT NULL,
                                    sent BOOLEAN NULL DEFAULT NULL,
                                    user_id INT NULL DEFAULT NULL,
                                    PRIMARY KEY (id),
                                    FOREIGN KEY (user_id)
                                    REFERENCES users (id)
                                   );
-- -----------------------------------------------------
-- Table gameshop.games
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS games (
                                    id INT NOT NULL,
                                    is_deleted BOOLEAN NULL DEFAULT NULL,
                                    name VARCHAR(255) NOT NULL,
                                    price DOUBLE NOT NULL,
                                    series_name VARCHAR(255) NULL DEFAULT NULL,
                                    volume INT NULL DEFAULT NULL,
                                    author_id INT NULL DEFAULT NULL,
                                    year_date INT NULL DEFAULT NULL,
                                    PRIMARY KEY (id),
                                    FOREIGN KEY (author_id)
                                    REFERENCES authors (id)
                                 );
-- -----------------------------------------------------
-- Table gameshop.game_baskets
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS game_baskets (
                                            id INT NOT NULL,
                                            copies INT NULL DEFAULT NULL,
                                            price DOUBLE NULL DEFAULT NULL,
                                            basket_id INT NULL DEFAULT NULL,
                                            game_id INT NULL DEFAULT NULL,
                                            PRIMARY KEY (id),
                                            FOREIGN KEY (basket_id)
                                            REFERENCES baskets(id),
                                            FOREIGN KEY (game_id)
                                            REFERENCES games (id)
                                        );
-- -----------------------------------------------------
-- Table gameshop.categories
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS categories (
                                        id INT NOT NULL,
                                        name VARCHAR(255) NULL DEFAULT NULL,
                                        PRIMARY KEY (id)
                                      );
-- -----------------------------------------------------
-- Table gameshop.games_game_categories
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS games_game_categories (
                                                    game_id INT NOT NULL,
                                                    game_categories_id INT NOT NULL,
                                                    FOREIGN KEY (game_id)
                                                    REFERENCES games (id),
                                                    FOREIGN KEY (game_categories_id)
                                                    REFERENCES categories (id)
                                                 );
-- -----------------------------------------------------
-- Table gameshop.coupons
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS coupons (
                                        id INT NOT NULL,
                                        discount DOUBLE NULL DEFAULT NULL,
                                        user_id INT NULL DEFAULT NULL,
                                        PRIMARY KEY (id),
                                        FOREIGN KEY (user_id)
                                        REFERENCES users (id)
                                   );

