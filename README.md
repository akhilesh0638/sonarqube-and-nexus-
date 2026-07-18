# Java Tomcat WebApp — CI/CD with GitHub Actions

A sample Maven-based Java Servlet + JSP web application, packaged as a `.war`
file and auto-deployed to an Apache Tomcat server on every push to `main`
using GitHub Actions.

## Project structure

```
java-tomcat-webapp/
├── pom.xml
├── src/main/java/com/akhilesh/app/HelloServlet.java
├── src/main/webapp/index.jsp
├── src/main/webapp/WEB-INF/web.xml
└── .github/workflows/deploy.yml
```

## 1. Prerequisites on the Tomcat server (EC2 / any Linux VM)

1. Launch an EC2 instance (Amazon Linux 2 / Ubuntu) and open port `8080` (Tomcat)
   and `22` (SSH) in its security group.
2. Install Java + Tomcat:
   ```bash
   sudo yum install -y java-11-amazon-corretto      # Amazon Linux
   # or: sudo apt install -y openjdk-11-jdk          # Ubuntu

   cd /opt
   sudo curl -O https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.90/bin/apache-tomcat-9.0.90.tar.gz
   sudo tar -xzf apache-tomcat-9.0.90.tar.gz
   sudo mv apache-tomcat-9.0.90 tomcat
   sudo chmod +x /opt/tomcat/bin/*.sh
   ```
3. Create a systemd service so Tomcat can be started/stopped/restarted with
   `systemctl` (needed by the deploy step):
   ```bash
   sudo tee /etc/systemd/system/tomcat.service > /dev/null <<'EOF'
   [Unit]
   Description=Apache Tomcat
   After=network.target

   [Service]
   Type=forking
   Environment=JAVA_HOME=/usr/lib/jvm/java-11-amazon-corretto
   Environment=CATALINA_HOME=/opt/tomcat
   ExecStart=/opt/tomcat/bin/startup.sh
   ExecStop=/opt/tomcat/bin/shutdown.sh
   User=ec2-user
   RemainAfterExit=yes

   [Install]
   WantedBy=multi-user.target
   EOF

   sudo systemctl daemon-reload
   sudo systemctl enable tomcat
   sudo systemctl start tomcat
   ```
4. Make sure the user you SSH in as (e.g. `ec2-user`) can run
   `sudo systemctl` for Tomcat without a password prompt (or adjust the
   sudoers file), since the workflow calls `sudo systemctl stop/start tomcat`.
5. Create the temp folder used for the SCP step:
   ```bash
   mkdir -p /tmp/deploy
   ```

## 2. GitHub repository setup

1. Push this project to a new GitHub repo (see commands below).
2. Go to **Settings → Secrets and variables → Actions → New repository secret**
   and add:

   | Secret name       | Value                                                   |
   |-------------------|----------------------------------------------------------|
   | `TOMCAT_HOST`     | Public IP or DNS of your EC2 instance                    |
   | `TOMCAT_USER`     | SSH username (e.g. `ec2-user` or `ubuntu`)               |
   | `TOMCAT_SSH_KEY`  | Full contents of your EC2 `.pem` private key             |
   | `TOMCAT_PORT`     | `22` (or your custom SSH port)                           |

## 3. Push the code

```bash
git init
git add .
git commit -m "Initial commit: Java Tomcat webapp with GitHub Actions CI/CD"
git branch -M main
git remote add origin https://github.com/akhileshgajulaa/<your-repo-name>.git
git push -u origin main
```

Pushing to `main` automatically triggers `.github/workflows/deploy.yml`.

## 4. What the pipeline does

1. Checks out your code.
2. Sets up JDK 11 (Temurin) with Maven caching.
3. Runs `mvn clean package` to produce `target/java-tomcat-webapp.war`.
4. Uploads the WAR as a workflow artifact (viewable under the Actions run).
5. Copies the WAR to the EC2 instance via SCP (`appleboy/scp-action`).
6. SSHes in (`appleboy/ssh-action`), stops Tomcat, replaces the old WAR/exploded
   folder, copies the new WAR into `webapps/`, and restarts Tomcat.

## 5. Verify the deployment

Watch the run under the **Actions** tab of your repo. Once it's green, visit:

```
http://<TOMCAT_HOST>:8080/java-tomcat-webapp/
```

You should see the landing page, and `http://<TOMCAT_HOST>:8080/java-tomcat-webapp/hello`
should hit the servlet.

## 6. Local build/test (optional)

```bash
mvn clean package
# WAR file is generated at target/java-tomcat-webapp.war
```

To test locally, drop the WAR into a local Tomcat's `webapps/` folder and
start Tomcat, or run it with an embedded plugin like `tomcat7-maven-plugin`.

## Notes

- The Tomcat webapp context path will be `/java-tomcat-webapp` (from the WAR's
  filename, set via `<finalName>` in `pom.xml`). Rename `finalName` in
  `pom.xml` if you want a different context path (e.g. `ROOT` to deploy at `/`).
- If your Tomcat runs on a different install path than `/opt/tomcat`, update
  the paths in `deploy.yml` accordingly.
- Never commit real SSH keys or credentials to the repo — they belong only in
  GitHub Secrets, exactly as set up in step 2.
