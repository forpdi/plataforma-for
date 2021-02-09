
![PlataformaFor_logo](http://teste.forpdi.org/images/plataforma-for-logo.svg)

Plataforma Open Source para:

- Criação, gerenciamento, acompanhamento e divulgação de Planos de Desenvolvimento Institucional (PDI).
- Criação, gerenciamento, acompanhamento e divulgação de Planos de Gestão de  Riscos.

Website Plataforma FOR: [https://www.unifal-mg.edu.br/plataformafor/](https://www.unifal-mg.edu.br/plataformafor/)

**Table of Contents**

- [Instalação Rápida - Docker](#instalacao-rapida-com-docker)
- [Stack de tecnologias utilizadas](#stack-de-tecnologias-utilizadas)
- [Pré-requisitos](#pr%C3%A9-requisitos)
	- [Build](#build)
		- [Construindo o frontend](#construindo-o-frontend)
		- [Construindo o .war do backend](#construindo-o-war-do-backend)
	- [Runtime](#runtime)
	- [Configurando o Eclipse para desenvolvimento](#configurando-o-eclipse-para-desenvolvimento)
- [Licença](#licen%C3%A7a)
- [Documentos e Capacitação](#documentos-e-capacitação)

## Instalação Rápida Com Docker

Instale na sua máquina o Docker-Compose e o Docker 18.06 (ou superior).
Execute o comando:
```properties
git clone https://github.com/forpdi/plataforma-for.git
cd plataforma-for
docker-compose up
```



## Stack de tecnologias utilizadas
- Banco de dados MySQL 5.7
- Hibernate/JPA como framework de persistência
- VRaptor 4 como framework MVC
- Java EE 7 com JDK 1.8, CDI 1.2 (Weld 2) e JPA 2
- Servidor de aplicação: WildFly 9.0.2
- Serviços baseados em REST
- Frontend web desacoplado:
  - React.js
  - Backbone.js
  - Webpack


## Pré-requisitos
Para a utilização do ForPDI e ForRisco, são necessárias algumas ferramentas para o build e algumas para o runtime do sistema.

### Build
Você precisará de no mínimo, as seguintes ferramentas para realizar o build do sistema:

- Apache Maven 3.x
- JDK 1.8
- Node.js 6+ (apenas para rebuilds do front-end e desenvolvimento da plataforma)
- JBoss Developer Studio (IDE para desenvolvimento)

Com as ferramentas acima você consegue realizar o build da plataforma e desenvolver novos códigos ou apenas gerar um .war para publicação e utilização.
Instale as ferramentas listadas acima e siga os passos adiante.

#### Construindo o frontend
O front-end do projeto está feito utilizando a ferramenta Webpack para bundling e o framework React.js utilizando a arquitetura Flux.
Para realizar build do código do frontend, instale o [Node.js versão 6 ou superior](http://nodejs.org/) e adicione os executáveis à sua variável de ambiente `PATH`.
Em seguida abra uma linha de comando e vá até a pasta `frontend-web/` do projeto.
Essa pasta contém todo o código do frontend. Executa o seguintes comandos para instalar as bibliotecas do Node.js necessárias:

```shell
cd frontend-web
npm install
```

Após a execução destes 2 comandos, você pode realizar um build para produção. Basta executar o comando abaixo:

```shell
# Para compilar a versão de produção
npm run build
```

Após executar este comando seu frontend pronto para publicação estará disponível na pasta `frontend-web/dist/`.

#### Construindo o .war do backend
O ForPDI utiliza o Apache Maven para realizar o processo de packaging do backend da aplicação em um arquivo `.war` que pode ser implantado em um servidor de aplicação que suporta Java.
Para esta etapa, considera-se que você já instalou o JDK 1.8 e configurou corretamente a variável de ambiente `JAVA_HOME`. A próxima etapa é realizar o download do [Maven 3.x](https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.5.0/apache-maven-3.5.0-bin.zip).
Após o download do Maven, descompacte o arquivo em uma pasta. Neste passo a passo será considerado que o Maven foi descompactado na pasta `/opt/maven`.
Em seguida, configure a variável de ambiente `MAVEN_HOME` para o caminho onde você descompactou o arquivo. Também coloque na sua variável `PATH` o caminho da pasta `bin` do Maven (ex: `/opt/maven/bin`).

Com os procedimentos de instalação prontos, você já pode realizar o build e packaging da aplicação. O arquivo `backend-java/pom.xml` descreve todas as configurações do Maven para o projeto.
O ForPDI está configurado com alguns *profiles* iniciais, o profile de desenvolvimento (perfil padrão) já vem pronto para uso, com as configurações no arquivo `backend-java/dev.properties`:

```properties
# dev.properties
backendUrl=http://localhost:8080/forpdi/

db.host=localhost
db.port=3306
db.name=forpdi_db
db.username=root
db.password=

mail.smtp.from.name=ForPDI
mail.smtp.from.email=noreply@forpdi.org
mail.smtp.url=localhost
mail.smtp.port=25
mail.smtp.username=
mail.smtp.password=
mail.smtp.ssl=false
mail.smtp.tls=false
```

Note que o perfil de desenvolvimento vem configurado com SMTP local (localhost na porta 25) e o usuário do banco de dados é o root sem senha. Caso seu ambiente de desenvolvimento tenha outras configurações você pode trocá-las nesse arquivo.
Os outros perfis disponíveis são o de teste (`test`), espelho (`mirror`) e produção (`prd`). Os arquivos de propriedades desses ambientes não são sincronizados pelo Git, já que eles contêm informações de senhas e usuários.
Para realizar o build você terá que copiar o arquivo `dev.properties` para o nome do ambiente cujo qual você quer realizar build (`test.properties`, `mirror.properties` e `prd.properties`).
Em seguida, edite o arquivo e insira os dados de conexão do banco de dados e SMTP de seu ambiente. Por exemplo, para gerar um arquivo WAR para produção poderíamos fazer da seguinte forma:

```shell
# Copie o arquivo dev.properties e edite-o
cd backend-java
cp dev.properties prd.properties
vim prd.properties
```
```properties
# prd.properties
backendUrl=http://app.forpdi.org/

db.host=localhost
db.port=3306
db.name=forpdi_prd
db.username=forpdi
db.password=SuaSenhaDoBancoDeDados

mail.smtp.from.name=ForPDI
mail.smtp.from.email=noreply@forpdi.org
mail.smtp.url=smtp.gmail.com
mail.smtp.port=587
mail.smtp.username=seuemail@gmail.com
mail.smtp.password=SuaSenhaDoEmail
mail.smtp.ssl=false
mail.smtp.tls=true
```

Acima um exemplo de arquivo para produção.
Após configurar o arquivo podemos realizar o packaging do maven para o perfil desejado:

```shell
# Realize o build e packaging da aplicação
cd backend-java
mvn clean package -P prd
```

Após o packaging, o arquivo WAR estará disponível na pasta `backend-java/target/forpdi.war`. Esse arquivo é o backend da sua aplicação, pronto para publicação no Wildfly.

### Runtime
Para o runtime do ForPDI/ForRisco você vai precisar:

- Java 1.8 (JDK)
- Servidor de aplicação Java EE: Wildfly 9.0.2
- Banco de Dados MySQL 5.7
- Servidor web (Apache ou NGINX)

Primeiramente, baixe e instale o JDK 1.8 para seu sistema operacional configurando corretamente a variável JAVA_HOME no seu servidor.
Em seguida faça o download do [Wildfly 9.0.2](http://download.jboss.org/wildfly/9.0.2.Final/wildfly-9.0.2.Final.zip) e descompacte-o em alguma pasta de seu servidor.
Neste passo a passo, será assumido que o Wildfly foi descompactado na pasta `/opt/wildfly`.

Antes de publicar o backend da aplicação no Wildfly, é necessário instalar o MySQL versão 5.7 ou superior e criar o banco de dados que irá conter as tabelas do ForPDI.
Esse banco de dados deve possuir o nome definido no arquivo `prd.properties` na hora do build (propriedade `db.name`). A codificação de caracteres deve ser o *UTF-8*.
Crie o banco de dados através do comando SQL:

```sql
/* Exemplo de criação do banco de dados "forpdi_prd" */
CREATE DATABASE forpdi_prd CHARSET=utf8;
```

O processo de build do arquivo WAR é coberto nas seções anteriores. Isso inclui dados de conexão com o banco de dados que devem ser passados para o sistema no momento do build.
Nessa etapa a única coisa que precisa ser feita é a publicação do arquivo WAR no Wildfly. Existem várias maneiras de fazer isso e o Wildfly possui várias configurações de execução, caso você queira otimizar essas configurações consulta a documentação do Wildfly.
A maneira mais simples de executar o sistema é rodas o Wildfly em modo standalone e colocar o seu arquivo WAR na pasta de deployments:

```shell
# Copie o arquivo war após o build
cp {caminho_da_pasta_backend-java}/target/forpdi.war /opt/wildfly/standalone/deployments/
```

Antes de inicializar o Wildfly, é necessário que habilitemos o conector AJP para que o Apache HTTPD possa posteriormente
atuar como proxy reverso utilizando este protocolo. Edite o arquivo `standalone.xml` para incluir este conector e depois
inicie o Wildfly:

```xml
<!-- Arquivo /opt/wildfly/standalone/configuration/standalone.xml -->
...
        <subsystem xmlns="urn:jboss:domain:undertow:2.0">
            <buffer-cache name="default"/>
            <server name="default-server">
                <ajp-listener name="default-ajp" socket-binding="ajp" redirect-socket="http"/><!-- Inclua essa linha -->
                <http-listener name="default" socket-binding="http" redirect-socket="https"/>
                <host name="default-host" alias="localhost">
                    <filter-ref name="server-header"/>
                    <filter-ref name="x-powered-by-header"/>
                </host>
            </server>
            <servlet-container name="default">
                <jsp-config/>
                <websockets/>
            </servlet-container>
            <handlers>
                <file name="welcome-content" path="${jboss.home.dir}/welcome-content"/>
            </handlers>
            <filters>
                <response-header name="server-header" header-name="Server" header-value="WildFly/9"/>
                <response-header name="x-powered-by-header" header-name="X-Powered-By" header-value="Undertow/1"/>
            </filters>
        </subsystem>
...
```
```shell
vim /opt/wildfly/standalone/configuration/standalone.xml
cd /opt/wildfly
/opt/wildfly/bin/standalone.sh &
# Você precisa sair do terminal com o comando exit para não encerrar o processo:
exit
```

Por padrão o backend do sistema estará disponível em: `http://ip-do-seu-servidor:8080/forpdi/`.
Após a publicação do backend, é necessário configurar um servidor web para servir os arquivos de frontend
e para atuar como um proxy reverso para as chamadas ao backend. Você pode usar o Apache HTTPD ou o NGINX.
Neste tutorial iremos utilizar o Apache HTTPD, porém se você preferir o NGINX é só realizar a configuração
equivalente neste servidor web.

Você precisa instalar o Apache HTTPD (usaremos a versão 2.4, porém é possível realizar a mesma configuração
na versão 2.2, basta consultar a documentação do Apache HTTPD). Instale também os módulos (se já não vierem
junto com o pacote da sua distribuição) `mod_rewrite`, `mod_proxy` e `mod_proxy_ajp`. Além de instalar, é
preciso ativá-los nos arquivos de configuração do Apache HTTPD (este procedimento pode variar de acordo com
a sua distribuição). Nos próximos passos iremos considerar que os arquivos de configuração do HTTPD estão na
pasta `/etc/httpd` e que você estará logado no terminal com o usuário `root`.

Primeiramente você precisa criar um arquivo `.conf` para inserir as configurações do ForPDI/ForRisco. Para isso,
você precisará definir alguns parâmetros que utilizaremos na configuração do sistema:

- FRONTEND_DIR -> Deve ser substituído pelo caminho de uma pasta do servidor onde está a versão gerada pelo build da aplicação (disponível na pasta `frontend-web/dist`), ex: `/var/www/forpdi`
- FORPDI_DOMAIN -> Domínio que será utilizado para acessar o ForPDI/ForRisco, ex: `www.forpdi.org`

Com essas definições, podemos criar o arquivo `forpdi.conf` na pasta apropriada:

```shell
cd /etc/httpd/conf.d
touch forpdi.conf
vim forpdi.conf
```
```conf
# Arquivo forpdi.conf
<VirtualHost *:80>
	ServerName FORPDI_DOMAIN
	UseCanonicalName  Off
	ServerAdmin contato@forpdi.org
	DocumentRoot FRONTEND_DIR

	ErrorLog logs/forpdi-error.log
	CustomLog logs/forpdi-access.log combined

	<Location /forpdi>
		ProxyPreserveHost on
		ProxyPass ajp://0.0.0.0:8009/forpdi
		Order allow,deny
		Allow from all
	</Location>
</VirtualHost>
<Directory FRONTEND_DIR>
	Options Indexes FollowSymLinks
	AllowOverride None
	Require all granted
	<IfModule mod_rewrite.c>
		RewriteEngine On
		RewriteBase /
		RewriteRule ^index\.html$ - [L]
		RewriteCond %{REQUEST_FILENAME} !-f
		RewriteCond %{REQUEST_FILENAME} !-d
		RewriteCond %{REQUEST_FILENAME} !-l
		RewriteRule . /index.html [L]
	</IfModule>
</Directory>

# As configurações abaixo são opcionais mas são recomendadas para performance em produção
KeepAlive on
KeepAliveTimeout 15
MaxKeepAliveRequests 0
Header append Vary User-Agent
AddOutputFilterByType DEFLATE text/html text/css application/json application/javascript text/javascript
BrowserMatch ^Mozilla/4 gzip-only-text/html
BrowserMatch ^Mozilla/4\.0[678] no-gzip
BrowserMatch \bMSIE !no-gzip !gzip-only-text/html
DeflateCompressionLevel 4 # 1 a 9
DeflateMemLevel 9 # 1 a 9
DeflateWindowSize 15 # 1 a 15

# As configurações abaixo servem para habilitar acesso via HTTPS:
<VirtualHost *:443>
	ServerName FORPDI_DOMAIN
	UseCanonicalName  Off
	ServerAdmin contato@forpdi.org
	DocumentRoot FRONTEND_DIR

	SSLEngine on
	# Caminho dos arquivos de certificado digital fictícios, deve trocar pelo
	# caminho dos arquivos em seu servidor
	SSLCertificateFile "/etc/httpd/ssl/forpdi/cert.crt"
	SSLCertificateKeyFile "/etc/httpd/ssl/forpdi/private.key"
	SSLCACertificateFile "/etc/httpd/ssl/forpdi/ca-bundle.crt"
	SSLCompression Off
	SSLHonorCipherOrder On
	SSLCipherSuite ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES256-SHA:DHE-RSA-AES256-GCM-SHA384:DHE-RSA-AES256-SHA256:DHE-RSA-AES256-SHA:!LOW:!MD5:!aNULL:!eNULL:!3DES:!EXP:!PSK:!SRP:!DSS

	ErrorLog logs/forpdi-ssl-error.log
	CustomLog logs/forpdi-ssl-access.log combined

	<Location /forpdi>
		ProxyPreserveHost on
		ProxyPass ajp://0.0.0.0:8009/forpdi
		Order allow,deny
		Allow from all
	</Location>
</VirtualHost>
# Para permitir apenas acesso SSL, pode trocar o virtual host da porta 80 para:
<VirtualHost *:80>
	ServerName FORPDI_DOMAIN
	UseCanonicalName  Off
	RewriteEngine On
	RewriteCond %{HTTPS} !=on
	RewriteRule ^/?(.*) https://%{SERVER_NAME}/$1 [R,L]
</VirtualHost>
```

Após realizar a configuração do arquivo para o HTTPD, é só copiar o conteúdo da pasta `frontend-web/dist` após o build do frontend para a
pasta `FRONTEND_DIR` colocada na configuração. Em seguida basta reiniciar o HTTPD e a
aplicação estará disponível em `http://FORPDI_DOMAIN/`:

```shell
# Copia o conteúdo do frontend e reinicia o HTTPD.
cd frontend-web/dist
cp -R ./* FRONTEND_DIR
service httpd restart # pode variar entre distros
```

O primeiro acesso deve ser feito utilizando o usuário administrador de sistema:

```
E-mail: admin@forpdi.org
Senha: 12345
```

Para um guia mais detalhado da configuração inicial, acesse [o vídeo de instrução dos primeiros passos no ForPDI](https://youtu.be/4INjIqucYRo),
que explica as configurações e recursos mínimos para iniciar a utilização do ForPDI/ForRisco.

### Configurando o Eclipse para desenvolvimento

## Licença

O ForPDI/ForRisco é disponibilizado sob a licença Apache License 2.0.

## Documentos e Capacitação

- [Capacitação Online](https://www.youtube.com/channel/UC1DP0ZGoTm0OlX-vOc-Ns5Q/videos)
- [Diagrama Entidade-Relacionamento ForPDI](https://github.com/forpdi/plataforma-for/blob/master/doc/diagram_forpdi.pdf)
- [Diagrama Entidade-Relacionamento ForRisco](https://github.com/forpdi/plataforma-for/blob/master/doc/diagram_forrisco.pdf)
- [Diagrama Entidade-Relacionamento ForPDI + ForRisco](https://github.com/forpdi/plataforma-for/blob/master/doc/diagram.pdf)
- [Dicionário de Dados ForPDI](https://github.com/forpdi/plataforma-for/blob/master/doc/dictionary_forpdi.html)
- [Dicionário de Dados ForRisco](https://github.com/forpdi/plataforma-for/blob/master/doc/dictionary_forrisco.html)
- [Dicionário de Dados ForPDI + ForRisco](https://github.com/forpdi/plataforma-for/blob/master/doc/dictionary.html)
- [Documentação do Sistema (ForPDI)](https://github.com/forpdi/plataforma-for/blob/master/doc/FORPDI_documentacao_do_sistema.pdf)
- [Documentação da Interface (ForPDI)](https://github.com/forpdi/plataforma-for/blob/master/doc/FORPDI_documentacao_da_interface.pdf)
- [Manual ForPDI](https://github.com/forpdi/plataforma-for/blob/master/doc/manual.pdf)
- [Manual ForRisco]()
