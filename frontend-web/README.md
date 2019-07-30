#### Aviso

Para subir a Plataforma-For por completo  utilize o docker-compose na raiz do projeto.

#### Construindo somente a imagem Docker do frontend 
Para construir o projeto com o Docker, se faz necessário ter no mínimo a versão 18.06 ou superior. 
A partir dessa versão é permitido a construição em modo multstage.

Execute os seguintes comandos na pasta frontend-web:
	- docker build -t platfor-frontend .
	- docker run -d --name platfor-frontend -p 80:80 platfor-frontend

Pronto! Seu container com a aplicação referente ao frontent está disponivel, acesso em: http://localhost/
