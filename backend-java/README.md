#### Aviso

Para subir a Plataforma-For por completo  utilize o docker-compose na raiz do projeto.

#### Construindo somente a imagem Docker do Backend
Para construir o projeto com o Docker, se faz necessário ter no mínimo a versão 18.06 ou superior. 

Pré condição para iniciar o container:
  - Ter a instância do database iniciada
  - Configurar o arquivo conf/docker.dev.properties com as propriedades de banco de dados.

Execute os seguintes comandos dentro da pasta backend-java/ :
 - docker build -t platfor-backend .
 - docker run -it --name platfor-backend -p 8080:8080 -p 8009:8009 platfor-backend

Pronto! Seu container com a aplicação referente ao backend esta disponivel.
Para testar acesse em: http://localhost:8080/plataforma-for/environment
