# K8S_BoissierL_BlancA

<h1>Introduction :</h1>

Avant de rentrer dans le vif du sujet, voici une courte présentation de notre projet :

Scénario (Use Case):
Lors de la création d'une application à l'aide de l'outil Jenkins, les équipes travaillants sur ce sujet aimeraient savoir si l'application écrit bien dans leur BDD. Afin de répondre à cette interogation, ils leur suffit de lancer un job Jenkins qui réalisera les opérations voulus (test d'écriture dans la base de données). Cette bonne pratique d'automatiser les tâches rentre bien dans les moeurs des dernieres années, qui consiste à se rapprocher le plus possible d'une **infrastructure as code**. <br>

Afin de réaliser cela, nous avons mis en place:
- un serveur MySQL (DataBase)
- PhpMyAdmin (App Web gestion BDD)
- un serveur Jenkins (Outil d'Intergration Continue)
- un client MySQL (Pod temporaire pour faire des requêtes sur la BDD)

Explication :
L'objectif est d'avoir un job Jenkins qui va écrire dans la base de données MySQL. Pour cela il existe des plugins à installer sur Jenkins afin de mettre en lien des services. 
Dans notre cas, nous avons installé le plugin Kubernetes pour que le serveur Jenkins pilote Minikube. Pour quel usage ? Tout simplement pour instancier (lors d'un job jenkins) un pod client MySQL qui écrirat dans notre BDD (création basique d'une base de données). 

---

<h1>Procédure pour la mise en place :</h1>

<h3>Démarrage de minikube :</h3>
                               
> minikube start

<h3>On accède au dashboard de minikube :</h3>

> minikube dashboard # Cela nous permettra d'observer plus simplement nos containers.

Il va falloir lancer un nouvel invite de commande et laisser celui-là ouvert.

---

<h2>Jenkins :</h2>

<h3>Installation :</h3>
On créer notre namespace :

> kubectl create namespace jenkins

On créer notre volume :

> kubectl apply -f volume/jenkins-pv.yaml

On créer notre déploiement et notre service : 

> kubectl create -f jenkins-deployment.yaml --namespace jenkins<br/>
> kubectl create -f jenkins-service.yaml --namespace jenkins

**Attendre que le container soit lancé (Environ 90 secondes)**

On créer notre service account (On en aura besoin plus tard pour mettre en lien Kubernetes et Jenkins) :

> kubectl create serviceaccount jenkins --namespace=jenkins <br/>
> kubectl create rolebinding jenkins-admin-binding --clusterrole=admin --serviceaccount=jenkins:jenkins --namespace=jenkins<br/>


<h3>Mot de passe :</h3>
On récupère le nom de notre container :

> kubectl get pods -n jenkins

On récupère le mot de passe administrateur :

> kubectl logs <nom_container> -n jenkins

Vous devriez le trouver entre plusieurs lignes d'étoiles. Garder-le, on en aura besoin pour se connecter à Jenkins.<br/>

<h3>Accès :</h3>
On créer notre tunnel pour accéder à Jenkins :

> minikube service jenkins -n jenkins

Il va falloir lancer un nouvel invite de commande et laisser celui-là ouvert.

Un tableau devrait apparaitre, cliquer sur le deuxième URL, il devrait ressembler à ça : **http://127.0.0.1:<port\>**.<br/>
Une fois sur la page, on nous demande le mot de passe administrateur. C'est celui que l'on a récupéré.

<h3>Configuration :</h3>
Une fois que vous avez rentré le mot de passe administrateur, vous aurez deux choix. Nous vous conseillons de choisir le premier choix "Install suggested plugins".<br/>
Attendre que les plugins s'installent (environ 4-5 minutes) ...

Après l'installation on va pouvoir créer un utilisateur pour les futurs connexions.

Remplir les informations comme-ci :
- Username : **admin**
- Password : **admin**
- Full name : **admin**
- E-mail address : **admin@gmail.com**

On arrive enssuite sur la page : **"Instance Configuration"**, on ne va pas changer l'url qui nous est proposé, je vous invite donc à cliquer sur "Save and Finish", puis "Start using Jenkins". <br/>
Et voilà, on a désormais accès à tout Jenkins. On y reviendra plus tard dans le README.

---

<h2>MySQL :</h2>

<h3>Installation :</h3>
On créer notre volume :

> kubectl apply -f volume/mysql-pv.yaml

On créer notre déploiement et nos services :

> kubectl create -f mysql-deployment.yaml --namespace jenkins<br/>
> kubectl create -f mysql-service.yaml --namespace jenkins

<h4>Commande pour se connecter directement à MySQL : (Facultatif)</h4>

> kubectl run -it --rm --image=mysql:5.6 --namespace=jenkins --restart=Never mysql-client -- mysql -h mysql -ppassword

---

<h2>PhpMyAdmin :</h2>

<h3>Installation :</h3>
On créer notre déploiement et nos services :

> kubectl apply -f phpMyAdmin-deployment.yaml --namespace jenkins<br>
> kubectl apply -f phpMyAdmin-service.yaml --namespace jenkins

<h3>Accès :</h3>
On créer le tunnel nécessaire pour accéder à phpMyAdmin :

> minikube service phpmyadmin -n jenkins

Il va falloir lancer un nouvel invite de commande et laisser celui-là ouvert.

Une fois sur la page, vous pourrez vous connecter avec les logins suivants : **root** / **password**

---

<h2>Configuration d'un job Jenkins :</h2>

 <h3>Installation des plugins :</h3>

Dans le menu de gauche, cliquer sur l'onglet **"Manage Jenkins"**, puis cliquer sur **"Manage Plugins"**.<br>
Cliquer sur l'onglet "**Available**".<br>
Dans la barre de recherche taper : **"Mysql"** et cocher **MySQL Database**.
Faite de même pour le plugin **"Kubernetes"**. Installer les packages et **redémarrer** Jenkins (**"Download now and install after restart"**) (Attention, poarfois le restart ne ce fait pas bien, si vous voyez que tout a été installé et que rien ne se passe, cocher la case **Restart Jenkins** en bas de la page.) Ps : n'oubliez pas que pour vous reconnecter à jenkins vos login sont **admin / admin**.

<h3>Configuration du plugin MySQL (test de connexion à notre BDD) :</h3>

Ce plugin permet uniquement de tester la connexion à la BDD, il a pour unique but de prouver que les deux pods Jenkins & MySQL communiquement (objectif dans le cahier des charges).<br>

On retourne dans "**Manage Jenkins**" > "**Configure System**"<br>
Se rendre tout en bas de la page dans l'onglet : "**Global Database**" et sélectionner **MySQL**.<br> 

Rentrer les informations suivantes :
- Host Name : **mysql**
- Database : **mysql**
- Username : **root**
- Password : **password**
- Validation Query : **SELECT 1**

Vous pouvez vérifier la connectivité en appuyant sur le bouton "**Test Connection**", un **OK** devrait apparaître.

Cliquer sur **Save**.

<h3>Configuration du plugin Kubernetes (réalisation d'un job modifiant la BDD):</h3>

Premierement, aller sur le Dashboard afin de récuperer le Token de notre secret jenkins : 

Onglet Secrets > jenkins-token-xxxx > cliquer dans la partie Données sur "token" et copier le secret dans votre presse papier.
 
On retourne dans "**Manage Jenkins**" puis "**Manage Nodes**" et enfin "**Configure Clouds**"<br> 
Cliquer sur "**Ajouter un nouveau cloud**" et selectionner **Kubernetes**
Deplier les informations en cliquant sur "**Kurbernetes Cloud Details**" puis completer les champs suivants :
- Name : **kubernetes**
- Kubernetes URL : **https://IP_Minikube:8443** <br>

> Pour trouver l'adresse de minikube, sur le **Dasboard** rendez-vous dans l'onglet **"Nodes"**, cliquez sur **minikube** et vous devriez voir l'adresse IP dans le panneau **Resource information** (InternalIP : ).<br>

>Pour ce qui est du port il devrait toujours être le même, mais si cela ne fonctionne pas, rendez-vous sur l'application **Docker desktop**, cliquez sur votre instance de minikube, rendez vous dans **Inspect** et vous devriez voir les ports disponibles (Le dernier de la liste devrait l'être).

- Kubernetes Namespace : **jenkins**
- Credential : <br>
    > Ajouter > Jenkins<br>
    > Kind : Secret Text<br>
    > Secret : Coller le Token copié précédemment <br>
    > ID : serviceaccount_jenkins<br>
    > Ajouter<br>
    > Selectionner dans la liste deroulante le Credential nommé serviceaccount_jenkins

On peut dès à present tester la connexion en cliquant sur le bouton **"Test Connection"**. Le résultat doit être le suivant : *Connected to Kubernetes vx.xx.x*

Pour trouver les deux IPs suivantes, rendez-vous sur le **Dashboard** dans l'onglet **"Services"**.

- Jenkins URL : http://IP_frontend:8080 (Correspond au service : **jenkins**)
- Jenkins tunnel : IP_jenkins-jnlp:50000 (Correspond au service : **jenkins-jnlp** | ! Attention ! Ne pas mettre le **http://** pour celui-ci.)
- Pour le reste, laisser les informations par defaut.

Cliquer sur Apply, puis Save.

<h3>Configuration du job jenkins :</h3>
Dernière étape ! Sur le menu principal, aller sur **New Item** > nommer votre job > Pipeline > OK > Onglet Pipeline <br>

Dans la zone de texte, coller le contenu du script **job_pipeline.groovy** se trouvant sur le repository Github.<br>
Apply puis Save.

Dans le menu de gauche cliquer sur **"Build Now"**, le job est en train de se lancer. Pendant ce temps nous vous invitons grandement à regarder le **Dashboard**, vous devriez voir un nouveau pod se lancer, c'est un client mysql dans lequel on éxecute la commande inscrite dans le script groovy (création d'une Database). Après quelques minutes le pod devrait s'éteindre, vous pouvez dès à présent vous rendre sur la page PhpMyAdmin et vérifier qu'une nouvelle database a été créée.

---
