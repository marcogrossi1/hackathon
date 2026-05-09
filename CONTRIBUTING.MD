# Convenção

## Branchs

Os nossos projetos conta com duas branchs principais:

- `main` como a branch principal, contendo a versão oficial do projeto.
- `develop` como a branch secundária, contendo a versão testada e mais atualizada do projeto.

Durante o desenvolvimento, o padrão de criação de branchs é o seguinte:

- `feature/<branch-subject>-<task-id>`: branchs para o desenvolvimento de uma nova funcionalidade.
- `fix/<branch-subject>-<task-id>`: branchs para o correção de um problema ou bug.
- `docs/<branch-subject>-<task-id>`: branchs para mudanças na documentação do projeto.
- `refactor/<branch-subject>-<task-id>`: branchs para refatorações que não adicionam funcionalidades nem corrigem bugs.
- `style/<branch-subject>-<task-id>`: branchs para mudanças em estilos e lints.

> **Exemplos:**  `feature/signup-4754`, `fix/user-data-not-being-saved-4758` e `docs/readme-installation-section-4653`
> 

---

## Mensagens de commit

Nós seguimos o padrão [conventional commits](https://www.conventionalcommits.org), como forma de padronizar as mensagens de commit. Por isso, cada commit deve estar em inglês e seguir a seguinte estrutura:

```markdown
type(optional scope): subject
```

Os tipos disponíveis são:

- `build`: mudanças no processo de build ou em dependências externas
- `docs`: mudanças na documentação do projeto
- `feat`: mudanças que introduzem uma nova funcionalidade
- `fix`: mudanças que resolvem problema ou bug
- `perf`: mudanças para melhorias de performance
- `refactor`: refatorações que não adicionam funcionalidades nem corrigem problemas
- `style`: mudanças de estilo de código e lint
- `revert`: reversões a commits anteriores

> **Exemplos:** `feat: saves user data to database` e `fix: align inputs correctly`.
>

## Pull requests

Pull requests devem ser direcionados à branch `develop`. É recomendado que eles contenham uma pequena descrição do que foi modificado, na seguinte estrutura:

- **Exemplos de títulos:**
    - `Feature: <funcionalidade adicionada>`
    - `Fix: <fix realizado>`
    - `Docs: <mudanças na documentação>`
    - ...
- **Exemplo de corpo:**
  ```md
    ## PR Message
    What kind of change does this PR introduce?

    * [ ] Bugfix
    * [ ] Feature
    * [ ] Code style update (formatting, local variables)
    * [ ] Refactoring (no functional changes, no api changes)
    * [ ] Build related changes
    * [ ] CI related changes
    * [ ] Documentation content changes
    * [ ] Tests
    * [ ] Other

    [Issue Link](https://dev.azure.com/NexosDigital/standard/_boards/board/t/standard%20Team/Issues/?workitem=6512)

    ## What's new?
    - 

    ## Screenshots
    N/A
  ```
---