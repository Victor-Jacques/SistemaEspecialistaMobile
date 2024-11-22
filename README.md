# SistemaEspecialistaMob

Este é o código-fonte de um aplicativo móvel desenvolvido para a disciplina de Sistemas Especialistas da Universidade Veiga de Almeida, ministrada pelo professor Denis Gonçalves Cople.

## Descrição

O aplicativo é um quiz de personalidade que ajuda os usuários a entender melhor suas tendências e características pessoais. Responda a uma série de perguntas simples e receba um diagnóstico com base em seu perfil.

## Desenvolvedores

- Lucas Amaral
- Pedro Henrique da Silva Novais
- Pedro Henrique Sacramento Carvalho
- Victor Jacques

## Funcionalidades

- Quiz interativo de personalidade
- Interface amigável e intuitiva
- Resultados personalizados baseados nas respostas do usuário

## Requisitos

- Android Studio com o SDK mais recente
- JDK 17
- Gradle 8.7

## Como executar o projeto

1. Clone o repositório para a sua máquina local.
2. Abra o projeto no Android Studio.
3. Sincronize o Gradle e aguarde o download das dependências.
4. Conecte um dispositivo Android ou utilize um emulador.
5. Execute o aplicativo.

## Configurações adicionais

O aplicativo utiliza comunicação via rede local. Certifique-se de que o endereço IP do servidor está correto no arquivo `network_security_config.xml`:

```xml
<domain includeSubdomains="true">10.0.2.2</domain>
```

Altere para o IP do seu servidor, se necessário.
