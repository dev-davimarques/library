# Persona
Atue como um Arquiteto de Software Java Senior. O objetivo é gerar um sistema monolítico Spring Boot 3.x seguindo o padrão MVC, com separação clara de responsabilidades entre 3 domínios.

# Contexto do Projeto
- Projeto: Library Management System (Modular).
- Stack: Java 21, Thymeleaf, Spring Data JPA, H2, Lombok.
- Interface: Bootstrap 5 (Layout profissional e responsivo).

# Modelo de Dados e Domínios
Deve haver 3 fluxos completos e independentes (Controller + Service + Repository):
1. Publisher (Editora): id, name.
2. Author (Autor): id, name.
3. Book (Livro): id, title, Publisher publisher, List<Author> authors, String isbn.

# Requisitos de Negócio e Validação
- Validação: Todos os nomes/títulos são `@NotBlank`. O ISBN é obrigatório.
- Integridade: 
  - Service de Publisher deve impedir nomes duplicados.
  - Service de Book deve impedir ISBN duplicado.
- Relacionamentos: Use `@ManyToOne` para Publisher e `@ManyToMany` para Authors.

# Estrutura de Telas (Frontend แยก)
1. Cadastros Independentes:
   - Uma tela para listar/cadastrar Editoras.
   - Uma tela para listar/cadastrar Autores.
2. Tela de Books Elaborada:
   - Deve ser o dashboard principal.
   - Formulário de cadastro deve conter um `<select>` para Editora e um `<select multiple>` ou checkboxes para selecionar múltiplos Autores.
   - Tabela de listagem com busca por título e filtros por Editora/Autor.
   - Uso de Badges do Bootstrap para exibir os nomes dos autores na tabela.

# Tarefa (Código Modular)
Gere os ficheiros seguindo esta estrutura:
- 3 Entidades JPA.
- 3 Repositories.
- 3 Services (com as validações de duplicados especificadas).
- 3 Controllers (PublisherController, AuthorController, BookController).
- Templates Thymeleaf: 'publisher/list.html', 'author/list.html' e 'book/form.html' (versão elaborada).

# Formato de Saída
- Código seguindo Clean Code e Injeção de Dependência por construtor.
- Explicação de como a relação Many-to-Many entre Book e Author foi implementada no formulário.
- Blocos de código Markdown separados por nome de ficheiro.