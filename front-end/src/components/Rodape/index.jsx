import styles from './Rodape.module.css';

import Logo from "../Logo";

export default function Rodape(){

  return(
    <footer className={styles.rodape}>
      <Logo />
      <p>Protótipo visual · Dados fictícios.</p>
      <p>QuemResolve · Projeto Integrador IV · 2026</p>
    </footer>
  );
}