
![ForPDI Logo](http://forpdi.org/img/logo_forpdi.png)
Plataforma Open Source para criação, gerenciamento, acompanhamento e divulgação de Planos de Desenvolvimento Institucional (PDI).

Website: [http://www.forpdi.org/](http://www.forpdi.org/)

## Stack de tecnologias utilizadas
- Banco de dados MySQL 5.7
- Hibernate/JPA como framework de persistência
- VRaptor 4 como framework MVC
- Java EE 7 com JDK 1.8, CDI 1.2 (Weld 2) e JPA 2
- Servidor de aplicação WildFly 9.0.2
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

#### Implantando no Wildfly

#### Configurando o Eclipse para desenvolvimento

## Licença

O ForPDI é disponibilizado sob a licença Apache License 2.0.
