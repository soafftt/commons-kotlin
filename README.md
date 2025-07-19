### Commons
개인이 관리하는 유틸리티성 프로젝트로 자주 사용하는 유틸리티 및 client (DB / Http .. ETC)를 Wrapping 하여 자주사용하는 middleware 서비스를 쉽게 구성할 수 있도록 하는 프로젝트 입니다.

Client 구성의 경우 일부 기능에 있어서는 의존성 관리를 위하여 Spring.Core 를 이용한 Bean 구성이 포함될 수 있습니다.
이와같이 Spring.Core 기능을 사용하는 경우 보다 쉽게 구성을 제공하기 위하여, 일부 Client 기능이 설정에 따라 Bean 으로 제공 될 수 있습니다.




### Plugins
프로젝트는 kotlin 2.1 을 기반으로 하고 있기 때문에 Repo 에 포함된 모든 프로젝트는 아래와 같은 plugin 이 공통으로 apply 되어 있습니다.
```groovy
plugins {
    kotlin("jvm") version "2.1.0"
}

allprojects {
    apply(plugin = "kotlin")

    repositories {
        mavenCentral()
    }
}
```

멀티모듈에 의거하여, 모듈단위는 각각의 의존성에 대해서 모듈에 포함된 `build.gradle`에 정의되어 있습니다. 모듈을 fork 하여 분리가 필요한 경우 위의 plugin 은 항시 포함되어야 합니다.

### 프로젝트의 구조
commons 모듈은 크게 common, clients 구조로 이루어져 있으며 각 목표는 다음과 같습니다.

|모듈|비고|
|---|---|
|common| 유틸리티성 기능을 포함하며, 최소한의 의존성을 가지고 있습니다. 필요에 따라 json 과 같은 의존성을 사용할 수 있습니다|
|client| Redis, Http, DB 연결과 같은 middleware client 로 연결과정의 중복을 최소화를 목표합니다.|
|client#db#r2dbc#r2dbc-single| 단일 서비스의 Primary/Replica의 연결 구조를 쉬운 구성을 목표합니다.|
|client#db#r2dbc#r2dbc-multi| 여러서비스의 Primary/Replica 의 연결 구조를 Multi-tenancy 를 활용하여 쉬운 연결구조를 목표합니다|
|client#cache#redis-lettuce| Redis 의 Lettuce 클라이언트를 쉽게 구성하기 위하여 Wrapping 합니다.|
|client#cache#valkey-glide| Valkey 의 Glide 클라이언트를 쉽게 구성하기 위한 Wrapping 합니다.|
|client#http| ktor, webflux#webclient 를 기반으로 하여 연결 pool 생성 및 쉬운 client 구성을 목표합니댜.|


