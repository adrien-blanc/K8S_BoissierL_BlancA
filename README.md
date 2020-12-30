# K8S_BoissierL_BlancA
cb807df706e7421bb4c582704fe5cac7
http://127.0.0.1:65087/
admin / admin

#Procédure pour la mise en place :

##Démarrage de minikube :

> minikube start

##Jenkins :

> kubectl create namespace jenkins<br/>
> kubectl apply -f volume/jenkins-pv.yaml --namespace jenkins<br/>
> kubectl create -f jenkins-deployment.yaml --namespace jenkins<br/>
> kubectl create -f jenkins-service.yaml --namespace jenkins

**Attendre que le container soit lancé (Environ 90 secondes)**

> kubectl get pods -n jenkins # On récupère le nom de notre container<br/>
> kubectl logs <nom_container> -n jenkins # On récupère le mot de passe administrateur.<br/>
> minikube service jenkins -n jenkins # On créer le tunnel minikube

Un tableau devrait apparaitre, cliquer sur le deuxième URL, il devrait ressembler à ça : **http://127.0.0.1:<port>**.
Une fois sur la page, on nous demande le mot de passe administrateur. C'est celui que l'on a récupéré 

##On accède au dashboard de minikube :
minikube dashboard # Cela nous permettra d'observer plus simplement nos containers.


##Pour supprimier nos services :

kubectl get service -n jenkins<br/>
kubectl delete service -n jenkins <nom_service>
