
![ForPDI Logo](http://forpdi.org/img/logo_forpdi.png)
Plataforma Open Source para criação, gerenciamento, acompanhamento e divulgação de Planos de Desenvolvimento Institucional (PDI).

Website: [http://www.forpdi.org/](http://www.forpdi.org/)

**Table of Contents**

- [Stack de tecnologias utilizadas](#stack-de-tecnologias-utilizadas)
- [Pré-requisitos](#pr%C3%A9-requisitos)
  - [Build](#build)
    - [Construindo o front-end](#construindo-o-front-end)
    - [Construindo o .war para publicação](#construindo-o-war-para-publica%C3%A7%C3%A3o)
  - [Runtime](#runtime)
  - [Configurando o Eclipse para desenvolvimento](#configurando-o-eclipse-para-desenvolvimento)
- [Licença](#licen%C3%A7a)


## Stack de tecnologias utilizadas
- Banco de dados MySQL 5.7
- Hibernate/JPA como framework de persistência
- VRaptor 4 como framework MVC
- Java EE 7 com JDK 1.8, CDI 1.2 (Weld 2) e JPA 2
- Servidor de aplicação: WildFly 9.0.2
- Serviços baseados em REST
- Frontend web desacoplado
  - React.js
  - Backbone.js
  - Webpack


## Pré-requisitos
Para a utilização do ForPDI, são necessárias algumas ferramentas para o build e algumas para o runtime do sistema.

### Build
Você precisará de no mínimo, as seguintes ferramentas para realizar o build do sistema:

- Apache Maven 3.x
- JDK 1.8
- Node.js 6.x (apenas para rebuilds do front-end e desenvolvimento da plataforma)
- Eclipse (IDE para desenvolvimento)

Com as ferramentas acima você consegue realizar o build da plataforma e desenvolver novos códigos ou apenas gerar um .war para publicação e utilização.
Instale as ferramentas listadas acima e siga os passos adiante.

#### Construindo o front-end
O front-end do projeto está feito utilizando a ferramenta Webpack para bundling e o framework React.js utilizando a arquitetura Flux.
Para realizar build do código do front-end, instale o [Node.js versão 6.x](http://nodejs.org/) e adicione os executáveis à sua variável de ambiente `PATH`.
Em seguida abra uma linha de comando e vá até a pasta `backend-java/src/main/frontend/` do projeto.
Essa pasta contém todo o código do front-end. Executa o seguintes comandos para instalar as bibliotecas do Node.js necessárias:

```shell
npm -g install Webpack
npm install
```

Após a execução destes 2 comandos, você pode realizar um build para produção ou usar a opção de *watch* para desenvolvimento. Basta executar os comandos abaixo:

```shell
# Para compilar a versão de produção
npm run build

# Para realizar um watch de desenvolvimento
npm run devwatch
```

Após realizar o build do frontend você já pode construir o arquivo WAR para posterior publicação no servidor de produção.

#### Construindo o .war para publicação
O ForPDI utiliza o Apache Maven para realizar o processo de packaging da aplicação em um arquivo `.war` que pode ser implantado em um servidor de aplicação que suporta Java.
Para esta etapa, considera-se que você já instalou o JDK 1.8 e configurou corretamente a variável de ambiente `JAVA_HOME`. A próxima etapa é realizar o download do [Maven 3.x](http://ftp.unicamp.br/pub/apache/maven/maven-3/3.5.0/binaries/apache-maven-3.5.0-bin.zip).
Após o download do Maven, descompacte o arquivo em uma pasta. Neste passo a passo será considerado que o Maven foi descompactado na pasta `/opt/maven`.
Em seguida, confkgure a variável de ambiente `MAVEN_HOME` para o caminho onde você descompactou o arquivo. Também coloque na sua variável `PATH` o caminho da pasta `bin` do Maven (ex: `/opt/maven/bin`).

Com os procedimentos de instalação prontos, você já pode realizar o build e packaging da aplicação. O arquivo `backend-java/pom.xml` descreve todas as configurações do Maven para o projeto.
O ForPDI está configurado com alguns *profiles* iniciais, o profile de desenvolvimento (perfil padrão) já vem pronto para uso, com as configurações no arquivo `backend-java/dev.properties`:

```properties
# dev.properties
war.frontenddir=development
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
war.frontenddir=production
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

Acima um exemplo de arquivo para produção. A propriedade `war.frontenddir` define qual build do frontend deve ser copiado para a aplicação final, `development` ou `production`. Essa parte do build foi explicado na seção anterior.
Após configurar o arquivo podemos realizar o packaging do maven para o perfil desejado:

```shell
# Realize o build e packaging da aplicação
cd backend-java
mvn clean package -P prd
```

Após o packaging, o arquivo WAR estará disponível na pasta `backend-java/target/forpdi.war`. Esse arquivo é sua aplicação completa, pronta para publicação no Wildfly.

### Runtime
Para o runtime do forpdi você vai precisar:

- Java 1.8 (JDK)
- Servidor de aplicação Java EE: Wildfly 9.0.2
- Banco de Dados MySQL 5.7+

Primeiramente, baixe e instale o JDK 1.8 para seu sistema operacional configurando corretamente a variável JAVA_HOME no seu servidor.
Em seguida faça o download do [Wildfly 9.0.2](http://download.jboss.org/wildfly/9.0.2.Final/wildfly-9.0.2.Final.zip) e descompacte-o em alguma pasta de seu servidor.
Neste passo a passo, será assumido que o Wildfly foi descompactado na pasta `/opt/wildfly`.

Antes de publicar a aplicação no WIldfly, é necessário instalar o MySQL versão 5.7 ou superior e criar o banco de dados que irá conter as tabelas do ForPDI.
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
# Execute o Wildfly em modo standalone como root
cd /opt/wildfly
/opt/wildfly/bin/standalone.sh &
# Você precisa sair do terminal com o comando exit para não encerrar o processo:
exit
```

Por padrão o sistema estará disponível em: `http://seuservidor.com/forpdi/`.
O primeiro acesso deve ser feito utilizando o usuário administrador de sistema:

```
E-mail: admin@forpdi.org
Senha: 12345
```

Para um guia mais detalhado da configuração inicial, acesse [o vídeo de instrução dos primeiros passos no ForPDI](https://youtu.be/4INjIqucYRo),
que explica as configurações e recursos mínimos para iniciar a utilização do ForPDI.

### Configurando o Eclipse para desenvolvimento

## Licença

O ForPDI é disponibilizado sob a licença Apache License 2.0.
