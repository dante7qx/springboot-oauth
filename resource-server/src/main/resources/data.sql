-- 初始化用户表
insert into user(id, account, password, name, age, balance) values (1, 'zhangsan', '1', '张三', 23, 140.05);
insert into user(id, account, password, name, age, balance) values (2, 'lisi', '1', '李四', 24, 154.02);
insert into user(id, account, password, name, age, balance) values (3, 'wangwu', '1', '王五', 22, 350.98);
insert into user(id, account, password, name, age, balance) values (4, 'zhaoliu', '1', '赵六', 21, 560.04);
insert into user(id, account, password, name, age, balance) values (5, 'maqi', '1', '马七', 32, 133.23);
insert into user(id, account, password, name, age, balance) values (6, 'houba', '1', '猴八', 43, 68.73);

-- 初始化第三方应用表
insert into third_client(id, name, client_id, client_secret, redirect_uri, scope, private_key) 
values 
(1, '但丁App', '6de66752f7fc4f1aa4ad6e792d12a45e', '098uye012342343298342kolpppp2w3', 'http://localhost:8001/dante/oauth/callback', 'user,role', 'rO0ABXNyABRqYXZhLnNlY3VyaXR5LktleVJlcL35T7OImqVDAgAETAAJYWxnb3JpdGhtdAASTGphdmEvbGFuZy9TdHJpbmc7WwAHZW5jb2RlZHQAAltCTAAGZm9ybWF0cQB+AAFMAAR0eXBldAAbTGphdmEvc2VjdXJpdHkvS2V5UmVwJFR5cGU7eHB0AANSU0F1cgACW0Ks8xf4BghU4AIAAHhwAAAEwjCCBL4CAQAwDQYJKoZIhvcNAQEBBQAEggSoMIIEpAIBAAKCAQEAuZ4TcvLQyezAafPbE5zybbCAv/Q90GxgLuvH0O58dFV0aRddq/ArDH4NjHNOiN5qmlfhXRqFhK0iofSpdFIxL7ILG1O+vP0o7rgnjHDdUU7TknpgngMeQ1+oE86TDIQ6nfh48sUI0BsCNXkO/xQHAQ2two8i/O2bCroK5++YAx6B6KajqxU1NQrmEUVmNJZ7RrDQSchxvmxXtaSO549ldkKD8Do3JAQdG6eCHZp8AdFmv9vQGKOrBainHd+vgOjkz4c/5iUfq8gcC9Jk9GtqrwKvtw+xK4guYCJPRaNY9MoDCf+h+nqeWS6PdrD5h3iSTrpc1WtpMgzDJGATrAEOLQIDAQABAoIBAQCpTQVwUwkdJFRcT5AC06RLaDIG4z9+W1tfcK9QYjYh5c/ICwflS6n4OwEgmguHy1ZwcLedtBMUcNal4gKtlkpGtp5qzneq6T3sfwuTjV5QSxWvBPAbL7zqttUXO9PkFrdgXsHyrMTdC8V4AHMtNZct3sweKXBAQnin0Akig+ai8CIrTtrdICKEFVrf1rBobRRCKeXqPA1RF9kc5sxciMZDtqcARZaBc1dy3FkEmV2X2wWHwqYSOxwldBuQBTBCPfFnyIgEjfehSkmtPI1aSeZ2qXVHyVk7/z9ZDIGdY2wiFDx/Yx3Wh3Yy+uncEEyIJo6tv5ZMZ7BGrNlH2Ndh6WoBAoGBAOeXQ1a5Yh0XKevLPzGHFcJA8v07MsWQo7LAOdT0u7AjzvOHrQbxps0zATvSffHxWgUuh9QjHwEfAynD+9VNpBP2ShSHw7dCfiIOvAB+Hl4Z6H8sF9rrDfSTPw6sBb6oFrSTWXGhX0oTK+JV5yWmzQ9j7DWHmlEdyK/aaNJpxSiTAoGBAM0uXlcr0/yNd95c1eV4M/3kCU03ASbqMs99o9WeCuzoqnkmTgzPGg2wg2oO3rj/OvSa4tzlwMPMz/ggMN7T9jR0rITwpn6vLHcCiudhGLHbM33mPvV5qEr2xc9+3sucu6weQb8tF7Ylu0oVTTeKqTiNR47bzdG2rTBkEFZi2OY/AoGBAIOp6JvGe3REdg3bGEtFHGa63CqB3GSbzaVTSc27cXU7dm3XpdQ70HP62mmzSu0hJOerQ4eki/zsL7Uv+d2T7YN30zgG04s9n04niFTTXtpq6lZasBlC50Uz7Xae8AkrsPTqjMBgWTdY3OspoWczhIOKcIEiXeZeA3mOt2dgwLlHAoGAYB4hqbppYz0ucyeQOCAYUw65YMesrXs29EaKMm8H9TWFbp2IIK4AKXuQBGYU5hsClkXckti6db8DidwiqLAo/SfPDbPieyQ6s0Goqn2s4NysAYWT5tDoCr4RqoNngYDZ1eaSrClGm1iPpUPXWXaGkWx1ojHL+DsYZLvrf1xs+WcCgYB0k8D/USrgoIHaEbN1PekEFZpxzuy+Zab/2kjidyytKbjXkTRr6gq9yaCksrfvN0xS6h5a9gmvhC6RhY68dmhWBc15S3xaQlOpgVXj0E9gVPw0LH9Tn1XEHNgaemt/JKfbOosZD0Aoo0V/9k88CFxTJaOpNRTrovLiGc8BAno5K3QABlBLQ1MjOH5yABlqYXZhLnNlY3VyaXR5LktleVJlcCRUeXBlAAAAAAAAAAASAAB4cgAOamF2YS5sYW5nLkVudW0AAAAAAAAAABIAAHhwdAAHUFJJVkFURQ==');
