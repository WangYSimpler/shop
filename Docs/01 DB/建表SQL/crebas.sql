/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2017/7/18 8:07:49                            */
/*==============================================================*/


drop table SHOP.T_DICT cascade constraints;

drop table SHOP.T_ORDER cascade constraints;

drop table SHOP.T_SETTLE cascade constraints;

drop table SHOP.T_USER cascade constraints;

drop user SHOP;

/*==============================================================*/
/* User: SHOP                                                   */
/*==============================================================*/
create user SHOP identified by '';

/*==============================================================*/
/* Table: T_DICT                                                */
/*==============================================================*/
create table SHOP.T_DICT 
(
   ID                   INT                  not null
      constraint CKC_ID_T_DICT check (ID >= 0),
   DICT_OBJECT          NVARCHAR2(10),
   DICT_NAME            NVARCHAR2(100),
   DICT_PROPERTY        NUMBER(15,4),
   UP_OBJECT            NVARCHAR2(10),
   REMARK               NVARCHAR2(1000),
   DEL_FLAG             NVARCHAR2(2),
   CREATE_USER          NVARCHAR2(100),
   CREATE_DATE          DATE,
   UPDATE_USER          NVARCHAR2(100),
   UPDATE_DATE          DATE,
   constraint PK_T_DICT primary key (ID)
);

comment on table SHOP.T_DICT is
'字典表';

comment on column SHOP.T_DICT.DEL_FLAG is
'删除标志';

/*==============================================================*/
/* Table: T_ORDER                                               */
/*==============================================================*/
create table SHOP.T_ORDER 
(
   ID                   INT                  not null
      constraint CKC_ID_T_ORDER check (ID >= 0),
   ORDER_NO             NVARCHAR2(50),
   ORDER_NAME           NVARCHAR2(100),
   ORDER_OBJECT         NVARCHAR2(10),
   ORDER_STATUS         CHAR,
   ORDER_PRICE          NUMBER(15,4),
   ORDER_NUM            INT,
   ORDER_AMOUNT         NUMBER(20,4),
   FREE_NUM             INT,
   EXPRESS_NO           NVARCHAR2(50),
   EXPRESS_FEE          NUMBER(20,4),
   BUY_DATE             DATE,
   BUY_ADDRESS          NVARCHAR2(1000),
   REMARK               NVARCHAR2(1000),
   DEL_FLAG             NVARCHAR2(2),
   CREATE_USER          NVARCHAR2(100),
   CREATE_DATE          DATE,
   UPDATE_USER          NVARCHAR2(100),
   UPDATE_DATE          DATE,
   constraint PK_T_ORDER primary key (ID)
);

comment on table SHOP.T_ORDER is
'供应链订单';

comment on column SHOP.T_ORDER.DEL_FLAG is
'删除标志';

/*==============================================================*/
/* Table: T_SETTLE                                              */
/*==============================================================*/
create table SHOP.T_SETTLE 
(
   ID                   INT                  not null
      constraint CKC_ID_T_SETTLE check (ID >= 0),
   SETTLE_NO            NVARCHAR2(50),
   SETTLE_NAME          NVARCHAR2(100),
   SETTLE_STATUS        CHAR,
   PRIME_PRICE          NUMBER(15,4),
   PRIME_NUM            INT,
   PRIME_AMOUNT         NUMBER(20,4),
   EXPRESS_FEE          NUMBER(20,4),
   ORTHER_FEE           NUMBER(15,4),
   ORTHER_FEE_REASON    NVARCHAR2(100),
   ORDER_AMOUNT         NUMBER(20,4),
   MARGIN               NUMBER(20,4),
   REMARK               NVARCHAR2(1000),
   DEL_FLAG             NVARCHAR2(2),
   CREATE_USER          NVARCHAR2(100),
   CREATE_DATE          DATE,
   UPDATE_USER          NVARCHAR2(100),
   UPDATE_DATE          DATE,
   constraint PK_T_SETTLE primary key (ID)
);

comment on table SHOP.T_SETTLE is
'结算表';

comment on column SHOP.T_SETTLE.MARGIN is
'利润';

comment on column SHOP.T_SETTLE.DEL_FLAG is
'删除标志';

/*==============================================================*/
/* Table: T_USER                                                */
/*==============================================================*/
create table SHOP.T_USER 
(
   ID                   INT                  not null
      constraint CKC_ID_T_USER check (ID >= 0),
   USER_NO              NVARCHAR2(50),
   USER_NAME            NVARCHAR2(100),
   PASSWORD             NVARCHAR2(50),
   REMARK               NVARCHAR2(1000),
   DEL_FLAG             NVARCHAR2(2),
   CREATE_USER          NVARCHAR2(100),
   CREATE_DATE          DATE,
   UPDATE_USER          NVARCHAR2(100),
   UPDATE_DATE          DATE,
   constraint PK_T_USER primary key (ID)
);

comment on table SHOP.T_USER is
'用户表';

