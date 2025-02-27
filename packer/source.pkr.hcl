source "amazon-ebs" "ubuntu" {
  ami_name      = "my-webapp-ami-{{timestamp}}"      # AMI name with timestamp
  instance_type = "t2.micro"                         # EC2 instance type for building the AMI
  region        = "us-west-2"                        # AWS region
  source_ami    = "ami-00c257e12d6828491"            # Ubuntu 24.04 AMI ID
  ssh_username  = "ubuntu"                           # Default SSH username for Ubuntu
  associate_public_ip_address = true                # Public IP for the instance
}