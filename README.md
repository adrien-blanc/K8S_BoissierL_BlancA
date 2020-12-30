# K8S_BoissierL_BlancA
cb807df706e7421bb4c582704fe5cac7
http://127.0.0.1:65087/
admin / admin


minikube start

kubectl create namespace jenkins<br/>
kubectl create -f jenkins-deployment.yaml --namespace jenkins<br/>
kubectl create -f jenkins-service.yaml --namespace jenkins

kubectl get pods -n jenkins # On récupère le nom de notre container<br/>
kubectl logs <nom_container> -n jenkins # On récupère le mot de passe<br/>
minikube service jenkins -n jenkins # On créer le tunnel minikube


minikube dashboard
