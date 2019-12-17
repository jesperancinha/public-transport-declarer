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

## About me

[![Twitter Follow](https://img.shields.io/twitter/follow/joaofse?label=João%20Esperancinha&style=social)](https://twitter.com/joaofse)
[![GitHub followers](https://img.shields.io/github/followers/jesperancinha.svg?label=jesperancinha&style=social)](https://github.com/jesperancinha)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Homepage&message=joaofilipesabinoesperancinha.nl&color=informational)](http://joaofilipesabinoesperancinha.nl)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Homepage&message=Time%20Disruption%20Studios&color=informational)](http://tds.joaofilipesabinoesperancinha.nl/)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Homepage&message=Image%20Train%20Filters&color=informational)](http://itf.joaofilipesabinoesperancinha.nl/)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Homepage&message=MancalaJE&color=informational)](http://mancalaje.joaofilipesabinoesperancinha.nl/)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Homepage&message=Project%20Status&color=informational)](https://github.com/jesperancinha/project-signer/blob/master/project-signer-templates/Status.md)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Homepage&message=Badges&color=informational)](https://github.com/jesperancinha/project-signer/blob/master/project-signer-templates/Badges.md)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Google%20Apps&message=Joao+Filipe+Sabino+Esperancinha&color=informational)](https://play.google.com/store/apps/developer?id=Joao+Filipe+Sabino+Esperancinha)
[![Generic badge](https://img.shields.io/static/v1.svg?label=DEV&message=Profile&color=informational)](https://dev.to/jofisaes)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Medium&message=@jofisaes&color=informational)](https://medium.com/@jofisaes)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Free%20Code%20Camp&message=jofisaes&color=informational)](https://www.freecodecamp.org/jofisaes)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Hackerrank&message=jofisaes&color=informational)](https://www.hackerrank.com/jofisaes)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Acclaim%20Badges&message=joao-esperancinha&color=informational)](https://www.youracclaim.com/users/joao-esperancinha/badges)
[![Generic badge](https://img.shields.io/static/v1.svg?label=Code%20Pen&message=jesperancinha&color=informational)](https://codepen.io/jesperancinha)
[![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=ITF%20Chartizate%20Android&color=informational)](https://github.com/JEsperancinhaOrg/itf-chartizate-android)
[![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=ITF%20Chartizate%20Java&color=informational)](https://github.com/JEsperancinhaOrg/itf-chartizate-modules/tree/master/itf-chartizate-java)
[![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=ITF%20Chartizate%20API&color=informational)](https://github.com/JEsperancinhaOrg/itf-chartizate/tree/master/itf-chartizate-api)
[![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=Markdowner%20Core&color=informational)](https://github.com/jesperancinha/markdowner/tree/master/markdowner-core)
[![Generic badge](https://img.shields.io/static/v1.svg?label=GitHub&message=Markdowner%20Filter&color=informational)](https://github.com/jesperancinha/markdowner/tree/master/markdowner-filter)
