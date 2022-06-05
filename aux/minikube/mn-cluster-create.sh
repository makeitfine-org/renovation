#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2022
#

minikube start -n 2 -p mn

minikube -p mn addons enable volumesnapshots
minikube -p mn addons enable registry
minikube -p mn addons enable metrics-server
minikube -p mn addons enable ingress-dns
minikube -p mn addons enable ingress
minikube -p mn addons enable dashboard

minikube -p mn image load koresmosto/renovation-backend
minikube -p mn image load koresmosto/renovation-frontend-info
minikube -p mn image load koresmosto/renovation-info

#create folders inside minikube ssh
minikube -p mn cp aux/minikube/create-mnt-content.sh mn:/home/docker/create-mnt-content.sh
minikube -p mn ssh <<'ENDSSH'
sudo sh /home/docker/create-mnt-content.sh
exit
ENDSSH

minikube -p mn cp aux/minikube/create-mnt-content.sh mn-m02:/home/docker/create-mnt-content.sh
minikube -p mn -n mn-m02 ssh <<'ENDSSH'
sudo sh /home/docker/create-mnt-content.sh
exit
ENDSSH

kubectl apply -f aux/k8s/yaml/renovation-namespace.yaml
sh aux/k8s/scripts/deploy-all.sh
