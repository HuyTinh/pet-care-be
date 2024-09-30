create table employees (
    id bigint primary key auto_increment,
    first_name nvarchar(155) not null,
    last_name nvarchar(155) not null,
    email nvarchar(155) not null,
    address nvarchar(255),
    gender enum('MALE','FEMALE'),
    account_id bigint not null,
    phone_number varchar(16) not null,
    image_url nvarchar(500)
)
