# Alumni Computacao@UFCG

Esse serviço oferece uma API REST para consulta de dados obtidos a partir de "scraping" de páginas de perfis do LinkedIn.

No caso de Alumni Computação@UFCG, foi criado um grupo no LinkedIn chamado "Alumni Computação@UFCG" e feita uma campanha
para associar os egressos dos cursos de graduação e pós-graduação de Computação@UFCG a esse grupo.

Periodicamente usa-se o serviço gratuito disponível em https://phantombuster.com/ para: i) recuperar a lista de membros
do grupo "Alumni Computação@UFCG" no LinkedIn; e ii) recuperar os dados dos perfis de todos os membros recuperados no
passo "i".

O serviço permitirá ligar essa informação com informação do Sistema de Controle Acadêmico Online (SCAO) da UFCG. Isso
habilitará a Coordenação do Curso de Ciência da Computação da UFCG a ter mais informações sobre o impacto do curso na
sociedade, servindo tanto como ferramenta de prestação de contas para quem financia o curso, como também como um
importante instrumento de planejamento das atividades do curso.

Além disso, serão implementadas funcionalidades típicas de redes sociais que ofereçam serviços de interesse dos egressos
do curso, tanto para aumentar o engajamento social de egressos, como viabilizar cooperações e outras atividades no contexto
da atuação profissional dos egressos.

## Rodando o sistema

- É necessário ter instalado a JDK e o maven em sua máquina
- Clone o repositório na branch `matheus/alumni`
  - Pode ser realizado no terminal com o comando: `git clone -b matheus/alumni https://github.com/computacao-ufcg/pdc-alumni`
- Entre na pasta raíz do repositório local, e execute o comando `mvn install`
- Na IDE (Intellij), abra o projeto e realize o build
- Ao lado direito, verifique a aba `maven` e realize o reload de todos os projetos
- Adicione a pasta `private` (enviada via discord) aos diretórios `pdc-alumni\src\main\resources` e `pdc-alumni\target\classes`
- Realize um novo build do projeto
- Execute o projeto no arquivo `AlumniApplication` na classe `main`
- O projeto deve estar rodando na porta `8080`
- Para verificar se obteve êxito, verifique no navegador o endereço `localhost:8080/alumni`

## Enviando a aplicação para o DockerHub

- A partir da raiz do projeto, digite:

    `docker build -t alumni-backend:dev .`

- Após o sucesso do build, esteja logado com sua conta do DockerHub para enviar a imagem.Para se conectar ao Docker:

    `docker login`

- Insira suas credenciais e faça o login.

**Criando a tag para a imagem.**

- Com a imagem montada, e o login efetuado, execute:

    `docker images`

- Recupere o id da imagem alumni-backend, pois iremos utilizar no próximo passo.

    `docker tag "id_imagem" eureca/alumni-backend:dev`

    `docker push eureca/alumni-backend:dev`

- **"eureca/"** é o nome da organização que o docker enviará/atualizará a imagem.
- **alumni-backend:dev** é o nome da imagem.