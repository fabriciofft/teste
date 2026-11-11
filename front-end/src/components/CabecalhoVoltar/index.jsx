import styles from './CabecalhoVoltar.module.css';
import BotaoLink from '../BotaoLink';

export default function CabecalhoVoltar({destino}){

  return(
    <header className={styles.cabecalhoVoltar}>
      <div className={styles.margemCabecalhoVoltar}>
        <BotaoLink
          destino={destino}
          texto={"Voltar"}
        >
        </BotaoLink>
      </div>
    </header>
  );
}