# Implémentation de l'architecture microservices en utilisant Java Spring Boot, Eureka, Swagger-ui et MySQL.

Ce projet a pour objectif de mettre en oeuvre les différentes techniques enseignées dans le cadre du cours Architecture Microservices dispensé en MIAGE IF DAUPHINE.
* Auteurs :
* DJILANI Amira
* OUAZZANI CHAHDI Nizar

### Prerequis

Disposer de l’application Docker Desktop. Elle permettra la création des images docker et l’instanciation des containers. 
Les ports utilisés par les différentes composantes du projet doivent être disponibles pour pouvoir être utilisé (3306, 8761, 8302).

* Instancier un container mysql via la commande :

```
docker run -p 3306:3306 --name=mysql_db --env="MYSQL_ROOT_PASSWORD=root" --env="MYSQL_PASSWORD=root" --env "MYSQL_USER=user" --env="MYSQL_DATABASE=db" mysql:5.6
```

* Créer une image pour le registre Eureka, il faut préalablement avoir le fichier .jar du projet registre-eureka via la commande : 

```
mvn package compile 
```

Ensuite, lancer la commande : 

```
docker build . -t registre_eureka
```

* Compiler les projets des services gestion de comptes et gestion des transactions.

