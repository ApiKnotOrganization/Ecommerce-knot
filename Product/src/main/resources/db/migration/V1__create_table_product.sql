CREATE DATABASE IF NOT EXISTS KnotOrganizationDatabaseProduct;
USE KnotOrganizationDatabaseProduct;

-- Gender table
CREATE TABLE `gender` (
    id_gender TINYINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(40) NOT NULL
) ENGINE = InnoDB;

-- Images table
CREATE TABLE `images` (
    id_image SMALLINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    filename_image BLOB
) ENGINE = InnoDB;

-- Product table
CREATE TABLE `product` (
    id_product BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_gender TINYINT UNSIGNED,
    id_image SMALLINT UNSIGNED,
    name VARCHAR(40) UNIQUE NOT NULL,
    brand VARCHAR(50),
    stock INT UNSIGNED,
    price DECIMAL(10,2) UNSIGNED NOT NULL,
    description VARCHAR(2000),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_gender FOREIGN KEY (id_gender) REFERENCES gender(id_gender),
    CONSTRAINT FK_images FOREIGN KEY (id_image) REFERENCES images(id_image)
) ENGINE = InnoDB;

-- Size table
CREATE TABLE `size` (
    id_size TINYINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(5) NOT NULL
) ENGINE = InnoDB;

-- Product-Size relation
CREATE TABLE `product_size` (
    id_product BIGINT UNSIGNED,
    id_size TINYINT UNSIGNED,
    quantity_available MEDIUMINT UNSIGNED,
    PRIMARY KEY (id_product, id_size),
    CONSTRAINT FK_size FOREIGN KEY (id_size) REFERENCES size(id_size) ON DELETE CASCADE,
    CONSTRAINT FK_product FOREIGN KEY (id_product) REFERENCES product(id_product) ON DELETE CASCADE
) ENGINE = InnoDB;

-- Category table
CREATE TABLE `category` (
    id_category SMALLINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    description VARCHAR(1000),
    category_image BLOB
) ENGINE = InnoDB;

-- Product-Category relation
CREATE TABLE `product_category` (
    id_product BIGINT UNSIGNED,
    id_category SMALLINT UNSIGNED,
    PRIMARY KEY (id_product, id_category),
    CONSTRAINT FK_product_category_product FOREIGN KEY (id_product) REFERENCES product(id_product) ON DELETE CASCADE,
    CONSTRAINT FK_product_category_category FOREIGN KEY (id_category) REFERENCES category(id_category) ON DELETE CASCADE
) ENGINE = InnoDB;
