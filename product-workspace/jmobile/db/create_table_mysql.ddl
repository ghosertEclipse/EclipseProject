DROP DATABASE jmobile;

CREATE DATABASE jmobile;

USE jmobile;

CREATE TABLE orders
(
    id int unsigned auto_increment not null,
    productcode varchar(10) not null,
    payer varchar(20) not null,
    sender varchar(15) not null,
    receiver varchar(20) not null,
    linkid varchar(40),
    param varchar(160),
    itemid int unsigned ,
    paySuccess varchar(2) not null,
    pushSuccess varchar(2) not null,
    optime datetime not null,
    CONSTRAINT ORDERS_ID_PK PRIMARY KEY(id)
);

CREATE TABLE items
(
    id int unsigned auto_increment not null,
    productid int unsigned not null,
    name varchar(50),
    author varchar(20),
    url varchar(512),
    uploader varchar(20),
    weekpaytimes int not null,
    paytimes int not null,
    isValid varchar(2) not null,
    uploadtime datetime not null,
    itemType varchar(2) not null,
    CONSTRAINT ITEMS_ID_PK PRIMARY KEY(id)
);

CREATE TABLE products
(
   id int unsigned auto_increment not null,
   parentid int unsigned,
   name varchar(10) not null,
   CONSTRAINT PRODUCTS_ID_PK PRIMARY KEY(id)
);

ALTER TABLE orders
  ADD CONSTRAINT fk_orders_items FOREIGN KEY (itemid)
        REFERENCES items (id);
        
ALTER TABLE items
  ADD CONSTRAINT fk_items_products FOREIGN KEY (productid)
        REFERENCES products (id);
        
ALTER TABLE products
  ADD CONSTRAINT fk_products_products FOREIGN KEY (parentid)
        REFERENCES products (id);
        
CREATE TABLE userRecorders
(
    id int unsigned auto_increment not null,
    loginTime datetime not null,
    mobile varchar(20),
    ip varchar(15) not null,
    CONSTRAINT USERRECORDERS_ID_PK PRIMARY KEY(id)
);
