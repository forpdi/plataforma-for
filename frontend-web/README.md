#### Construindo o frontend com o Docker
Para construir o projeto com o Docker, se faz necessário ter no mínimo a versão 17.05 ou superior. A partir dessa versão é permitido a construição em modo multstage.

Execute os seguintes comandos na pasta onde fica o projeto:
	- docker build -t forpdi-front-end .
	- docker run -d --name forpdi-front-end -p 7778:80 forpdi-front-end

Pronto! Seu container com a aplicação referente ao frontent estará disponivel para acesso em: http://host:7778/forpdi
