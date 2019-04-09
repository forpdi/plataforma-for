#### Construindo o backend com o Docker
Para construir o projeto com o Docker, se faz necessário ter no mínimo a versão 17.05 ou superior. A partir dessa versão é permitido a construição em modo multstage.

Pré condição:
  - Ter a instância do database inciaida
  - Configurar o arquivo dev.properties com as propriedades de banco de dados.

Execute os seguintes comandos na pasta onde fica o projeto:
	- docker build -t forpdi-backend .
  - docker run -it --name forpdi-backend -p 8072:8080 -p 9590:9990 -p 8909:8009 forpdi-backend

Pronto! Seu container com a aplicação referente ao frontent estará disponivel para acesso em: http://host:8072/forpdi
