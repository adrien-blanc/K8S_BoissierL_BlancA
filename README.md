# K8S_BoissierL_BlancA
cb807df706e7421bb4c582704fe5cac7
http://127.0.0.1:65087/
admin / admin

<h2>Procédure pour la mise en place :</h2>

<h3>Démarrage de minikube :</h3>
                               

> minikube start

<h3>Jenkins :</h3>

> kubectl create namespace jenkins<br/>
> kubectl apply -f volume/jenkins-pv.yaml<br/>
> kubectl create -f jenkins-deployment.yaml --namespace jenkins<br/>
> kubectl create -f jenkins-service.yaml --namespace jenkins

**Attendre que le container soit lancé (Environ 90 secondes)**

> kubectl get pods -n jenkins # On récupère le nom de notre container<br/>
> kubectl logs <nom_container> -n jenkins # On récupère le mot de passe administrateur.<br/>
> minikube service jenkins -n jenkins # On créer le tunnel minikube

Un tableau devrait apparaitre, cliquer sur le deuxième URL, il devrait ressembler à ça : **http://127.0.0.1:<port>**.
Une fois sur la page, on nous demande le mot de passe administrateur. C'est celui que l'on a récupéré 
  
<h4>On accède au dashboard de minikube :</h4>
minikube dashboard # Cela nous permettra d'observer plus simplement nos containers.

<h3>MySQL :</h3>
> kubectl apply -f volume/mysql-pv.yaml<br/>
> kubectl create -f mysql-deployment.yaml --namespace jenkins<br/>

<h4>Voir les informations de deployment :</h4>
> kubectl describe deployment mysql

<h4>Voir le pod :</h4>
> kubectl get pods -l app=mysql

<h4>Inspecter le volume :</h4>
> kubectl describe pvc mysql-pv-claim

<h4>Commande pour run le client MySQL qui se connecte au serveur :</h4>
> kubectl run -it --rm --image=mysql:5.6 --restart=Never mysql-client -- mysql -h mysql -ppassword


<h3>Pour supprimier nos services :</h3>

> kubectl get service -n jenkins<br/>
> kubectl delete service -n jenkins <nom_service>
