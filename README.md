# K8S_BoissierL_BlancA
cb807df706e7421bb4c582704fe5cac7
http://127.0.0.1:65087/
admin / admin


minikube start

kubectl create namespace jenkins
kubectl create -f jenkins-deployment.yaml --namespace jenkins
kubectl create -f jenkins-service.yaml --namespace jenkins

minikube service jenkins -n jenkins

kubectl create -f jenkins.yaml --namespace jenkins # On récupère le nom de notre container
kubectl logs <nom_container> -n jenkins # On récupère le mot de passe

minikube dashboard
