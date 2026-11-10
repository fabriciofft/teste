# QuemResolve — frontend completo

1. Instale o Node.js 22 ou superior, caso não tenha.
2. Extraia o ZIP e abra a pasta quemresolve no VS Code.
3. No terminal dessa pasta (onde está package.json), execute:

```bash
npm install
npm run dev
```

Abra o endereço Local exibido no terminal. Não abra index.html diretamente.

## Arquivos
- src/App.jsx: componentes, dados demonstrativos, filtros, favoritos e modais.
- src/App.css: estilos completos da página e responsividade.
- src/index.css: cores e estilos globais.
- src/main.jsx: inicialização do React e importações.
- index.html: documento de entrada.
- vite.config.js: configuração do Vite.

## Funcionalidades
Busca por nome/serviço e região, categorias, filtro por nota, favoritos durante a sessão, ordenação, perfil e formulário demonstrativo.

Dados, avaliações e preços são fictícios. Cadastro não cria conta nem envia dados. Contato é demonstrativo. Favoritos reiniciam ao atualizar a página. Não há integração com backend Java ou banco de dados.

## Compilar
```bash
npm run build
npm run preview
```

Use os arquivos completos deste projeto para evitar misturar estilos do template anterior.
