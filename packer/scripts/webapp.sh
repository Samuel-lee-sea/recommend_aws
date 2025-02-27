set -e
sudo apt-get update -y
sudo apt-get install -f -y 
sudo apt-get install -y openjdk-17-jdk openjdk-17-jre
java -version
sudo apt install -y nginx
sudo systemctl enable nginx
sudo mv /tmp/nginx.conf /etc/nginx/nginx.conf
sudo systemctl restart nginx
