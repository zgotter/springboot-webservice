# EC2 에서 RDS 접근 설정

## 1. MySQL CLI 설치

```
sudo yum install mysql
```

<br>

## 2. RDS 접속

```
mysql -u 계정 -p -h Host주소(앤드 포인트)
```

<br>

## 3. 쿼리 실행 테스트 (데이터베이스 목록 확인)

```
show database;
```

![데이터베이스 목록 확인](./img/ec2_rds_settings_001.jpg)