# Secret-Reichstag-Server
Secret Reichstag Server

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.

This project is a web version of a modified version of the game ["Secret Hitler"](https://www.secrethitler.com/)

## Running your own server
Prebuilt .jar files are available under the [releases](https://github.com/MrLetsplay2003/Secret-Reichstag-Server/releases) section

After you've downloaded or built a server JAR, you can run it using Java 11 or higher. Upon first launch, the server will generate a `config.yml` file which you can edit. The default configuration looks like this:

```yaml
### CustomConfig version: 2.0
ssl: 
  enable: false
  certificate-path: "pem/cert.pem"
  private-key-path: "pem/privkey.pem"
  certificate-password: ""
server: 
  insecure: 
    host: "0.0.0.0"
    port: 34642
  secure: 
    host: "0.0.0.0"
    port: 34643
```
You can also create the config file before running the server

### Enabling SSL
The server supports using secure websockets (`wss://`) instead of insecure (`ws://`) ones. To enable them, make sure you have a valid certificate and private key for your domain name and edit the paths in the config accordingly. Optionally, you can enter the certificate's password (if it has one). Then, you can set `ssl.enable` to `true` and you should be able to connect to your server using `wss://your.domain:port`

### Setting up a proxy using Apache2
If you want to integrate the server into your existing domain/webpage configuration using Apache2, you can do so by using a proxy. Please keep in mind that these instructions apply to using Apache2 on Linux. If you're running it on Windows, you might need to adapt some of the steps.

#### Using an existing domain
To use an existing (sub-)domain for your server, you need to edit the site's config file and add the following lines to its `VirtualHost`
```apacheconf
	# For insecure WebSockets. If you're using secure websockets, disable this
	ProxyPass "/reichstag" "ws://localhost:34642"
	
	# For secure WebSockets, enable these
	# Note: Internally, this will always use insecure websockets. You can change this if you want
	# SSLProxyEngine On
	# ProxyPass "/reichstag" "ws://localhost:34642"
	# SSLCertificateFile /path/to/your/certificate.pem
	# SSLCertificateKeyFile /path/to/your/privatekey.pem
```
Make sure you've enabled the correct modules for Apache using
```
a2enmod proxy proxy_http proxy_wstunnel
```
then restart Apache using
```
systemctl restart apache2.service
```
If everything went well, you should now be able to connect to your server using `ws://your.domain/reichstag` or `wss://your.domain/reichstag` (depending on whether you're using secure WebSockets) in the app

#### Using a dedicated subdomain
To use a dedicated subdomain for the server (e.g. `sr.your.domain`), you first need to create a new site configuration.

Under `/etc/apache2/sites-available/` create a new configuration file (e.g. `secretreichstag.conf`) with the following contents:
```apacheconf
<VirtualHost *:443>
	ServerName sr.your.domain

	ServerAdmin webmaster@your.domain
  
	# You only need to set the DocumentRoot if you want to host the web version as well
	# For more instructions, see the README of https://github.com/MrLetsplay2003/Secret-Reichstag-Web
	# DocumentRoot /var/www/secretreichstag

	ErrorLog ${APACHE_LOG_DIR}/error.log
	CustomLog ${APACHE_LOG_DIR}/access.log combined
	
	# For insecure WebSockets. If you're using secure websockets, disable this
	ProxyPass "/" "ws://localhost:34642"
	
	# For secure WebSockets, enable these
	# Note: Internally, this will always use insecure websockets. You can change this if you want
	# SSLProxyEngine On
	# ProxyPass "/" "ws://localhost:34642"
	# SSLCertificateFile /path/to/your/certificate.pem
	# SSLCertificateKeyFile /path/to/your/privatekey.pem
</VirtualHost>
```

Before enabling the new site, you need to make sure you've enabled the right modules for Apache2 to proxy the WebSockets. To do so, use
```
a2enmod proxy proxy_http proxy_wstunnel
```
You can then enable the site using
```
a2ensite secretreichstag
```
and restart Apache using
```
systemctl restart apache2.service
```
Make sure you have a valid DNS entry for the new subdomain pointing to your server.

If everything went well, you should now be able to connect to your server using `ws://sr.your.domain` or `wss://sr.your.domain` (depending on whether you're using secure WebSockets) in the app

## Building the server yourself
If you don't want to use the prebuilt .jar files or want to modify the server before building, you can do so using Maven. Just clone the repo, make your changes, then run
```
mvn package
```
Make sure you have installed Java 11+ correctly before trying to build
