### R2DBC Single-tenant

#### dependencies
```groovy

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:3.4.0")
    api(project(":clients:db:r2dbc:r2dbc-core"))
}


```

#### 개요
R2DBC Core 모듈을 활용하여 Master/Replica 연결 구조를 가지는 DB 설정을 합니다.  
`r2dbc.implementation` 설정에 따라 `SPRING_JPA` 의 경우 다음과 같은 Bean 설정을 추가합니다. 
```kotlin
// Writer
@EnableR2dbcRepositories(
    basePackages = ["\${r2dbc.jpa-scan-packages.writer}:}"],
    entityOperationsRef = "r2dbcEntityOperations"
)
@EnableR2dbcAuditing
@EnableTransactionManagement

// Reader
@EnableR2dbcRepositories(
    basePackages = ["\${r2dbc.jpa-scan-packages.reader:}"],
    entityOperationsRef = "readerR2dbcEntityOperations"
)
@EnableR2dbcAuditing
@EnableTransactionManagement
```
상단의 예시는 writer/reader 의 JPARepository 사용설정이며, `r2dbc.implementation = EXPOSED` 인 경우 로드되지 않습니다.  
각 DB 구조는 `r2dbc.datasource.writer`, `r2dbc.datasource.reader` 설정의 유무에 따라 각각을 모두 구성하거나, 일부만 구성할 수 있습니다.  
<br />
Exposed 의 경우는 `@Bean("r2dbcDatabase")`, `@Bean("readerR2dbcDatabase")` 가 설정되어 사용자의 상황에 맞춰 사용할 수 있습니다. 