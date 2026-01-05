Commands 

Setting up jenkins-Master & Jenkins-Agent 

##Install Java 
$ sudo apt update
$ sudo apt upgrade
$ sudo nano /etc/hostname 
$ sudo init 6
$ sudo apt install openjdk-17-jre 
$ java -version 

##Install Jenkins 
sudo wget -O /etc/apt/keyrings/jenkins-keyring.asc \
  https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key
echo "deb [signed-by=/etc/apt/keyrings/jenkins-keyring.asc]" \
  https://pkg.jenkins.io/debian-stable binary/ | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null
sudo apt update
sudo apt install jenkins

$ sudo systemctl enable jenkins #Enable the jenkins service to start at boot 

$ sudo systemctl start jenkins #Start jenkins as a Service 

$ sudo systemctl status jenkins #Show status 

sudo apt-get install docker.io

$ sudo nano /etc/ssh/sshd_config #PubkeyAuthentication yes & Authorizedkeyfiles 

$ sudo service ssh reload
$ ssh-keygen or $ ssh-keygen -t ed25519 #Generating key pair 

pwd
/home/ubuntu

cd .ssh/ 
ls

#copy pub key 


----------------------------------------------------------------

Setting up Sonarqube 

Update Package repositry & upgrade packages 
$ sudo apt update
$ sudo apt upgrade 

Add PostgresSQL repo 
sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list'

curl -fsSL https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo gpg --dearmor -o /etc/apt/trusted.gpg.d/postgresql.gpg #Importing the repositry signing key 

Install postgresSQL 

$ sudo apt update 
$ sudo apt install postgresql-16 postgresql-contrib-16
$ sudo systemctl start postgresql
$ sudo systemctl enable postgresql

Create database for SonarQube 
$ sudo passwd postgres
$ su -postgres
$ createuser sonar 
$ psql
$ ALTER USER sonar WITH ENCRYPTED password 'sonar';
$ CREATE DATABASE sonarqube OWNER sonar ;
$ \q
$ exit 

Add Adoptium repo 
$ sudo bash 
$ apt install -y wget apt-transport-https gpg
$ wget -qO - https://packages.adoptium.net/artifactory/api/gpg/key/public | gpg --dearmor | tee /etc/apt/trusted.gpg.d/adoptium.gpg > /dev/null
$ echo "deb https://packages.adoptium.net/artifactory/deb $(awk -F= '/^VERSION_CODENAME/{print$2}' /etc/os-release) main" | tee /etc/apt/sources.list.d/adoptium.list

Install Java

$ sudo apt update
$ sudo apt install openjdk-21-jdk
$ java --version

Linux kernel tuning 

#Increase Limits
$ sudo sysctl -p /etc/sysctl.d/99-sonarqube.conf

# Add these lines to /etc/security/limits.d/99-sonarqube.conf
sonar    -    nofile    131072
sonar    -    nproc     8192

#Increase Mapped memory regions 
# Add these lines to /etc/sysctl.d/99-sonarqube.conf
vm.max_map_count=262144
fs.file-max=2097152


Sonarqube Installation 

#Download and Extract 

$ sudo apt install unzip 
$ sudo unzip sonarqube-9.9.0.65466.zip -d/opt
$ sudo mv /opt/sonarqube-9.9.0.65466 /opt/sonarqube

#Create user and set permissions 

$ sudo groupadd sonar
$ sudo useradd -c "user to run SonarQube" -d /opt/sonarqube -g sonar sonar 
$ sudo chown sonar:sonar /opt/sonarqube -R

#Update Sonarqube properities with DB credentials 

$ sudo vim /opt/sonarqube/conf/sonar.properities 
//Find and replace the velow values, you might need to add the sonar.jdbc.url 

sonar.jdbc.username=sonar
sonar.jdbc.password=sonar
sonar.jdbc.url=jdbc:postgresql://locahost:5432/sonarqube 

#Create service for Sonarqube 

$ sudo vim /etc/systemd/system/sonarqube.service 
// post the below into the file 

[Unit]
Description=SonarQube service
After=syslog.target network.target

[Service]
Type=forking 

User=sonarqube
Group=sonarqube
PermissionsStartOnly=true

ExecStart=/bin/nohup /opt/java/bin/java -Xms32m -Xmx32m -Djava.net.preferIPv4Stack=true -jar /opt/sonarqube/lib/sonar-application-25.1.0.102122.jar

StandardOutput=journal

LimitNOFILE=131072
LimitNPROC=8192
TimeoutStartSec=5

Restart=always
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target

#Start Sonarqube and Enable service 

$ sudo systemctl start sonar
$ sudo systemctl enable sonar 
$ sudo systemctl status sonar 

#watch log files and monitor for startup 

$ sudo tail -f /opt/sonarqube/logs/sonag.log 





