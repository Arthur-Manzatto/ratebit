# Ratebit - Documentação Técnica do Projeto

## Sumário
1. [Visão Geral e Proposta](#visão-geral-e-proposta)
2. [Funcionalidades Principais](#funcionalidades-principais)
3. [Detalhamento das Telas e Experiência do Usuário](#detalhamento-das-telas-e-experiência-do-usuário)
4. [Arquitetura e Pilha Tecnológica](#arquitetura-e-pilha-tecnológica)
5. [Modelagem de Dados (Firestore)](#modelagem-de-dados-firestore)
6. [Diretrizes para Instalação e Execução](#diretrizes-para-instalação-e-execução)
7. [Histórico de Versões](#histórico-de-versões)
8. [Equipe de Desenvolvimento](#equipe-de-desenvolvimento)

---

## 1. Visão Geral e Proposta

O Ratebit é uma plataforma móvel voltada para a comunidade de jogadores, com o objetivo de centralizar a descoberta e avaliação de títulos eletrônicos. O projeto propõe um ecossistema social onde a experiência do usuário é priorizada, permitindo a curadoria pessoal de uma biblioteca de favoritos e a participação ativa através de críticas detalhadas. O foco principal é oferecer uma interface moderna e responsiva que conecte jogadores às suas preferências de forma direta e eficiente.

---

## 2. Funcionalidades Principais

### Gestão de Conteúdo e Comunidade
- **Feedback Interativo:** Sistema de avaliação por estrelas com atualização visual dinâmica, permitindo uma percepção clara da nota atribuída.
- **Sincronização Reativa:** Utilização de listeners em tempo real para que qualquer alteração em favoritos ou avaliações seja refletida instantaneamente em todas as telas do aplicativo.
- **Filtragem de Alta Performance:** Implementação de filtros locais que permitem ao usuário refinar comentários e listas sem dependência constante de novas requisições ao servidor.
- **Persistência de Avaliações:** Capacidade de edição e atualização de reviews, garantindo que o usuário possa evoluir sua opinião sobre um jogo ao longo do tempo.

### Segurança e Governança
- **Níveis de Acesso:** Distinção entre usuários comuns e administradores, protegendo as ferramentas de cadastro de novos títulos e garantindo a integridade do banco de dados global.

---

## 3. Detalhamento das Telas e Experiência do Usuário

### Fluxo de Acesso (Login e Cadastro)
As telas de autenticação gerenciam a entrada do usuário através de validação segura com o Firestore. A sessão é persistida localmente via SharedPreferences para evitar logins redundantes, enquanto o fluxo de cadastro atribui automaticamente permissões de usuário padrão.

### Feed Principal (GamesListActivity)
Apresenta uma grade organizada de títulos contendo informações cruciais (capa, gênero e média). Possui uma barra de busca com filtragem por texto em tempo real e um sistema de BottomSheet para filtros avançados, onde é possível selecionar gêneros específicos e definir uma nota de corte mínima através de uma interface de estrelas.

### Página do Jogo (GamePageActivity)
Centraliza todas as informações de um título específico. Implementa o padrão "Your Review" no topo da seção de comentários, garantindo que a própria avaliação do usuário tenha prioridade visual. O sistema de média aritmética é dinâmico, recalculando o score do jogo a cada nova interação da comunidade.

### Gestão de Perfil (ProfileActivity)
Oferece uma visão consolidada da atividade do usuário. Gerencia a exibição de avatares com tratamento de erro e placeholders inteligentes. A seção de favoritos permite um acesso rápido aos jogos de interesse, criando um atalho direto para as páginas de detalhe.

---

## 4. Arquitetura e Pilha Tecnológica

### Padronização: Repository Pattern
O aplicativo adota a arquitetura de repositórios para desacoplar a interface de usuário da fonte de dados (Firebase). Isso facilita a manutenção e garante que a lógica de negócio esteja isolada das particularidades da API de banco de dados.

### Tecnologias Utilizadas
- **Kotlin:** Linguagem base para desenvolvimento nativo.
- **Firebase Firestore:** Banco de dados NoSQL orientado a documentos com capacidades de sincronização em tempo real.
- **Glide:** Biblioteca de carregamento de imagens com foco em performance e cache eficiente.
- **Material Design 3:** Implementação dos padrões visuais mais recentes do Google para uma interface limpa e profissional.

---

## 5. Modelagem de Dados (Firestore)

### Coleção: `users`
Armazena perfis de usuários. Documentos identificados por e-mail contendo nome, URL do avatar, tipo de conta (user/admin) e uma sub-coleção de IDs para favoritos.

### Coleção: `games`
Catálogo global de jogos. Inclui campos para nome, categoria, desenvolvedora, data de lançamento, descrição, URL da imagem de capa e a média ponderada das avaliações.

### Coleção: `reviews`
Registros individuais de opiniões. Cada documento vincula um usuário a um jogo, armazenando a nota numérica e o comentário textual.

---

## 6. Diretrizes para Instalação e Execução

Para configurar o ambiente de desenvolvimento:
1. Certifique-se de utilizar o **Android Studio Ladybug** (ou versão superior).
2. Clone o repositório e importe o projeto via Gradle.
3. Integre o arquivo `google-services.json` no diretório `/app`.
4. Configure as regras de segurança do Firestore para permitir acesso a usuários autenticados.
5. Sincronize as dependências e execute o aplicativo em um ambiente com API 24+.

---

## 7. Histórico de Versões

### [v1.0.0](https://github.com/Arthur-Manzatto/ratebit/releases/tag/v1.0.0) - Lançamento Inicial
- Implementação da arquitetura base e integração com Firebase.
- Sistema completo de autenticação e perfis.
- Feed dinâmico de jogos com busca e filtros.
- Sistema de avaliações interativas e gestão de favoritos.

---

## 8. Equipe de Desenvolvimento

- **Arthur Manzatto**
- **Eduardo Scudeler**
- **Lucas Cirino**

---
*Documentação atualizada em Abril de 2024*
