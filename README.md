# SPID-bundle
SPID bundle per Entando 7

## Description
This project creates a bundle that enables SPID authentication in Entando.

There are three sections:
 - **installer**  
This is the JAVA project containing the code to perform the installation of the SPID provider in Keycloack


 - **docker**  
This is simply the Dockerfile to create the image that will be executed in Kubernets


 - **kubernets**  
This contains all the manifests needed in order to run the installer job

## Build

  - login to your Dockerhub account
  - launch the script to compile and upload the docker image for the job  
```shell
build.sh -d<DOCKERHUB_ACCOUNT> -v<VERSION_TAG>
```
Remeber to adjust the  `kubernetes/installer-job.yaml ` accordingly
 
