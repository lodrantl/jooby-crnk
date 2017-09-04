# Jooby Crnk module

[![License](https://img.shields.io/badge/License-Apache%202.0-yellowgreen.svg)](https://github.com/lodrantl/jooby-crnk/blob/master/LICENSE)


[Crnk](http://www.crnk.io/) is an implementation of the [JSON API](http://jsonapi.org) specification and recommendations in Java to facilitate building RESTful applications.

This module integrates Crnk into Jooby, allowing you to create CRUD application with minimal effort. 


## Requirements

Depends on development version of Crnk:
```
git clone https://github.com/crnk-project/crnk-framework.git

./gradlew install -x test
```

## Usage

In the app:

```java
use(new Crnk().path("/api")
        .repositories(new BoxRepository())
);
```

Using JPA and some custom configuration (requires jooby-hbm module first):
```java
use(new Crnk().path("/api").useJpa().doWith(boot -> {
    boot.getObjectMapper().setDateFormat(new ISO8601DateFormat());
}));
```


