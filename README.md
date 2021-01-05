# K8S_BoissierL_BlancA

<h2>Procédure pour la mise en place :</h2>

<h4>Démarrage de minikube :</h4>
                               
> minikube start

<h4>On accède au dashboard de minikube :</h4>

> minikube dashboard # Cela nous permettra d'observer plus simplement nos containers.

---

<h3>Jenkins :</h3>

<h4>Installation :</h4>
On créer notre namespace :

> kubectl create namespace jenkins

On créer notre volume :

> kubectl apply -f volume/jenkins-pv.yaml

On créer notre déploiement et notre service : 

> kubectl create -f jenkins-deployment.yaml --namespace jenkins<br/>
> kubectl create -f jenkins-service.yaml --namespace jenkins

**Attendre que le container soit lancé (Environ 90 secondes)**


<h4>Mot de passe :</h4>
On récupère le nom de notre container :

> kubectl get pods -n jenkins

On récupère le mot de passe administrateur :

> kubectl logs <nom_container> -n jenkins

Vous devriez le trouver entre plusieurs lignes d'étoiles. Garder-le, on en aura besoin pour se connecter à Jenkins.<br/>

<h4>Accès :</h4>
On créer notre tunnel pour accéder à Jenkins :

> minikube service jenkins -n jenkins

Un tableau devrait apparaitre, cliquer sur le deuxième URL, il devrait ressembler à ça : **http://127.0.0.1:<port\>**.<br/>
Une fois sur la page, on nous demande le mot de passe administrateur. C'est celui que l'on a récupéré.

<h4>Configuration :</h4>
Une fois que vous avez rentré le mot de passe administrateur, vous aurez deux choix. Nous vous conseillons de choisir le premier choix "Install suggested plugins".<br/>
Attendre que les plugins s'installent (environ 4-5 minutes) ...

Après l'installation on va pouvoir créer un utilisateur admin pour les futurs connection.

Remplir les informations comme-ci :
- Username : admin
- Password : admin
- Full name : admin
- E-mail address : admin@gmail.com

On arrive enssuite sur la page : "Instance Configuration", on ne va pas changer l'url qui nous est proposé, je vous invites donc à cliquer sur "Save and Finish", puis "Start using Jenkins". <br/>
Et voilà, on a désormais accès à tout Jenkins. On y reviendra plus tard dans le README.

---

<h3>MySQL :</h3>

<h4>Installation :</h4>
On créer notre volume :

> kubectl apply -f volume/mysql-pv.yaml

On créer notre déploiement et nos services :

> kubectl create -f mysql-deployment.yaml --namespace jenkins<br/>
> kubectl create -f mysql-service.yaml --namespace jenkins

<h4>Commande pour se connecter directement à MySQL : (Facultatif)</h4>

> kubectl run -it --rm --image=mysql:5.6 --namespace=jenkins --restart=Never mysql-client -- mysql -h mysql -ppassword

---

<h3>PhpMyAdmin :</h3>

<h4>Installation :</h4>
On créer notre déploiement et nos services :

> kubectl apply -f phpMyAdmin-deployment.yaml --namespace jenkins<br>
> kubectl apply -f phpMyAdmin-service.yaml --namespace jenkins

<h4>Accès :</h4>
On créer le tunnel nécessaire pour accéder à phpMyAdmin :

> minikube service phpmyadmin -n jenkins

Une fois sur la page, vous pourrez vous connecter avec les logins suivants : **root** / **password**

---

<h3>Configuration d'une tâche Jenkins :</h3>

<h4>Installation du plugin :</h4>
Dans le menu de gauche, cliquer sur l'onglet "**Manage Jenkins**", puis cliquer sur "**Manage Plugins**".<br>
Cliquer sur l'onglet "**Available**".<br>
Dans la barre de recherche taper : "**Mysql**" et installer le package MySQL Database et **redémarrer** jenkins ("**Download now and install after restart**")

---

<h3>Pour supprimer nos services :</h3>

> kubectl get service -n jenkins<br/>
> kubectl delete service -n jenkins <nom_service>
