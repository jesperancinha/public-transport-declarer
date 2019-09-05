# Bisca JE Rest API

## Start Docker MongoDB Image

```
$ docker build --tag bisca/je .

$ docker run -p 27017:27017 --name biscaje -d bisca/je


```
## Build notes

1. OGM driver versions:

* OGM at the time this is written is not compatible with  version [3.2.2](http://mvnrepository.com/artifact/org.mongodb/mongo-java-driver) of it's [mongo-java-driver](http://mvnrepository.com/artifact/org.mongodb/mongo-java-driver)

* Instead use version [2.13.0](http://mvnrepository.com/artifact/org.mongodb/mongo-java-driver/2.13.0)

* More info [here](https://forum.hibernate.org/viewtopic.php?f=31&t=1040302)

2. Install active MQ

```
$ brew install activeMQ
```

---
## Software Security References

* http://www.javamex.com/tutorials/cryptography/pbe_salt.shtml

* https://www.schneier.com/

* http://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/

* https://nakedsecurity.sophos.com/2013/11/20/serious-security-how-to-store-your-users-passwords-safely/

* https://www.owasp.org/index.php/Hashing_Java

* https://paragonie.com/blog/2016/02/how-safely-store-password-in-2016

* https://password-hashing.net/

## General References

* http://hibernate.org/ogm/

* https://forum.hibernate.org/viewtopic.php?f=31&t=1040302

* http://programmers.stackexchange.com/questions/150045/what-is-the-point-of-having-every-service-class-have-an-interface

* https://en.wikipedia.org/wiki/SOLID_(object-oriented_design)

* https://en.wikipedia.org/wiki/Dependency_inversion_principle

## License

```text
Copyright 2016-2019 João Esperancinha (jesperancinha)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## About me

-   [![Generic badge](https://img.shields.io/static/v1.svg?label=Homepage&message=joaofilipesabinoesperancinha&color=informational)](http://joaofilipesabinoesperancinha.nl)

-   [![Twitter Follow](https://img.shields.io/twitter/follow/jofisaes.svg?label=%40jofisaes&style=social)](https://twitter.com/intent/follow?screen_name=jofisaes)

-   [![GitHub followers](https://img.shields.io/github/followers/jesperancinha.svg?label=jesperancinha&style=social)](https://github.com/jesperancinha)

-   Free Code Camp [jofisaes](https://www.freecodecamp.org/jofisaes)

-   Hackerrank [jofisaes](https://www.hackerrank.com/jofisaes)

-   Acclaim Badges [joao-esperancinha](https://www.youracclaim.com/users/joao-esperancinha/badges)

-   Projects:

    -   [![Generic badge](https://img.shields.io/static/v1.svg?label=Homepage&message=Time%20Disruption%20Studios&color=informational)](http://tds.joaofilipesabinoesperancinha.nl/)
    -   [![Generic badge](https://img.shields.io/static/v1.svg?label=Homepage&message=Image%20Train%20Filters&color=informational)](http://itf.joaofilipesabinoesperancinha.nl/)
    -   [![Generic badge](https://img.shields.io/static/v1.svg?label=Homepage&message=MancalaJE&color=informational)](http://mancalaje.joaofilipesabinoesperancinha.nl/)
    -   [![Generic badge](https://img.shields.io/static/v1.svg?label=Google%20Apps&message=Joao+Filipe+Sabino+Esperancinha&color=informational)](https://play.google.com/store/apps/developer?id=Joao+Filipe+Sabino+Esperancinha)
-   Releases:
    -   itf-chartizate-android:   
        [![Maven Central](https://img.shields.io/maven-central/v/org.jesperancinha.itf/itf-chartizate-android)](https://search.maven.org/search?q=a:itf-chartizate-android) 
        [![Download](https://api.bintray.com/packages/jesperancinha/maven/itf-chartizate-android/images/download.svg)](https://bintray.com/jesperancinha/maven/itf-chartizate-android/_latestVersion)
    -   itf-chartizate-java:   
        [![Maven Central](https://img.shields.io/maven-central/v/org.jesperancinha.itf/itf-chartizate)](https://search.maven.org/search?q=a:itf-chartizate)
    -   itf-chartizate-api:  
        [![Maven Central](https://img.shields.io/maven-central/v/org.jesperancinha.itf/itf-chartizate-api)](https://search.maven.org/search?q=a:itf-chartizate-api)
