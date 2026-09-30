# Sistema Escolar MVC

**Atividade de Arquitetura MVC com Java**
Aluno: Matheus Soto · Turma: 2º D.S. · Professor: Maurício

Aplicação web feita do zero para praticar a separação entre Model, View, Controller, Service, Repository e banco de dados. O sistema reúne cadastro de alunos com RG, CRUD de cursos e matrícula de alunos em cursos.

## Tecnologias

- Java 17 e Spring Boot 3
- Spring MVC e Thymeleaf
- Spring Data JPA e Hibernate
- Banco H2 em memória
- Maven

## Como executar

É necessário ter Java 17 e Maven instalados. Na pasta do projeto, rode:

```bash
mvn spring-boot:run
```

Depois acesse:

- Sistema: <http://localhost:8080>
- Alunos: <http://localhost:8080/alunos>
- Cursos: <http://localhost:8080/cursos>
- Matrículas: <http://localhost:8080/matriculas>
- H2 Console: <http://localhost:8080/h2-console>

No H2 Console, use JDBC URL `jdbc:h2:mem:escola`, usuário `sa` e deixe a senha vazia. Exemplos de consulta:

```sql
SELECT * FROM ALUNO;
SELECT * FROM CURSO;
SELECT * FROM MATRICULA;
```

Na primeira inicialização, o sistema cria um aluno, um curso e uma matrícula de exemplo para facilitar a demonstração no H2. O aluno de exemplo também tem RG. O H2 está configurado em memória (`create-drop`), então esses dados são apagados quando a aplicação é encerrada.

## Funcionalidades

### Alunos

- Listar, cadastrar, editar e excluir.
- Nome, e-mail e RG obrigatórios.
- O RG é comparado sem diferenciar maiúsculas e minúsculas; na edição o próprio registro é ignorado na verificação de duplicidade.
- Um aluno com matrículas não pode ser excluído.

### Cursos

- Listar, cadastrar, editar e excluir.
- Nome obrigatório e carga horária maior que zero.
- Um curso com matrículas não pode ser excluído, para preservar os vínculos.

### Matrículas

- Criar o vínculo entre um aluno e um curso já cadastrados.
- Data preenchida automaticamente com a data atual e status inicial `ATIVA`.
- Permite alterar o status para `ATIVA`, `TRANCADA` ou `CONCLUIDA`.
- Impede duas matrículas ativas para o mesmo aluno e curso, inclusive ao reativar uma matrícula.

## Organização do código

```text
src/main/java/br/com/escola/mvc/
├── controller/  # rotas web e coordenação das telas
├── model/       # Aluno, Curso, Matricula e StatusMatricula
├── repository/  # acesso aos dados com Spring Data JPA
├── service/     # validações e regras do sistema
└── MvcApplication.java

src/main/resources/
├── templates/   # telas Thymeleaf
├── static/css/  # estilos
└── application.properties
```

## Respostas às questões

**1. Em qual camada foi implementada a validação de matrícula duplicada?**
No `MatriculaService`. Essa regra envolve consultar a combinação aluno, curso e status antes de salvar, então faz parte da regra de negócio. O Controller só recebe os dados e apresenta o resultado; o Repository faz a consulta ao banco.

**2. O atributo curso como texto deve permanecer em Aluno depois da criação da entidade Matricula?**
Não. O aluno não precisa guardar o nome do curso como texto, porque a matrícula já relaciona o aluno à entidade Curso. Assim, o sistema usa os IDs e consegue manter os vínculos corretos mesmo se o nome do curso mudar.

**3. O que deve acontecer ao tentar excluir um curso que possui matrículas?**
A exclusão é bloqueada e o sistema apresenta uma mensagem. Isso evita apagar um curso ainda referenciado por matrículas. Primeiro seria necessário resolver essas matrículas (por exemplo, alterando seu estado ou removendo os vínculos) e então excluir o curso.

**4. Qual é o fluxo do formulário até a gravação da matrícula?**
A View envia `POST /matriculas/salvar` com os identificadores escolhidos. O `MatriculaController` recebe a requisição e chama `MatriculaService`. O Service busca aluno e curso no `AlunoRepository` e no `CursoRepository`, valida a regra de duplicidade, define a data e o status inicial, e pede ao `MatriculaRepository` para salvar. O Spring Data JPA grava os dados no H2. Por fim, o Controller redireciona para a listagem, que lê as matrículas e mostra os dados relacionados.

## Roteiro curto de demonstração

1. Cadastrar um aluno e conferir o RG na lista.
2. Tentar cadastrar outro aluno com o mesmo RG e mostrar a mensagem; editar o primeiro sem trocar o RG.
3. Cadastrar um curso e tentar carga horária igual a zero.
4. Criar matrícula, confirmar data e status, e tentar repetir a mesma matrícula ativa.
5. Alterar o status, tentar excluir o curso vinculado e mostrar o bloqueio.
6. Abrir o H2 Console e executar os `SELECT` acima.
