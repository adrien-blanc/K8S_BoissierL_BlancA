# K8S_BoissierL_BlancA
cb807df706e7421bb4c582704fe5cac7
http://127.0.0.1:65087/
admin / admin


minikube start

kubectl create namespace jenkins
kubectl create -f jenkins-deployment.yaml --namespace jenkins
kubectl create -f jenkins-service.yaml --namespace jenkins

kubectl get pods -n jenkins # On récupère le nom de notre container
kubectl logs <nom_container> -n jenkins # On récupère le mot de passe

minikube service jenkins -n jenkins # On créer letunnel minikube

minikube dashboard
