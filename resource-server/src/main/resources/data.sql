-- 初始化用户表
insert into user(id, account, password, name, age, balance) values (1, 'zhangsan', '1', '张三', 23, 140.05);
insert into user(id, account, password, name, age, balance) values (2, 'lisi', '1', '李四', 24, 154.02);
insert into user(id, account, password, name, age, balance) values (3, 'wangwu', '1', '王五', 22, 350.98);
insert into user(id, account, password, name, age, balance) values (4, 'zhaoliu', '1', '赵六', 21, 560.04);
insert into user(id, account, password, name, age, balance) values (5, 'maqi', '1', '马七', 32, 133.23);
insert into user(id, account, password, name, age, balance) values (6, 'houba', '1', '猴八', 43, 68.73);

-- 初始化第三方应用表
insert into third_client(id, name, client_id, redirect_uri) values(1, '但丁App', '6de66752f7fc4f1aa4ad6e792d12a45e', 'http://localhost:8001/dante/oauth/callback');
