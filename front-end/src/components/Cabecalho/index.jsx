import styles from './Cabecalho.module.css';
import Logo from "../Logo";
import BotaoLink from '../BotaoLink';
import { Link } from 'react-router-dom';

export default function Cabecalho({cidade}){

  return(
    <header className={styles.cabecalho}>
      <nav className={styles.navegacao} aria-label="Navegação principal">
        <Logo />

        <div className={styles.linksNavegacao}>
          <a className={styles.linkAtivo} href="#profissionais">
            Encontrar profissionais
          </a>
          
          <Link to={"/perfil"}>
            Meu perfil
          </Link>
        </div>

        <div className={styles.regiaoCabecalho}>
          <span>Buscar na região</span>
          <strong>⌖ {cidade || "Paulínia e Campinas"}</strong>
        </div>

        <div className={styles.botoes}>
          <BotaoLink
            texto={"Cadastro"}
            destino={"/cadastro"}
            classeAdicional={"margemADireita"}
          />

          <BotaoLink
            texto={"Login"}
            destino={"/login"}
          />
        </div>
      </nav>
    </header>
  );
}