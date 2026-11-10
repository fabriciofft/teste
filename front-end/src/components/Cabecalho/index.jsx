import styles from './Cabecalho.module.css';
import Logo from "../Logo";

export default function Cabecalho({abrirCadastro, cidade}){

  return(
    <header className={styles.cabecalho}>
      <nav className={styles.navegacao} aria-label="Navegação principal">
        <Logo />
        <div className={styles.linksNavegacao}>
          <a className={styles.linkAtivo} href="#profissionais">
            Encontrar profissionais
          </a>
          <button
            className={styles.linkBotao}
            onClick={() => abrirCadastro("prestador")}
          >
            Sou prestador
          </button>
        </div>
        <div className={styles.regiaoCabecalho}>
          <span>Buscar na região</span>
          <strong>⌖ {cidade || "Paulínia e Campinas"}</strong>
        </div>
        <button
          className={styles.botao+" "+styles["botaoSuave"]}
          onClick={() => abrirCadastro("cliente")}
        >
          Cadastrar
        </button>
      </nav>
    </header>
  );
}