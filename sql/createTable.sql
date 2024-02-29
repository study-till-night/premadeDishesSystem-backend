create table if not exists contract
(
	product_name varchar(50) not null,
	product_num int not null comment '单次配送数量',
	uid2 int not null comment '供应方',
	product_id int not null comment '货物id',
	frequency int null comment '配送频率',
	price double not null,
	validity_time int not null comment '有效期限',
	status int default 0 not null comment '0 供应商回复
1 买家回复
2 协商成功
3 协商失败',
	refuse_reason int null comment '拒绝理由',
	custom_grade int null,
	custom_comment varchar(250) null,
	custom varchar(255) not null comment '个性化配比',
	create_time datetime null,
	update_time datetime null,
	uid1 int not null comment '需求方',
	contract_id int auto_increment
		primary key,
	start_time datetime null comment '订单起始时间',
	deliver_times int default 0 not null comment '发货次数'
)
comment '长期合同';

create table if not exists delivery_record
(
	cid int not null comment '哪个交易单',
	update_time datetime null,
	record_id int auto_increment
		primary key,
	times int not null comment '第几次发货',
	create_time datetime null,
	is_late bit default b'0' not null,
	constraint delivery_record_contract_contract_id_fk
		foreign key (cid) references contract (contract_id)
)
comment '长期交易发货记录';

create table if not exists product_category
(
	category_id int auto_increment
		primary key,
	create_time datetime null,
	update_time datetime null,
	category_name varchar(45) not null
);

create table if not exists sys_role_menu
(
	component varchar(50) not null,
	menu_id int auto_increment
		primary key,
	path varchar(50) not null,
	name varchar(50) not null,
	role_id int not null,
	title varchar(50) not null,
	hidden bit not null
)
comment '前端菜单';

create table if not exists user_account
(
	uid int auto_increment
		primary key,
	role int null,
	user_name varchar(45) not null,
	update_time datetime null,
	create_time datetime null,
	password varchar(100) not null,
	constraint user_role_user_name_uindex
		unique (user_name)
);

create table if not exists address
(
	address_id int auto_increment,
	uid int not null,
	is_default bit default b'0' not null,
	user_name varchar(45) not null,
	mobile varchar(45) not null,
	province varchar(45) not null,
	city varchar(45) not null,
	district varchar(45) not null,
	detailed_address varchar(45) not null,
	create_time datetime null,
	update_time datetime null,
	primary key (address_id, uid),
	constraint address_uid
		foreign key (uid) references user_account (uid)
);

create index address_uid_idx
	on address (uid);

create table if not exists product_info
(
	pid int auto_increment
		primary key,
	pname varchar(45) not null,
	freight double not null comment '运费
',
	least_num int not null comment '起售量 对于校企',
	picture_path varchar(200) default '' not null comment '商品图片',
	is_opened bit default b'0' not null,
	pay_type varchar(45) not null comment '计价单位',
	update_time datetime null,
	sell_num int default 0 not null comment '已售数量',
	seller_id int not null comment '卖家用户id',
	is_deleted bit default b'0' not null,
	create_time datetime null,
	category_id int not null comment '商品类型id',
	customizable bit default b'1' not null,
	abstruct varchar(255) not null comment '简介',
	constraint product_cid
		foreign key (category_id) references product_category (category_id),
	constraint product_sid
		foreign key (seller_id) references user_account (uid)
);

create table if not exists prod_comment
(
	cid int auto_increment
		primary key,
	pid int not null,
	uid int not null comment '发表评论的用户',
	love int default 0 not null comment '点赞',
	create_time datetime null,
	update_time datetime null,
	oid int not null comment '发表评论的订单',
	content text not null,
	constraint prod_comment___uid
		foreign key (uid) references user_account (uid),
	constraint prod_comment_pid
		foreign key (pid) references product_info (pid)
)
comment '货物评价';

create index product_cid_idx
	on product_info (category_id);

create index product_sid_idx
	on product_info (seller_id);

create table if not exists product_recipe
(
	pid int not null,
	seasoning text not null comment '调料',
	steps text not null comment '制作步骤',
	tips text not null comment '注意事项',
	ingredients text not null comment '食材',
	love int default 0 not null comment '点赞数',
	create_time datetime not null,
	update_time datetime null,
	seller_id int not null comment '由哪家供应商提出',
	rid int auto_increment
		primary key,
	constraint product_recipe___pid
		foreign key (pid) references product_info (pid),
	constraint product_recipe___uid
		foreign key (seller_id) references user_account (uid)
)
comment '公开制作配方';

create table if not exists product_specs
(
	inventory int not null comment '库存',
	price int not null,
	is_enabled bit default b'1' not null comment '是否启用',
	version int default 1 not null,
	update_time datetime null,
	create_time datetime null,
	spec_name varchar(45) not null,
	pid int not null,
	product_spec_id int auto_increment
		primary key,
	constraint product_specs_pid
		foreign key (pid) references product_info (pid)
);

create table if not exists order_product
(
	oid int auto_increment
		primary key,
	uid int not null,
	spec_id int not null comment '商品规格',
	count int not null,
	aid int not null comment '收货地址',
	total_price double not null,
	deliver_time datetime null comment '发货时间',
	reach_time datetime null comment '送达时间',
	confirm_time datetime null comment '确认时间',
	update_time datetime null,
	create_time datetime null,
	is_commented bit default b'0' not null,
	pid int not null,
	seller_id int not null comment '供应商id',
	status int default 0 not null comment '0 未发货
1 已发货
2 已送达
3 已确认',
	constraint order_aid
		foreign key (aid) references address (address_id),
	constraint order_pid
		foreign key (pid) references product_info (pid),
	constraint order_seller_id
		foreign key (seller_id) references user_account (uid),
	constraint order_spec
		foreign key (spec_id) references product_specs (product_spec_id),
	constraint order_uid
		foreign key (uid) references user_account (uid)
);

create index order_pid_idx
	on order_product (pid);

create index order_spec_idx
	on order_product (spec_id);

create index order_uid_idx
	on order_product (uid);

create index product_specs_pid_idx
	on product_specs (pid);

create table if not exists provider_grade
(
	uid int not null
		primary key,
	credit int default 0 not null comment '诚信度 未按时发货次数',
	creativity int default 0 not null comment '个性化配比受到好评次数',
	create_time datetime null,
	update_time datetime null,
	transparency int default 0 not null comment '公开制作工艺次数',
	constraint provider_grade___uid
		foreign key (uid) references user_account (uid)
)
comment '供应商数值评估';

create table if not exists shop_car
(
	cid int auto_increment comment '购物车栏目id'
		primary key,
	uid int not null comment '购物车栏目所属用户',
	pid int not null comment '商品id',
	count int not null comment '商品数量',
	spec_id int not null comment '商品所属种类',
	update_time datetime null,
	create_time datetime null,
	is_chosen bit default b'1' null comment '商品是否选中',
	constraint shop_car___fk1
		foreign key (uid) references user_account (uid),
	constraint shop_car___fk2
		foreign key (pid) references product_info (pid),
	constraint shop_car___fk3
		foreign key (spec_id) references product_specs (product_spec_id)
)
comment '购物车';

create table if not exists user_details
(
	uid int not null
		primary key,
	gender int default 0 null,
	mobile varchar(45) null,
	email varchar(45) null,
	nick_name varchar(50) null,
	real_name varchar(45) null,
	create_time datetime null,
	update_time datetime null
);

create index uid_idx
	on user_details (uid);

create table if not exists user_org
(
	charger_name varchar(50) not null,
	charger_mobile varchar(50) not null,
	update_time datetime null,
	create_time datetime null,
	uid int not null
		primary key,
	org_name varchar(50) not null,
	org_address varchar(55) not null,
	org_type int not null,
	license_id varchar(18) not null,
	constraint user_org_user_account_uid_fk
		foreign key (uid) references user_account (uid)
)
comment '学校/企业认证信息';

