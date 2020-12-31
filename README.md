# K8S_BoissierL_BlancA

<h2>Procédure pour la mise en place :</h2>

<h3>Démarrage de minikube :</h3>
                               
> minikube start

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
Une fois que vous avez rentré le mot de passe administrateur, vous aurez deux choix. Nous vous conseillons de choisir le premier choix "Installation par défaut".<br/>
Attendre que les plugins s'installent ...

  
<h4>On accède au dashboard de minikube :</h4>

> minikube dashboard # Cela nous permettra d'observer plus simplement nos containers.

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

> minikube service phpmyadmin-service -n jenkins

Une fois sur la page, vous pourrez vous connecter avec les logins suivants : **root** / **password**

---

<h3>Pour supprimer nos services :</h3>

> kubectl get service -n jenkins<br/>
> kubectl delete service -n jenkins <nom_service>
