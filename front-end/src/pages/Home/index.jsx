import { useEffect, useRef, useState } from "react";
import usePrestadores from "../../hooks/usePrestadores";
import categorias from "../../constants/categorias";
import Cabecalho from "../../components/Cabecalho";
import CardPrestador from "../../components/CardPrestador";
import { normalizar } from "../../utils/normalizar";
import Rodape from "../../components/Rodape";
import Hero from "../../components/Hero";
import BannerPrestador from "../../components/BannerPrestador";
import JanelaFormulario from "../../components/InformacoesPrestador";

import styles from "./Home.module.css";

function Home() {
  const dialogRef = useRef(null);
  const [busca, setBusca] = useState("");
  const [cidade, setCidade] = useState("");
  const [categoria, setCategoria] = useState("Todos");
  const [somenteMelhores, setSomenteMelhores] = useState(false);
  const [somenteFavoritos, setSomenteFavoritos] = useState(false);
  const [favoritos, setFavoritos] = useState([]);
  const [ordenacao, setOrdenacao] = useState("avaliacao");
  const [modal, setModal] = useState(null);
  const [profissionalSelecionado, setProfissionalSelecionado] = useState(null);
  const [tipoCadastro, setTipoCadastro] = useState("cliente");
  const [cadastroDemonstrado, setCadastroDemonstrado] = useState(false);
  const [mostrarContato, setMostrarContato] = useState(false);

  useEffect(() => {
    if (!modal) return;
    const dialog = dialogRef.current;
    const previous = document.activeElement;
    const overflow = document.body.style.overflow;
    if (dialog && !dialog.open) dialog.showModal();
    document.body.style.overflow = "hidden";
    return () => {
      if (dialog?.open) dialog.close();
      document.body.style.overflow = overflow;
      if (previous instanceof HTMLElement) previous.focus();
    };
  }, [modal]);

  const {prestadores} = usePrestadores();

  const listaFiltrada = prestadores
    .filter((profissional) => {
      const textoBusca = normalizar(
        `${profissional.nome} ${profissional.categoria} ${profissional.descricao}`
      );
      const correspondeBusca = textoBusca.includes(normalizar(busca));
      const correspondeCidade = normalizar(
        profissional.regiao
      ).includes(normalizar(cidade));
      const correspondeCategoria =
        categoria === "Todos" ||
        profissional.categoria === categoria;
      const correspondeNota =
        !somenteMelhores || profissional.nota >= 4.8;
      const correspondeFavorito =
        !somenteFavoritos || favoritos.includes(profissional.id);
      return (
        correspondeBusca &&
        correspondeCidade &&
        correspondeCategoria &&
        correspondeNota &&
        correspondeFavorito
      );
    })
    .sort((a, b) => {
      if (ordenacao === "preco") {
        return a.preco - b.preco;
      }
      if (ordenacao === "nome") {
        return a.nome.localeCompare(b.nome, "pt-BR");
      }
      return b.nota - a.nota;
    });
  function alternarFavorito(id) {
    setFavoritos((atuais) =>
      atuais.includes(id)
        ? atuais.filter((favoritoId) => favoritoId !== id)
        : [...atuais, id]
    );
  }
  function limparFiltros() {
    setBusca("");
    setCidade("");
    setCategoria("Todos");
    setSomenteMelhores(false);
    setSomenteFavoritos(false);
    setOrdenacao("avaliacao");
  }

  function abrirCadastro(tipo) {
    setTipoCadastro(tipo);
    setCadastroDemonstrado(false);
    setModal("cadastro");
  }

  function abrirPerfil(profissional) {
    setProfissionalSelecionado(profissional);
    setMostrarContato(false);
    setModal("perfil");
  }

  function irParaProfissionais() {
    document.getElementById("profissionais")?.scrollIntoView({
      behavior: "smooth",
    });
  }
  function realizarBusca(evento) {
    evento.preventDefault();
    irParaProfissionais();
  }
  return (
    <>
      <Cabecalho
        cidade={cidade}
        abrirCadastro={abrirCadastro}
      />

      <main className={styles.conteudoPrincipal}>
        <Hero
          irParaProfissionais={irParaProfissionais}
        />

        <form className={styles.barraBusca} onSubmit={realizarBusca}>
          <label className={styles.campoBusca}>
            <span aria-hidden="true">⌕</span>
            <input
              type="search"
              placeholder="Qual serviço você precisa?"
              aria-label="Pesquisar serviço ou profissional"
              value={busca}
              onChange={(evento) => setBusca(evento.target.value)}
            />
          </label>

          <label className={styles.campoBusca+" "+styles["campoCidade"]}>
            <span aria-hidden="true">⌖</span>
            <input
              placeholder="Cidade ou região"
              aria-label="Cidade ou região"
              value={cidade}
              onChange={(evento) => setCidade(evento.target.value)}
            />
          </label>
          <button className={styles.botao+" "+styles["botaoPrincipal"]} type="submit">
            Buscar →
          </button>
        </form>

        <section className={styles.secaoCategorias}>
          <div className={styles.tituloSecao}>
            <h2>O que precisa resolver?</h2>
            <p>Um profissional para cada necessidade.</p>
          </div>

          <div className={styles.listaCategorias}>
            {categorias.map((item) => (
              <button
                key={item.nome}
                className={styles.categoria+" "+styles[(categoria === item.nome) ? "categoria-ativa" : ""]}
                onClick={() => setCategoria(item.nome)}
                aria-pressed={categoria === item.nome}
              >
                <span className={styles.categoriaIcone} aria-hidden="true">
                  {item.icone}
                </span>
                <span>{item.nome}</span>
              </button>
            ))}
          </div>
        </section>

        <section id="profissionais" className={styles.secaoProfissionais}>
          <div className={styles.tituloSecao}>
            <div>
              <h2>Encontre seu próximo profissional</h2>
              <p>
                {listaFiltrada.length} profissionais encontrados
                · dados demonstrativos
              </p>
            </div>
            <span className={styles.textoDestaque}>
              Conheça. Compare. Escolha.
            </span>
          </div>
          
          <div className={styles.barraFiltros}>
            <button
              className={styles.filtro+" "+styles[(somenteFavoritos) ? "filtro-ativo" : ""]}
              onClick={() => setSomenteMelhores(!somenteMelhores)}
              aria-pressed={somenteMelhores}
            >
              ★ Nota 4,8 ou mais
            </button>
            <button
              className={styles.filtro+" "+styles[(somenteFavoritos) ? "filtro-ativo" : ""]}
              onClick={() => setSomenteFavoritos(!somenteFavoritos)}
              aria-pressed={somenteFavoritos}
            >
              ♡ Meus favoritos
            </button>
            <button className={styles.filtro} onClick={limparFiltros}>
              Limpar filtros
            </button>
            <select
              className={styles.ordenacao}
              aria-label="Ordenar profissionais"
              value={ordenacao}
              onChange={(evento) => setOrdenacao(evento.target.value)}
            >
              <option value="avaliacao">Melhor avaliação</option>
              <option value="preco">Menor valor de referência</option>
              <option value="nome">Nome: A–Z</option>
            </select>
          </div>

          <div className={styles.gradeProfissionais}>
            {listaFiltrada.map((prestador) => (
              <CardPrestador
                key={prestador.usuario_id}
                prestador={prestador}
                favorito={favoritos.includes(prestador.usuario_id)}
                aoFavoritar={() => alternarFavorito(prestador.usuario_id)}
                aoAbrir={() => abrirPerfil(prestador)}
              />
            ))}
            
            {listaFiltrada.length === 0 && (
              <div className={styles.estadoVazio}>
                <span>⌕</span>
                <h3>Nenhum profissional encontrado</h3>
                <p>
                  Tente outra categoria ou região. Há exemplos em
                  Paulínia e Campinas.
                </p>

                <button
                  className={styles.botao+" "+styles["botaoPrincipal"]}
                  onClick={limparFiltros}
                >
                  Limpar filtros
                </button>
              </div>
            )}
          </div>

          <p className={styles.avisoPrecos}>
            Valores de referência: o preço final deve ser combinado
            diretamente com o prestador. Serviços com unidades
            diferentes não são diretamente comparáveis.
          </p>
        </section>

        <BannerPrestador
          abrirCadastro={abrirCadastro}
        />
      </main>

      <Rodape/>

      <JanelaFormulario
        modal={modal}
        dialogRef={dialogRef}
        cadastroDemonstrado={cadastroDemonstrado}
        tipoCadastro={tipoCadastro}
        setModal={setModal}
        profissionalSelecionado={profissionalSelecionado}
        mostrarContato={mostrarContato}
        setMostrarContato={setMostrarContato}
      />
    </>
  );
}
export default Home;