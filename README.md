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

## Building the server yourself
If you don't want to use the prebuilt .jar files or want to modify the server before building, you can do so using Maven. Just clone the repo, make your changes, then run
```
mvn package
```
Make sure you have installed Java 11+ correctly before trying to build
