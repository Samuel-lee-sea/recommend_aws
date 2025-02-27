packer {
  required_plugins {
    amazon = {
      version = "~> 1.0"
      source  = "github.com/hashicorp/amazon"
    }
  }
}


# Define the build steps to install dependencies and copy the app
build {
  sources = ["source.amazon-ebs.ubuntu"]

  provisioner "file" {
    source      = "webapp.jar"
    destination = "/home/ubuntu/webapp.jar"
  }

  provisioner "file" {
    source      = "nginx.conf"
    destination = "/tmp/nginx.conf"
  }

  provisioner "shell" {
    script = "scripts/webapp.sh"
  }

}