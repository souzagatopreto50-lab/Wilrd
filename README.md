# MapLens Android

Aplicativo Android inicial para importar e visualizar capturas de tela escolhidas pelo usuário.

## O que esta versão faz
- Abre imagens pelo seletor de documentos do Android.
- Exibe a imagem e suas dimensões.
- Não solicita root nem permissões de captura de tela.
- Não lê dados internos do jogo, não acompanha partidas ao vivo, não automatiza comandos e não tenta contornar sistemas anti-cheat.

## Compilar
Requisitos: JDK 17, Android SDK 35 e Gradle 8.9.

```bash
gradle assembleDebug
```

O APK de depuração será gerado em `app/build/outputs/apk/debug/app-debug.apk`.

## CI
O GitHub Actions compila o APK em cada push/PR para `main` e publica o arquivo como artefato da execução.
