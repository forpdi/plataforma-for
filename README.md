
![ForPDI Logo](http://forpdi.org/img/logo_forpdi.png)
Plataforma Open Source para criação, gerenciamento, acompanhamento e divulgação de Planos de Desenvolvimento Institucional (PDI).

Website: [http://www.forpdi.org/](http://www.forpdi.org/)

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
npm run deploy

# Para realizar um watch de desenvolvimento
npm run devwatch
```

#### Construindo o .war para publicação


### Runtime
Para o runtime do forpdi você vai precisar:

- Java 1.8 (JDK)
- Servidor de aplicação Java EE: Wildfly 9.0.2
- Banco de Dados MySQL 5.7+

Primeiramente, baixe e instale o JDK 1.8 para seu sistema operacional configurando corretamente a variável JAVA_HOME no seu servidor.
Em seguida faça o download do [Wildfly 9.0.2](http://download.jboss.org/wildfly/9.0.2.Final/wildfly-9.0.2.Final.zip) e descompacte-o em alguma pasta de seu servidor.
Neste passo a passo, será assumido que o Wildfly foi descompactado na pasta `/opt/wildfly`.

O processo de build do arquivo WAR é cober nas seções anteriores. Isso inclui dados de conexão com o banco de dados que devem ser passados para o sistema no momento do build. Então daqui por diante será assumido que o banco de dados com codificação de caracteres UTF-8 já foi criado.
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

Por padrão o sistema estará disponível em: `http://seuservidor.com/forpdi/`

#### Implantando no Wildfly

#### Configurando o Eclipse para desenvolvimento

## Licença

O ForPDI é disponibilizado sob a licença Apache License 2.0.
