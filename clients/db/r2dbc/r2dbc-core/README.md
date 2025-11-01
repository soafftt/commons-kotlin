### R2DBC core

#### dependencies
```groovy
depenceies {
    implementation("org.springframework.boot:spring-boot-starter:3.4.0")
    implementation("io.asyncer:r2dbc-mysql:1.4.1")
    
    api("org.springframework.boot:spring-boot-starter-data-r2dbc:3.4.0")
    
    api("io.r2dbc:r2dbc-proxy:1.1.6.RELEASE")
    api("io.r2dbc:r2dbc-pool:1.0.2.RELEASE")

    api("org.jetbrains.exposed:exposed-core:1.0.0-rc-2")
    api("org.jetbrains.exposed:exposed-r2dbc:1.0.0-rc-2")
}
```
<br />

#### 참고
모듈의 기본 방향성이 Spring을 기본으로 사용한다는 가정의 모듈이며, 추후 Spring 의 의존성을 제거할 수 있습니다.

Spring-data 와 Exposed 의 기반 연결 구조를 생성할 수 있는 Wrapping 모듈입니다.    
지원 database 는 `MySql`, `Postgresql` 를 지원하고 있으며, ConnectionFactory 생성방식은  `spring.boot.r2bc` 의 `ConnectionFactoryBuilder` 와 `spring.data.r2bc` 의 `Converter`, `dialect` 를 사용합니다.  
  
