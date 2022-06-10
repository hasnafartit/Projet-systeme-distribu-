# Projet-systeme-distribué





L'objectif de note projet et de créer un système distribué basé sur les micro services permettant de gérer les factures des clients .

voici l'archetecture de notre projet :
 
![image](https://user-images.githubusercontent.com/84719124/173114217-fbec351b-948a-473a-928e-e06efd5140e7.png)


Dans ce projet on a creer 5 micro-service : 

1-	Customer-Service : ce service permet de gérer les clients 
On a commencé par créer la classe Customer où on a défini les attribut d’un customeer 
On a ajouté l’annotation  ‘@Entity’ pour qu’il soit une entité jpa et on ajouté @Data pour définit les getters et les setters.
 


Puis l’interface CustomerRepository qui hérite de JpaRepository qui prend le nom de la table customer et son clé primaire et elle permet de faire des modifications sur la base de données.
Puis on a ajouté l’annotation @RepositoryRestResource pour dire à spring dataRest que tous les méthodes hérité de Jpa respository sont accessible via un web service 

![image](https://user-images.githubusercontent.com/84719124/173114881-320a4735-3e45-4a17-ba52-b88f7513bcbd.png)

 

Exemples :
Base de données :

![image](https://user-images.githubusercontent.com/84719124/173114908-4bf797f6-9b4b-409a-92d3-879f7be2f35a.png)

 
GET :

![image](https://user-images.githubusercontent.com/84719124/173114923-59591c0a-0f3d-4afd-a925-410d0ae97138.png)

  

Modifier :

![image](https://user-images.githubusercontent.com/84719124/173114941-0d1643d8-0847-4dc9-a404-96238c8fdd97.png)

  
Etc.

2-	Product-Service : qui permet de gérer les produits qui a même principe du 1er service :
 
![image](https://user-images.githubusercontent.com/84719124/173114964-a9ad3861-4564-4616-8ec3-ec41de685329.png)


 

Exemples :
 Base de données :
 
 ![image](https://user-images.githubusercontent.com/84719124/173114994-ee3b08b7-3ca2-48b4-a4d7-2c9f93cbdf7c.png)


Afficher :
 
![image](https://user-images.githubusercontent.com/84719124/173115018-af773b7c-fed4-4239-a7a6-499014ce5981.png)

![image](https://user-images.githubusercontent.com/84719124/173115064-985b8c0c-b0c4-4967-a295-5f0349fa99c1.png)

 

Ajouter :

![image](https://user-images.githubusercontent.com/84719124/173115082-29d9d0d7-9faf-4f3c-9dc3-b9016466467e.png)

 
Etc.

3-	Gatway-service : qui va jouer le rôle d’un intermédiaire entre le client et le micro service qu’il va répondre à ses besoins. C-à-d le service gateway reçoit une requête http qui porte le nom de service désiré est il redirige le client vers le service qu’il va traiter cette requête(qu’il va répondre aux besoins du client).  
Dans ce service on peut utiliser soit une configuration statique soit dynamique :
-	Configuration statique :  on indique la route(uri,path,…) de chaque micro service il y a 2 méthodes soit utilisation un ficher de configuration soit l’utilisation d’une classe de configuration en utilisant ‘RouteLocator’ et ‘RouteLocatorBuilder’
Exemples : 
Fichier de configuration :
 
 ![image](https://user-images.githubusercontent.com/84719124/173115124-fd1968f2-389f-47c3-9c1c-844616a0cadc.png)


Classe de configuration :
 
 ![image](https://user-images.githubusercontent.com/84719124/173115141-7ec54168-6a04-4d86-9ab7-41588be73337.png)


Test :

products:

![image](https://user-images.githubusercontent.com/84719124/173115162-a63c49ed-7cfc-49ec-a2d0-8448f4ffa50a.png)

customers :

![image](https://user-images.githubusercontent.com/84719124/173115205-e9d9d235-168c-41ec-864f-682ae2c48743.png)



-	Configuration dynamique : ici on n’indique pas les noms des micro services et ses routes dans le fichier de configuration. Dans cette configuration notre micro-service lorsqu’il reçoit une requête qui contient le nom du micro service qu’on veut le service gateway va tout simplement se communiquer avec un service d’enregistrement et il va prendre l’adresse IP et le port du micro service qui est dans la requête http. 
 
 ![image](https://user-images.githubusercontent.com/84719124/173115331-c725ecb1-0170-4c83-82ff-867003b8fca5.png)


Test 

products :

![image](https://user-images.githubusercontent.com/84719124/173115381-a7d369cb-b9dd-4867-a98d-539ef5b67303.png)

customers :


![image](https://user-images.githubusercontent.com/84719124/173115419-c88f8f35-89e3-4493-8742-2b967a2913c2.png)
 

4-	Eureka-service : service d’enregistrement 
Dans la classe principale on ajoute l’annotation @EnableEurekaServer


![image](https://user-images.githubusercontent.com/84719124/173115468-9e7f8e15-35d4-44a3-a5f4-2a1386dbc0a3.png)

 
Exécution :
  
![image](https://user-images.githubusercontent.com/84719124/173115489-f9a78e43-a01e-48f9-a680-6018a40717fa.png)


5-	Billing-service : qui permet de gérer les factures, ce service va se comminuqué avec le service customer et le inventery service pour qu’il puisse avoir les information des produits et des clients.
Tout d’abord on a définit les classe qui ont relation avec la base de données :
		Table bill :

 
 ![image](https://user-images.githubusercontent.com/84719124/173115534-6ae3f6b7-1137-4a8d-a076-6375da749cab.png)


![image](https://user-images.githubusercontent.com/84719124/173115573-e04e00c5-e711-41c9-bb8b-67121a8e5829.png)



Table ProductItem :
 
 ![image](https://user-images.githubusercontent.com/84719124/173115677-dd6e430f-f7f6-4d18-b00e-14e71ccefd70.png)
 
 Puis on a défini 2 classes Product et Customer mais ils ne sont pas des entités jpa puisque leurs bases de données sont dans des autres micro services

![image](https://user-images.githubusercontent.com/84719124/173115736-548d048e-b351-48e3-a4d5-7f5c7f5ed0a3.png)


    
![image](https://user-images.githubusercontent.com/84719124/173115760-deff3a0e-5870-4ba9-9a3b-99e8a606db88.png) 

 Après on a créé puis on a ajouté 2 interfaces avec l’annotation @FeignClient pour qu’il puisse récupérer les données du customer-service et de imventory-service 
 
![image](https://user-images.githubusercontent.com/84719124/173115819-155db001-7b8b-49c4-a060-387107992557.png)
 


  
 ![image](https://user-images.githubusercontent.com/84719124/173115846-8f1862f5-f836-472c-82ae-9b2764527148.png)




Et on a ajouté des données :

![image](https://user-images.githubusercontent.com/84719124/173115869-192b6d90-c9e8-4431-b67f-04ed15d7ab73.png)
 

Affichage :
Table bill :
 
 ![image](https://user-images.githubusercontent.com/84719124/173115911-620d9b9b-9f48-4ed1-945b-36bf0a28c0ce.png)


Table productItem :

![image](https://user-images.githubusercontent.com/84719124/173115898-96e598e2-acf3-481a-9ceb-86e91cfe0f82.png)
 
 Puis on a crée un Rest Controller qui retourne les détails d’une facture :
 
 
![image](https://user-images.githubusercontent.com/84719124/173115929-26221727-b23a-422c-ac69-e049e6fd3ef4.png)


Résultat :  


![image](https://user-images.githubusercontent.com/84719124/173115973-3bb2e68d-3a51-4687-ab4e-028d2bec3bde.png)


Et enfin on a créer un service avec le broker kafka (Kafka offre un cluster de brokers qui permet d'echanger les messages entre les applications)

Pour demarer kafka il faut tout d'abord demarer zookeeper qui fait la coordination entre les instance de kafka et puis demarer le serveur kafka

![image](https://user-images.githubusercontent.com/84719124/173118201-527595e3-670a-4404-83a4-840be6a4f28b.png)

on a creé un producer en utilisant la fonction Supplier qui permet de publier dans un topic FACTURATON

(ici on a lire les factures par un consumer sur cmd)

![image](https://user-images.githubusercontent.com/84719124/173119737-d05c0d70-e91b-4b32-a234-55459dbde2f1.png)

  
Puis on a creer un consumer (avec Consumer) qui lire les factures envoyés par le topic FACTURATON et les enregistrer dans la base des données

![image](https://user-images.githubusercontent.com/84719124/173119926-16db15cf-6855-441e-a8b8-107170f0a9a1.png)




 


