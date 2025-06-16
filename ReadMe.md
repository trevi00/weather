## 날씨 일기

### 테스트 코드 실행해보기
JUnit
- assertSame(a, b) : 객체 a,b 가 같은 객체임을 확인, 객체 자체를 비교한다
- assertEquals(a, b) : 객체 a,b가 일치함을 확인 객체에 정의된 equals를 통해 비교
- assertArrayEquals(a, b) : 배열 a와 b가 일치함을 확인
- assertTrue(a) : a가 참인지 확인
- asswertNotNull(a) : a가 null인지 확인

### Persistence Framework
DB와의 연동되는 시스템을 빠르게 개발하고, 안정적인 구동을 보장해주는 프레임워크

장점
1. 재사용 및 유지보수에 용이
2. 직관적인 코드

종류
1. SQL Mapper
2. ORM

#### SQL Mapper
- SQL을 개발자가 직접 작성 (ex. 'SELECT name FROM students)
- 매핑 : 쿼리 수행 결과와 객체가 상호작용
- 단점 : DB 종류 변경시 쿼리 수정 필요
- 단점 : 비슷한 쿼리를 반복적으로 작성해야함

#### ORM : Objdct Relation Mapping
- Object와 DB테이블을 매핑
- java 함수를 사용하면 자동으로 SQL이 만들어짐
- 매핑 : DB 테이블과 객체가 상호작용
- 단점 : 복잡한 쿼리를 자바 메서드 만으로 해결하는 것이 불편함


## JPA vs JDBC
JPA : JAVA Persistent API
ORM(Object Relation Mapping)의 일종

JDBC : Java Database Connectivity
SQL Mapper의 일종


### JDBC 방식으로 데이터 저장하기
#### build.gradle
implementation 'org.springframework.boot:spring-boot-starter-jdbc'
runtimeOnly 'mysql:mysql-connector-java'

#### application.properties
- spring.datasource.driver-class-name
- spring.datasource.url
- spring.datasource.username
- spring.datasource.password
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/project?serverTimezone=UTC&characterEncording=UTF-8
spring.datasource.username=root
spring.datasource.password=rootPassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### JPA로 데이터 저장하기
자바 ORM 기술의 표준 명세

#### build.gradle
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'

#### application.properties
```properties
spring.jpa.show-sql=true
spring.jap.database=mysql
```
#### GeneratedValue
- GenerationType.AUTO : 자동적으로
- GenerationType.IDENTITY : DBMS에게 맡김
- GenerationType.SEQUENCE : 데이터베이스 오브젝트를 만들어서 키생성을 해줌
- GenerationType.TABLE : 키생성을 위한 테이블을 만듬

## MVC 패턴
**Client**
dto
**Controller**
dto
**Service**
dto
**Repository**
Entity
**DB**

### 받아온 데이터 (json) 사용 가능하게 파싱하기
Json을 다루기 위한 디펜던시 추가하기
implementation 'com.googlecode.json-simple:json-simple:1.1.1'


### diary table
```mysql
CREATE TABLE diary (
                       id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                       weather VARCHAR(50) NOT NULL,
                       icon VARCHAR(50) NOT NULL,
                       temperature DOUBLE NOT NULL,
                       text VARCHAR(500) NOT NULL,
                       `date` DATE NOT NULL
);
```

## Transaction
데이터베이스의 상태를 변화시키기 위해 수행하는 작업 단위

작업단위
EX)
트랜잭션 : 오늘 일기 작성하기
1. 오늘 날씨 데이터를 가져와서
2. 일기를 DB에 저장하기

### 속성
- 원자성 (Atomicity) : 모두 반영이 되거나 반영이 되지 않는다.
- 일관성 (Consistency) : 작업처리 결과는 항상 일관적이어야 한다.
- 독립성 (Isolation) : 여러 데이터베이스를 건드는 crud작업에서 끼어드는 현상이 발생할 수 없다.
- 지속성 (Durability) : 트랜잭션이 성공적으로 완료가 되었다면, 변화된 상태가 지속되어야 한다.

### 트랜잭션의 연산
- 커밋 (Commit)
- 롤백 (Rollback)

### Dirty Read : 여러 트랜잭션이 경쟁하면 생기는 문제
- 트랜잭션 A : Diary 테이블의 3번째 row 수정중 문제 \> 롤백
- 트랜잭션 B : Diary 테이블의 3번째 row 조회 시도 (롤백되기 전의 값을 읽음)

### Non-Repeatable Read
- 트랜잭션 A : Diary 테이블의 3번째 row 조회\*2
- 트랜잭션 B : Diary 테이블의 3번째 row 수정 후 커밋

첫번째 조회 시에는 B의 수정 전의 데이터.
두번째 조회 시에는 B의 수정 후의 데이터.
일관성을 만족시키지 못함

### Phantom Read
트랜잭션 A : Diary 테이블의 0~4번째 row 조회\*2
트랜잭션 B : Diary 테이블의 3번째 row 수정 후 커밋

특정 범위 내의 값을 경쟁했을때 생기는 문제
일관성을 만족시키지 못함

## Spring 트랜잭션
```java
@Transactional
```
클래스, 메서드 위에 어노테이션을 추가할 수 있다.
트랜잭션 기능이 적용된 프록시 객체 생성 (Platform Transaction Manager)

### 스프링 트랜잭션의 세부 설정들
1. Isolation (격리수준)
2. Propagation (전파수준)
3. ReadOnly 속성
4. 트랜잭션 롤백 예외
5. timeout 속성

#### Isolation (격리 수준)
트랜잭션에서 일관성이 없는 데이터를 허용하는 수준

- DEFAULT : 사용하고 있는 데이터베이스의 기본 Isolation 레벨을 따르겠다.
- READ_UNCOMMITTED (Dirty Read 발생) : 느슨한 격리 수준
- READ_COMMITTED (Dirty Read 방지) : 트랜잭션이 커밋이 된 확장 데이터만 읽는다.
- REPEATABLE_READ (Non-Repeatable Read 방지) : 트랜잭션 전체가 완료 될때까지 락을 걸어서 읽기를 방지한다.
- SERIALIZABLE (Phantom Read 방지) : 엄밀한 격리 수준, 데이터의 일관성과 동시성을 위해서, 트랜잭션이 완료될 때까지 범위 데이터에 쉐어드 락이 걸린다.

#### Isolation 격리 수준
트랜잭션에서 일관성이 없는 데이터를 허용하는 수준
```java
@Transactional(isolation=isolation.DEFAULT)
```
#### Propagation (전파 수준)
트랜잭션 동작 도중 다른 트랜잭션을 호출하는 상황

트랜잭션을 시작하거나
기존 트랜잭션에 참여하는 방법에 대해 결장하는 속성값

- REQUIRED
- SUPPORTS
- REQUIRES_NEW
- NESTED
...

> NESTED 사용 예지
> EX) 일기 작성 관련해서 로그를 DB에 저장하는 상황
> 1. 로그 저장이 실패한다고 해서, 일기 작성까지 롤백되면 안됨
> 2. 일기 작성이 실패한다면, 로그 작성까지 롤백되어야 함

#### readOnly 속성
트랜잭션을 읽기 전용으로 속성으로 지정
```java
@Transactional(readOnly=true)
```

#### 트랜잭션 롤백 예외
예외 발생했을때 트랜잭션 롤백시킬 경우를 설정
```java
@Trnasactional(rollbackFor=Exception.class)
@Transactional(noRollBackFor=Exception.class)
```
Default:RuntimeException, Error

#### Timeout 속성
일정 시간 내에 트랜잭션 끝내지 못하면 롤백
```java
@Transactional(timeout=10)
```

### 트랜잭션 코드에 반영하기
```java
@Transactional
@EnableTransactionManagement // 프로젝트 안에서 트랜젝션이 동작

@Transactional
Class{
    Method()
    Method()
}
```

### 캐싱
데이터 등을 미리 복사해서 저장해두는 것
장점 : 요청을 빠르게 처리

#### 캐싱을 어떻게 하는가
- 웹 브라우저에 캐시
- 서버에서 캐시

#### 유의할 점
요청한 것에 대한 응답이 변하지 않을때만 사용할 수 있다.

2033년 3월 30일의 서울 날씨 예측값 : 변할 수 있음 (캐싱 불가능)
2022년 2월 22일의 서울 날씨 : 변하지 않음 (캐싱 가능)

#### 캐싱 적용 전
1. OpenWeatherMap에서 데이터 받아오기
2. 받아온 데이터 (json) 사용 가능하게 파싱하기
3. 우리 DB에 저장하기

#### 캐싱 적용 후 두번째 요청시
1. 우리 DB에서 불러오기

#### 캐싱 적용했을때 장점
1. 요청을 빠르게 처리
2. 서버의 부하가 줄어든다
3. (현 프로젝트의 경우) Open Weather Map API 사용료 절감

#### 매일 새벽 1시에 얻어오는 법
```java
@Schedulde(cron="0 0 1 * * *")
```
0 0 \* \* \* \* : 매시각

\*/10 \* \* \* \* \* : 10초마다

0 0 8\-10 \* \* \* : 매일 8시, 9시, 10시

#### table date_weather
```mysql
create table date_weather(
    'date' DATE not null primary key,
    weather VARCHAR(50) NOT NULL,
    icon VARCHAR(50) NOT NULL,
    temperature DOUBLE NOT NULL
);
```

## 로그
로그를 왜 쓰는가.
1. 서비스 동작 상태 파악
2. 장애 파악

#### 로그를 작성하는 법
1. sout("로그내용")
2. Logging library 사용

#### Logging library
- log4j \-\> log4j2
- logback

### 로그 레벨
1. Error
2. Warn
3. Info
4. Debug
5. Trace

#### 설정
logback-spring.xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    <property name="LOGS_PATH" value="./logs"/>
    <property name="LOGS_LEVEL" value="INFO"/>
    <property name="LOG_PATTERN" value="[%d{yyyy-MM-dd HH:mm:ss}:%-3relative][%thread] %-5level %logger{35} - %msg%n"/>

    <!-- 1. Console Appender -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>${LOG_PATTERN}</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <!-- 2. File Appender (Rolling, Size & Time Based) -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOGS_PATH}/log_file.log</file>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>${LOG_PATTERN}</pattern>
            <charset>UTF-8</charset>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOGS_PATH}/%d{yyyy-MM-dd}_%i.log</fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>10MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <maxHistory>60</maxHistory>
        </rollingPolicy>
    </appender>

    <!-- 3. Error Appender (ERROR 레벨만 별도 파일로 저장) -->
    <appender name="ERROR_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOGS_PATH}/error.log</file>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>${LOG_PATTERN}</pattern>
            <charset>UTF-8</charset>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOGS_PATH}/error.%d{yyyy-MM-dd}_%i.log</fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>10MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <maxHistory>60</maxHistory>
        </rollingPolicy>
    </appender>

    <root level="${LOGS_LEVEL}">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
        <appender-ref ref="ERROR_FILE"/>
    </root>
</configuration>
```

application.properties
```properties
logging.config=classpath:logback-spring.xml
```

.gitignore
맨 하단에 logs


## 예외 처리

#### Spring의 기본적인 예외 처리 1
http://127.0.0.1:8080/abc
Whitelabel Error Page

#### Spring의 기본적인 예외 처리 2
Ex) http://localhost:8080/read/diary?date=2020-04-20000
@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date

#### Spring의 기본적인 에외 처리 3
try {

} catch {

}

#### Custom Exception 만들어서 처리
```java
public class InvalidDate extends RuntimeException {
    private static final String MESSAGE = "너무 미래의 날짜입니다.";
    public InvalidDate(){
        super(MESSAGE);
    }
}
```

### Exception Handler
@Controller, @RestController의 예외를
하나의 메서드에서 처리해주는 기능

```java
@GetMapping("/read/diaries")
List<Diary> readDiaries(){
    // ...
}

@PutMapping("/update/diary")
void updateDiary(){
    // ...
}

@ExceptionHandler()
RepositoryEntity<ErrorResponse> handleLineException(){
    // ...
}

@ExceptionHandler(InvalidDate.class)
RepositoryEntity<ErrorResponse> handleLineException(final InvalidDate.class error){
}
```

#### ControllerAdivce
모든 Controller 단을 대상으로 하여 예외가 발생한 것을 잡아줌

## API documentation
- 프론트 개발자에게 문서를 전해야함
- 백엔드 개발자끼리 공유해야함

#### 1. txt 파일에 정리
GET/read/diary
POST/create/diary
...

#### 2. tool 사용
1. Swagger
2. ReDoc
3. GitBook
4. ...

#### Summary
API documentation은 백엔드 개발자의 덕목

#### Dependency
```gradle
implementation 'io.springfox:springfox-boot-starter:3.0.0'
implementation 'io.springfox:springfox-swagger-ui:3.0.0'
```
스프링 부트 3에서는
```gradle
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9'
```
#### Application.properties
```properties
spring.mvc.pathmatch.matching-strategy=ANT_PATH_MATCHER
```