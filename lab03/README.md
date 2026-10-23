# Sistema de Moeda Estudantil — Laboratório 3

Projeto da disciplina **Projeto de Software** (Engenharia de Software — PUC Minas),
Laboratório 3, Segundo Semestre/2026.

> Entrega atual: **Lab03S01** (Modelagem do sistema — Diagrama de Casos de Uso, Histórias
> de Usuário, Diagrama de Classes e Diagrama de Componentes).

## Sobre o sistema

Sistema para estimular o reconhecimento do mérito estudantil através de uma moeda
virtual. Professores distribuem moedas aos alunos como reconhecimento, e os alunos trocam
essas moedas por vantagens (descontos, produtos) oferecidas por empresas parceiras.

### Regras de negócio principais

- Alunos se cadastram informando nome, e-mail, CPF, RG, endereço, instituição de ensino
  (pré-cadastrada) e curso.
- Professores e instituições já vêm **pré-cadastrados** no sistema (a instituição envia a
  lista de professores no momento da parceria) — esse processo de carga inicial é tratado
  como dado semente, fora do escopo dos casos de uso modelados aqui.
- A cada semestre, cada professor recebe **1.000 moedas**, que se **acumulam** caso não
  sejam todas distribuídas no semestre anterior.
- Ao enviar moedas a um aluno, o professor deve ter saldo suficiente e informar um
  **motivo obrigatório** (mensagem aberta).
- Ao receber moedas, o aluno é **notificado por e-mail**.
- Alunos e professores podem consultar o **extrato** da conta (saldo e transações: envio
  para o professor; recebimento ou troca para o aluno).
- Para trocar moedas, o aluno escolhe uma **vantagem** cadastrada por uma empresa parceira
  (desconto, produto, etc.).
- Empresas parceiras se cadastram e cadastram suas vantagens, cada uma com descrição,
  foto e custo em moedas.
- Ao resgatar uma vantagem, o valor é descontado do saldo do aluno, e um **e-mail de
  cupom** (com código gerado pelo sistema) é enviado tanto ao aluno quanto à empresa
  parceira, para conferência na troca presencial.
- Alunos, professores e empresas parceiras autenticam-se com login e senha.

## Atores

| Ator                                 | Descrição                                                                                     |
| ------------------------------------- | ---------------------------------------------------------------------------------------------- |
| **Aluno**                             | Cadastra-se, consulta extrato e resgata vantagens oferecidas por empresas parceiras.           |
| **Professor**                         | Envia moedas aos alunos como reconhecimento e consulta seu extrato.                            |
| **Empresa Parceira**                  | Cadastra-se e cadastra as vantagens que oferece em troca de moedas.                            |
| **Serviço de E-mail** _(ator secundário)_ | Sistema externo acionado para notificar alunos e parceiros (recebimento de moeda, cupons). |

## Diagrama de Caso de Uso

![Diagrama de Caso de Uso do Sistema de Moeda Estudantil](docs/diagrama-caso-de-uso.png)

_Notação: elipses são casos de uso, agrupados dentro do sistema; retângulos arredondados são
atores. Setas sólidas são associações ator↔caso de uso; setas tracejadas são relações
`«include»` entre casos de uso._

**Observações sobre o diagrama:**

- O pré-cadastro de instituições e professores não aparece como caso de uso — é dado
  semente do sistema, não uma funcionalidade acionada por um ator.
- **Enviar Moedas a Aluno** e **Resgatar Vantagem** incluem **Notificar por E-mail**, que
  por sua vez aciona o ator secundário Serviço de E-mail.
- Todos os casos de uso de Aluno, Professor e Empresa Parceira pressupõem
  `«include» Autenticar-se`.

**Para editar este diagrama:** o fonte é texto puro em
[docs/diagrama-caso-de-uso.mmd](docs/diagrama-caso-de-uso.mmd) (sintaxe
[Mermaid](https://mermaid.js.org/)). Duas formas de editar:

1. Abra [mermaid.live](https://mermaid.live/), cole o conteúdo do `.mmd`, edite visualmente
   e exporte o PNG por cima de `docs/diagrama-caso-de-uso.png`.
2. Edite o texto do `.mmd` diretamente (é só uma lista de nós e setas) e peça para renderizar
   a imagem novamente.

## Histórias de Usuário

### Aluno

1. Como aluno, quero me cadastrar informando meus dados pessoais e selecionando minha
   instituição de ensino, para ingressar no sistema de mérito.
2. Como aluno, quero me autenticar no sistema com login e senha, para acessar minhas
   funcionalidades.
3. Como aluno, quero consultar o extrato da minha conta, para ver meu saldo de moedas e
   o histórico de recebimentos e trocas.
4. Como aluno, quero resgatar uma vantagem cadastrada por uma empresa parceira, para trocar
   minhas moedas por produtos ou descontos.
   - O valor da vantagem é descontado do meu saldo.
   - Recebo um e-mail com um cupom (código gerado pelo sistema) para usar na troca
     presencial.

### Professor

5. Como professor, quero me autenticar no sistema, para acessar minhas funcionalidades.
6. Como professor, quero enviar moedas a um aluno informando o motivo do reconhecimento,
   para premiar bom comportamento ou participação em aula.
   - Preciso ter saldo suficiente para o envio.
   - O motivo é uma mensagem aberta e obrigatória.
7. Como professor, quero que meu saldo seja reabastecido automaticamente a cada semestre
   com 1.000 moedas, acumulando o que não usei, para continuar reconhecendo meus alunos.
8. Como professor, quero consultar o extrato da minha conta, para ver meu saldo e o
   histórico de moedas enviadas.

### Empresa Parceira

9. Como empresa parceira, quero me cadastrar no sistema, para oferecer vantagens aos
   alunos.
10. Como empresa parceira, quero cadastrar vantagens com descrição, foto e custo em
    moedas, para que os alunos possam resgatá-las.
11. Como empresa parceira, quero receber um e-mail com o código do cupom quando um aluno
    resgatar uma vantagem, para conferir a troca presencial.

### Serviço de E-mail _(ator secundário)_

12. Como serviço de e-mail, quero ser acionado sempre que um aluno receber moedas ou
    resgatar uma vantagem, para notificar os envolvidos (aluno e, quando aplicável, a
    empresa parceira).

## Diagrama de Classes

![Diagrama de Classes do Sistema de Moeda Estudantil](docs/diagrama-classes.png)

**Observações sobre o diagrama:**

- `Usuario` é uma classe abstrata com login/senha e autenticação; `Aluno`, `Professor` e
  `EmpresaParceira` herdam dela, já que os três atores autenticam-se do mesmo jeito.
- `Transacao` é uma classe abstrata que generaliza `EnvioMoeda` (professor → aluno) e
  `Resgate` (aluno → vantagem), refletindo o extrato único mencionado no enunciado
  ("transações que realizou").
- `NotificacaoEmail` é a classe de integração com o serviço externo de e-mail (ator
  secundário do caso de uso), acionada por `EnvioMoeda` e `Resgate`.
- `Instituicao` aparece apenas como dado associado a `Aluno`/`Professor`, sem casos de uso
  de cadastro próprios (ver observação no diagrama de casos de uso).
- Os atributos e assinaturas de métodos aqui modelados são os que serão implementados como
  stubs (métodos vazios) no projeto Java desta sprint.

**Para editar este diagrama:** mesmo fluxo do diagrama de caso de uso — o fonte é
[docs/diagrama-classes.mmd](docs/diagrama-classes.mmd) (sintaxe
[Mermaid](https://mermaid.js.org/)). Edite o `.mmd` e cole em [mermaid.live](https://mermaid.live/)
para visualizar/exportar o PNG novamente.

## Diagrama de Componentes

![Diagrama de Componentes do Sistema de Moeda Estudantil](docs/diagrama-componentes.png)

**Observações sobre o diagrama:**

- A arquitetura segue o padrão **MVC** exigido a partir da Sprint 02: Camada de
  Apresentação (View), Camada de Aplicação (Controllers), Camada de Domínio (Services) e
  Camada de Persistência (DAO).
- `ServicoMoedas` e `ServicoVantagens` concentram as regras de negócio (saldo, crédito
  semestral, desconto ao resgatar) e delegam a persistência aos componentes DAO.
- `ServicoNotificacao` isola a integração com o Serviço de E-mail externo, reutilizado
  tanto pelo fluxo de envio de moedas quanto pelo de resgate de vantagens.
- Os componentes DAO encapsulam o acesso ao Banco de Dados, antecipando a estratégia de
  persistência (ORM/DAO) que será detalhada na Sprint 02.

**Para editar este diagrama:** mesmo fluxo dos diagramas anteriores — o fonte é
[docs/diagrama-componentes.mmd](docs/diagrama-componentes.mmd) (sintaxe
[Mermaid](https://mermaid.js.org/)). Edite o `.mmd` e cole em [mermaid.live](https://mermaid.live/)
para visualizar/exportar o PNG novamente.
