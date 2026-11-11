import styles from './BotaoLink.module.css';

import { Link } from "react-router-dom";

export default function BotaoLink({texto, destino, classeAdicional}){

  return(
    <Link 
      to={destino}
      className={styles.botaoLink+" "+styles[classeAdicional]}
    >
      {texto}
    </Link>
  );
}