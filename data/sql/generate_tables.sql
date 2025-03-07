create table CONFIG
(
    ID                             BIGINT auto_increment
        primary key,
    PERMIT_TABLE_ASSIGNMENT_TO_ALL BOOLEAN not null
);

create table CUSTOMER
(
    CUSTOMER_ID       BIGINT auto_increment
        primary key,
    ADDRESS           CHARACTER VARYING(255),
    CREATED_DATE      TIMESTAMP,
    EMAIL             CHARACTER VARYING(255),
    FIRST_NAME        CHARACTER VARYING(255),
    LAST_NAME         CHARACTER VARYING(255),
    LOGIN             CHARACTER VARYING(255),
    PASSWORD          CHARACTER VARYING(255),
    PHONE_NUMBER      CHARACTER VARYING(255),
    PROFILE_COMPLETED BOOLEAN not null,
    ROLE              ENUM ('CUSTOMER', 'CANCELED', 'DELIVERED', 'PENDING')
);

create table CUSTOMER_ALLERGIES
(
    CUSTOMER_CUSTOMER_ID BIGINT not null,
    ALLERGIES            ENUM ('EGGS', 'BOISSON_CHAUDE', 'SULFITES', 'FISH', 'BOISSON_FROIDE', 'GLUTEN', 'DESSERT', 'LACTOSE', 'ENTREE_CHAUDE', 'NUTS', 'ENTREE_FROIDE', 'OTHER', 'PLAT', 'PEANUTS', 'PLAT_CHEF', 'SHELLFISH', 'SOY'),
    constraint FKQMEB1LHPQAJUD8Q272KVG28DW
        foreign key (CUSTOMER_CUSTOMER_ID) references CUSTOMER
);

create table CUSTOMER_PREFERENCES
(
    CUSTOMER_CUSTOMER_ID BIGINT not null,
    PREFERENCES          TINYINT,
    constraint FK38294BQC9PB9OWIX8O9QW7SBF
        foreign key (CUSTOMER_CUSTOMER_ID) references CUSTOMER,
    check ("PREFERENCES" BETWEEN 0 AND 6)
);

create table CUSTOMER_TABLE
(
    ID           BIGINT auto_increment
        primary key,
    CAPACITE     INTEGER not null,
    NUMERO_TABLE INTEGER not null,
    STATUT       ENUM ('PAID', 'AVAILABLE', 'CANCELED', 'REFUNDED', 'BUSY', 'PENDING', 'UNPAID', 'RESERVED', 'READY', 'VALIDATE')
);

create table DELIVERY_DETAILS
(
    DELIVERY_DETAILS_ID   BIGINT auto_increment
        primary key,
    DELIVERY_ADDRESS      CHARACTER VARYING(255),
    DELIVERY_CITY         CHARACTER VARYING(255),
    DELIVERY_COUNTRY      CHARACTER VARYING(255),
    DELIVERY_DATE         CHARACTER VARYING(255),
    DELIVERY_EMAIL        CHARACTER VARYING(255),
    DELIVERY_FIRST_NAME   CHARACTER VARYING(255),
    DELIVERY_LAST_NAME    CHARACTER VARYING(255),
    DELIVERY_PHONE_NUMBER CHARACTER VARYING(255),
    DELIVERY_POSTAL_CODE  CHARACTER VARYING(255),
    DELIVERY_STATUS       ENUM ('CUSTOMER', 'CANCELED', 'DELIVERED', 'PENDING')
);

create table DISH
(
    DISH_ID          BIGINT auto_increment
        primary key,
    CATEGORY         ENUM ('EGGS', 'BOISSON_CHAUDE', 'SULFITES', 'FISH', 'BOISSON_FROIDE', 'GLUTEN', 'DESSERT', 'LACTOSE', 'ENTREE_CHAUDE', 'NUTS', 'ENTREE_FROIDE', 'OTHER', 'PLAT', 'PEANUTS', 'PLAT_CHEF', 'SHELLFISH', 'SOY'),
    DESCRIPTION      CHARACTER VARYING(1000),
    IMAGE            CHARACTER LARGE OBJECT,
    IS_SPECIAL_PRICE BOOLEAN          not null,
    NAME             CHARACTER VARYING(255),
    PRICE            DOUBLE PRECISION not null,
    STATUS           ENUM ('AVAILABLE', 'COMING_SOON', 'NOT_AVAILABLE')
);

create table EMPLOYEE
(
    EMPLOYEE_ID  BIGINT auto_increment
        primary key,
    CREATED_DATE TIMESTAMP,
    EMAIL        CHARACTER VARYING(255),
    LAST_LOGIN   TIMESTAMP,
    LOGIN        CHARACTER VARYING(255),
    NAME         CHARACTER VARYING(255),
    PASSWORD     CHARACTER VARYING(255),
    PHONE_NUMBER CHARACTER VARYING(255),
    ROLE         ENUM ('ADMIN', 'CHEF', 'WAITER')
);

create table ACCESS_CODE
(
    CODE_ID     BIGINT auto_increment
        primary key,
    CODE        CHARACTER VARYING(255) not null
        constraint UK696S3FO8TKOI5Q16MNN0DMAQ8
            unique,
    EXPIRY_DATE TIMESTAMP              not null,
    USED        BOOLEAN                not null,
    CUSTOMER_ID BIGINT                 not null,
    EMPLOYEE_ID BIGINT                 not null,
    constraint FK7EHO0C5CIYG6DHR3DCGTIKRFV
        foreign key (EMPLOYEE_ID) references EMPLOYEE,
    constraint FKNA70BRDASYC33CBD01VGX0ACB
        foreign key (CUSTOMER_ID) references CUSTOMER
);

create table LOGIN_CUSTOMER
(
    ID              BIGINT auto_increment
        primary key,
    ACCESS_CODE     CHARACTER VARYING(255),
    CLIENT_ID       CHARACTER VARYING(255),
    CREATED_DATE    TIMESTAMP,
    EXPIRATION_DATE TIMESTAMP,
    IS_VALID        BOOLEAN not null
);

create table ORDER_DETAILS
(
    ORDER_DETAILS_ID    BIGINT auto_increment
        primary key,
    COMMENT             CHARACTER VARYING(255),
    PAYMENT_METHOD      CHARACTER VARYING(255),
    PAYMENT_STATUS      ENUM ('PAID', 'AVAILABLE', 'CANCELED', 'REFUNDED', 'BUSY', 'PENDING', 'UNPAID', 'RESERVED', 'READY', 'VALIDATE'),
    DELIVERY_DETAILS_ID BIGINT
        constraint UKJWX83TT4AP9A7T5XP79EDFCR0
            unique,
    constraint FKIMBT82VP0DN4FFJ6C97DTWGDQ
        foreign key (DELIVERY_DETAILS_ID) references DELIVERY_DETAILS
);

create table ORDER_DISHES
(
    ID               BIGINT auto_increment
        primary key,
    COMMENT          CHARACTER VARYING(255),
    QUANTITY         INTEGER,
    DISH_ID          BIGINT,
    ORDER_DETAILS_ID BIGINT,
    constraint FKLO12MIXHUW6P3JOS1COIMBPXG
        foreign key (ORDER_DETAILS_ID) references ORDER_DETAILS,
    constraint FKSQ2EL5GNS896PPAJMO8NJTQPI
        foreign key (DISH_ID) references DISH
);

create table ORDER_ENTITY
(
    ORDER_ID         BIGINT auto_increment
        primary key,
    CUSTOMER_LOGIN   CHARACTER VARYING(255),
    ORDER_DATE       TIMESTAMP,
    ORDER_STATUS     ENUM ('PAID', 'AVAILABLE', 'CANCELED', 'REFUNDED', 'BUSY', 'PENDING', 'UNPAID', 'RESERVED', 'READY', 'VALIDATE'),
    ORDER_TYPE       ENUM ('INDOOR', 'OUTDOOR'),
    TABLE_NUMBER     INTEGER,
    TOTAL_AMOUNT     DOUBLE PRECISION not null,
    ORDER_DETAILS_ID BIGINT
        constraint UK7B0G6U1TKHBP7T77EFWFDH2JU
            unique,
    EMPLOYEE_ID      BIGINT,
    constraint FKJJV2BPYPKBJ9G5534TO9LCH0U
        foreign key (ORDER_DETAILS_ID) references ORDER_DETAILS,
    constraint FKT3DYOYIV2LWBPO54XC3O2YBFD
        foreign key (EMPLOYEE_ID) references EMPLOYEE
);

create table NOTIFICATION
(
    ID            BIGINT auto_increment
        primary key,
    CREATED_AT    TIMESTAMP,
    HAS_BEEN_READ BOOLEAN not null,
    MESSAGE       CHARACTER VARYING(255),
    EMPLOYEE_ID   BIGINT,
    ORDER_ID      BIGINT
        constraint UKLQY0TIO2LS6U508F6OY8ENX5S
            unique,
    constraint FK858APFRIBGISSO7WSLGVEBTXY
        foreign key (EMPLOYEE_ID) references EMPLOYEE,
    constraint FK993CLAA3117WPYFC8CUSPOJ9T
        foreign key (ORDER_ID) references ORDER_ENTITY
);

create table TABLE_ASSIGNMENT
(
    ID           BIGINT auto_increment
        primary key,
    TABLE_NUMBER INTEGER not null,
    EMPLOYEE_ID  BIGINT  not null,
    constraint FK37KAISCT5PM8NGXLIDDPQ4CKN
        foreign key (EMPLOYEE_ID) references EMPLOYEE
);

