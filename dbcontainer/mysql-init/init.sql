-- =======================================================
-- CREATE DATABASES
-- =======================================================
CREATE DATABASE IF NOT EXISTS KnotOrganizationDatabaseProduct;
CREATE DATABASE IF NOT EXISTS KnotOrganizationDatabaseUser;
CREATE DATABASE IF NOT EXISTS KnotOrganizationDatabaseOrder;
CREATE DATABASE IF NOT EXISTS KnotOrganizationDatabasePayment;

-- =======================================================
-- PRODUCT MICROSERVICE
-- =======================================================
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

-- =======================================================
-- PAYMENT MICROSERVICE
-- =======================================================
USE KnotOrganizationDatabasePayment;

CREATE TABLE `payment` (
    id_payment BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    payment_method ENUM('CASH', 'CREDIT_CARD', 'DEBIT_CARD', 'PAYPAL') NOT NULL,
    total_amount DECIMAL(10,2) UNSIGNED NOT NULL
) ENGINE = InnoDB;

-- =======================================================
-- ORDER MICROSERVICE
-- =======================================================
USE KnotOrganizationDatabaseOrder;

CREATE TABLE `order_item` (
    id_order_item BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    original_price DECIMAL(10,2) UNSIGNED NOT NULL,
    sale_price DECIMAL(10,2) UNSIGNED,
    quantity MEDIUMINT UNSIGNED NOT NULL
) ENGINE = InnoDB;

CREATE TABLE `product_order_item` (
    id_order_item BIGINT UNSIGNED,
    id_product BIGINT UNSIGNED,
    PRIMARY KEY (id_order_item, id_product),
    CONSTRAINT FK_item_order FOREIGN KEY (id_order_item) REFERENCES order_item(id_order_item) ON DELETE CASCADE,
    CONSTRAINT FK_product FOREIGN KEY (id_product) REFERENCES KnotOrganizationDatabaseProduct.product(id_product) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE `order` (
    id_order BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    status ENUM('PENDING', 'PAID', 'CANCELLED') NOT NULL,
    total_amount DECIMAL(10,2) UNSIGNED NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;

-- =======================================================
-- USER MICROSERVICE
-- =======================================================
USE KnotOrganizationDatabaseUser;

CREATE TABLE `app_user` (
    id_user BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_payment BIGINT UNSIGNED,
    phone_country_code VARCHAR(5),
    phone_number VARCHAR(15),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) UNIQUE NOT NULL,
    role ENUM('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATION') NOT NULL DEFAULT 'ROLE_USER',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT FK_payment FOREIGN KEY (id_payment) REFERENCES KnotOrganizationDatabasePayment.payment(id_payment) ON DELETE SET NULL
) ENGINE = InnoDB;

CREATE TABLE `user_address` (
    id_address BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_user BIGINT UNSIGNED,
    line_address_1 VARCHAR(255) NOT NULL,
    line_address_2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    is_primary TINYINT(1) DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT FK_user_address FOREIGN KEY (id_user) REFERENCES app_user(id_user) ON DELETE CASCADE
) ENGINE = InnoDB;