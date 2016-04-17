# Bisca JE Rest API

## Build notes

1. OGM driver versions:

* OGM at the time this is written is not compatible with  version [3.2.2](http://mvnrepository.com/artifact/org.mongodb/mongo-java-driver) of it's [mongo-java-driver](http://mvnrepository.com/artifact/org.mongodb/mongo-java-driver)

* Instead use version [2.13.0](http://mvnrepository.com/artifact/org.mongodb/mongo-java-driver/2.13.0)

* More info [here](https://forum.hibernate.org/viewtopic.php?f=31&t=1040302)

2. JETTY Server

* Jetty Server does not come with CDI and that must be added:

```
<dependency>
    <groupId>org.jboss.weld.servlet</groupId>
    <artifactId>weld-servlet-core</artifactId>
    <version>${weld.servlet}</version>
</dependency>
```

---
## Software Security References

* http://www.javamex.com/tutorials/cryptography/pbe_salt.shtml

* https://www.schneier.com/

* http://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/

* https://nakedsecurity.sophos.com/2013/11/20/serious-security-how-to-store-your-users-passwords-safely/

## General References

* http://hibernate.org/ogm/

* https://forum.hibernate.org/viewtopic.php?f=31&t=1040302

* http://programmers.stackexchange.com/questions/150045/what-is-the-point-of-having-every-service-class-have-an-interface

* https://en.wikipedia.org/wiki/SOLID_(object-oriented_design)

* https://en.wikipedia.org/wiki/Dependency_inversion_principle