# Ratebit - Documentação Completa

## Índice
1. [Visão Geral](#visão-geral)
2. [Funcionalidades](#funcionalidades)
3. [Tecnologias](#tecnologias)
4. [Estrutura do Projeto](#estrutura-do-projeto)
5. [Configuração e Instalação](#configuração-e-instalação)
6. [Guia de Uso](#guia-de-uso)
7. [Arquitetura](#arquitetura)
8. [Contato e Contribuições](#contato-e-contribuições)

---

## Visão Geral

Ratebit é uma plataforma social para fãs de games, desenvolvida em Kotlin para Android. O aplicativo permite que usuários descubram novos jogos, gerenciem seus favoritos e compartilhem opiniões através de um robusto sistema de avaliações.

### Versão: 1.0
- **Compilação (SDK):** 36
- **Versão Mínima (API):** 24 (Android 7.0)
- **Versão Alvo (API):** 36
- **Identificador do Pacote:** com.example.ratebit

---

## Funcionalidades

### Para Usuários

#### Autenticação
- Sistema seguro de login e cadastro
- Integração com Firebase Firestore
- Gerenciamento de sessão de usuário

#### Descoberta de Jogos
- Feed em tempo real com lista de jogos
- Busca por nome de jogo
- Filtros por categoria
- Filtro por avaliação mínima (sistema de 5 estrelas)

#### Detalhes do Jogo
- Informações completas:
  - Nome do desenvolvedor
  - Data de lançamento
  - Descrição detalhada
  - Avaliação média global

#### Sistema de Avaliações
- Avaliação intuitiva (1 a 5 estrelas)
- Seção "Sua Avaliação" em destaque no topo
- Edição de avaliações existentes
- Cálculo automático da avaliação média do jogo

#### Favoritos
- Salvação de jogos preferidos com um clique
- Sincronização em todas as telas
- Acesso rápido aos favoritos

#### Perfil Customizável
- Alteração de nome de usuário
- Mudança de foto de perfil via URL
- Salvar alterações de forma segura

### Para Administradores

#### Gerenciamento de Conteúdo
- Acesso exclusivo para adicionar novos jogos à plataforma
- Controle total sobre o catálogo

---

## Tecnologias Utilizadas

| Tecnologia | Versão | Propósito |
|---|---|---|
| Kotlin | 1.9+ | Linguagem principal |
| Firebase Firestore | Latest | Banco de dados em tempo real |
| Android Jetpack | Latest | Componentes modernos do Android |
| Material Design 3 | Latest | Design visual e componentes UI |
| Glide | Latest | Carregamento e cache de imagens |
| ConstraintLayout | Latest | Layouts complexos |
| RecyclerView | Latest | Listas de dados |
| ShapeableImageView | Latest | Avatares circulares

---

## Estrutura do Projeto

```
ratebit/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/ratebit/
│   │   │   │   ├── model/              # Classes de dados (Game, User, Review)
│   │   │   │   ├── repository/         # Lógica de comunicação com Firebase
│   │   │   │   ├── ui/                 # Activities e lógica da interface
│   │   │   │   │   ├── activities/     # Telas principais
│   │   │   │   │   └── adapter/        # Adaptadores para RecyclerView
│   │   │   │   └── utils/              # Funções utilitárias
│   │   │   ├── res/
│   │   │   │   ├── layout/             # Arquivos XML de layouts
│   │   │   │   ├── drawable/           # Imagens e vetores
│   │   │   │   ├── values/             # Cores, strings, dimensões
│   │   │   │   └── anim/               # Animações
│   │   │   └── AndroidManifest.xml     # Configuração da aplicação
│   │   ├── test/                       # Testes unitários
│   │   └── androidTest/                # Testes instrumentados
│   ├── build.gradle.kts                # Dependências e configuração Gradle
│   └── google-services.json            # Configuração Firebase
│
├── gradle/
│   ├── libs.versions.toml              # Versões centralizadas de dependências
│   └── wrapper/                        # Gradle Wrapper
│
├── build.gradle.kts                    # Configuração raiz do Gradle
├── settings.gradle.kts                 # Configuração de módulos
└── README.md                           # Informações gerais do projeto
```

---

## Configuração e Instalação

### Pré-requisitos
- Android Studio Electric Eel ou superior
- Java 11 ou superior
- Kotlin 1.9+
- Emulador ou dispositivo Android com API 24+

### Passo 1: Clonar o Repositório
```bash
git clone https://github.com/Arthur-Manzatto/ratebit.git
cd ratebit
```

### Passo 2: Abrir no Android Studio
1. Abra Android Studio
2. Selecione **File → Open**
3. Navegue até a pasta `ratebit` e clique **Open**
4. Aguarde a sincronização automática do Gradle

### Passo 3: Configurar Firebase

1. **Criar Projeto no Firebase:**
   - Acesse [Firebase Console](https://console.firebase.google.com/)
   - Clique em "Criar projeto"
   - Nomeie como "Ratebit"
   - Conclua a criação

2. **Adicionar App Android:**
   - No console, clique em "Adicionar app" e selecione Android
   - **Package name:** `com.example.ratebit`
   - **Nome do app (SHA-1):** Pode deixar em branco por enquanto
   - Clique em "Registrar app"

3. **Baixar google-services.json:**
   - Na etapa "Fazer download de google-services.json"
   - Clique no botão de download
   - Coloque o arquivo na pasta `app/`

4. **Configurar Firestore:**
   - No console, vá para "Firestore Database"
   - Clique em "Criar database"
   - Selecione **Modo de teste** (para desenvolvimento)
   - Escolha a localização mais próxima
   - Clique em "Criar"

5. **Criar Coleções no Firestore:**
   - Na aba "Coleções", clique em "Iniciar coleção"
   - Crie as seguintes coleções:
     - `users`
     - `games`
     - `reviews`

### Passo 4: Executar a Aplicação

1. **Conecte um dispositivo ou inicie um emulador:**
   - Emulador: **Tools → AVD Manager → Launch**
   - Ou conecte um dispositivo Android via USB

2. **Execute o projeto:**
   - Clique no botão **Run** (ícone de play verde)
   - Ou use o atalho **Shift + F10**

3. **Aguarde a compilação e instalação**
   - O app será instalado e iniciado automaticamente

---

## Download

### Versão Disponível

| Versão | Data | APK |
|--------|------|-----|
| v1.0.0 | 08/04/2026 | [Download](https://github.com/Arthur-Manzatto/ratebit/releases/tag/v1.0.0) |

Consulte a seção [Visão Geral](#visão-geral) para os requisitos do sistema.

---

## Guia de Uso

### Primeiro Acesso

#### 1. Criar uma Conta
```
1. Abra o aplicativo
2. Clique em "Criar Conta"
3. Preencha email e senha
4. Clique em "Cadastrar"
5. Você será redirecionado para o feed principal
```

#### 2. Fazer Login
```
1. Na tela de boas-vindas, clique "Entrar"
2. Insira seu email e senha
3. Clique em "Entrar"
```

### Usando o Aplicativo

#### Explorar Jogos
```
1. Na tela inicial (Feed), visualize a lista de jogos
2. Use a barra de busca para procurar por nome
3. Clique no jogo para ver detalhes completos
```

#### Filtrar Jogos
```
1. Clique no ícone de filtro (funil)
2. Selecione a categoria desejada
3. Ajuste a avaliação mínima com o slider
4. Clique em "Aplicar"
```

#### Avaliar um Jogo
```
1. Abra o jogo desejado
2. Role até a seção "Avaliações"
3. Clique nas estrelas para dar sua nota (1-5)
4. Adicione um comentário (opcional)
5. Clique em "Enviar Avaliação"
```

#### Adicionar aos Favoritos
```
1. Na página do jogo, clique no ícone de coração
2. O jogo será adicionado aos favoritos
3. Acesse a aba "Favoritos" para visualizar
```

#### Customizar Perfil
```
1. Na aba "Perfil", clique em "Editar"
2. Altere seu nome de usuário
3. Insira URL da sua foto de perfil
4. Clique em "Salvar"
```

---

## Arquitetura

### Padrão: Repository Pattern

O projeto utiliza o Repository Pattern para abstrair a lógica de acesso aos dados, garantindo separação de responsabilidades:

```
Activities (Apresentação)
    |
ViewModels (Lógica de negócio)
    |
Repositories (Abstração de dados)
    |
Firebase (Dados remotos)
```

### Componentes Principais

#### Models (Modelos de Dados)
- Game: Representa um jogo com ID, nome, desenvolvedor, etc.
- User: Dados do usuário (ID, email, nome, foto)
- Review: Avaliação do usuário (ID, jogo, autor, nota, comentário)

#### Repository
- GameRepository: Gerencia operações com jogos (listar, adicionar, buscar)
- UserRepository: Gerencia dados e autenticação de usuários
- ReviewRepository: Gerencia avaliações e comentários

#### UI/Activities
- LoginActivity: Tela de autenticação
- FeedActivity: Lista principal de jogos
- GameDetailActivity: Detalhes do jogo
- ProfileActivity: Perfil do usuário
- FavoritesActivity: Jogos favoritos

#### Adapters
- GamesAdapter: Adaptador para a lista de jogos
- ReviewsAdapter: Adaptador para a lista de avaliações

---

## Segurança

- Autenticação segura com Firebase
- Regras de Firestore para validação de dados
- Criptografia de senhas
- Sanitização de inputs de usuário

---

## Troubleshooting

### Problema: Firebase não sincroniza
Solução:
1. Verifique se google-services.json está na pasta app/
2. Sincronize o Gradle: File > Sync Now
3. Limpe o cache: Build > Clean Project

### Problema: Emulador não inicia
Solução:
1. Abra o AVD Manager
2. Clique em "Deletar" no emulador com problema
3. Crie um novo emulador
4. Inicie novamente

### Problema: Imagens não carregam
Solução:
1. Verifique URLs das imagens
2. Certifique-se que o Glide está configurado corretamente
3. Limpe o cache de imagens do dispositivo

---

## Contribuindo

Estamos sempre abertos a contribuições! Se você deseja contribuir:

1. Faça um Fork do repositório
2. Crie uma branch para sua feature (git checkout -b feature/MinhaFeature)
3. Commit suas mudanças (git commit -m 'Add: Minha Feature')
4. Push para a branch (git push origin feature/MinhaFeature)
5. Abra um Pull Request

### Diretrizes de Contribuição
- Mantenha o código bem comentado
- Siga o padrão de código Kotlin
- Adicione testes quando possível
- Atualize a documentação se necessário

---

## Desenvolvimento

### Principais Dependências

```kotlin
// AndroidX
androidx.core:core-ktx
androidx.appcompat:appcompat
androidx.activity:activity
androidx.constraintlayout:constraintlayout
androidx.recyclerview:recyclerview

// Material Design
com.google.android.material:material

// Firebase
com.google.firebase:firebase-firestore

// Carregamento de Imagens
com.github.bumptech.glide:glide

// Testes
junit:junit
androidx.test.ext:junit
androidx.test.espresso:espresso-core
```

---

## Contato e Suporte

### Desenvolvedores Principais
- Arthur Manzatto - GitHub
- Eduardo Scudeler - GitHub
- Lucas Cirino - GitHub

### Relatório de Bugs
Se encontrar um bug, abra uma Issue no repositório com:
- Descrição do problema
- Passos para reproduzir
- Screenshots (se aplicável)
- Dispositivo e versão do Android

### Sugestões de Features
Sugestões são bem-vindas! Crie uma Discussion com sua ideia.

---

## Licença

Este projeto está licenciado sob a MIT License - veja o arquivo LICENSE para detalhes.

---

## Roadmap Futuro

- Suporte a múltiplos idiomas
- Sistema de ranking de usuários
- Notificações em tempo real
- Compartilhamento em redes sociais
- Versão web do aplicativo
- Sistema de achievements/badges
- Chat entre usuários

---

Desenvolvido pela equipe Ratebit

Última atualização: Abril de 2026








