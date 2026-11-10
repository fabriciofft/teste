import styles from './CardPrestador.module.css';

import { moeda } from "../../utils/moeda";

export default function CardPrestador({prestador,favorito,aoFavoritar,aoAbrir}) {
  const iniciais = prestador.nome
  .split(" ")
  .map((parte) => parte[0])
  .slice(0, 2)
  .join("");

  return (
    <article className={styles.cardProfissional}>
      <div
        className={styles.cardCapa}
        style={{ backgroundColor: prestador.cor }}
      >
        <span className={styles.cardCategoria}>
          {prestador.categoria}
        </span>

        <button
          className={styles.botaoFavorito+" "+styles[(favorito) ? "favoritado" : ""]}
          onClick={aoFavoritar}
          aria-label={
            favorito
              ? `Remover ${prestador.nome} dos favoritos`
              : `Adicionar ${prestador.nome} aos favoritos`
          }
          aria-pressed={favorito}
        >
          {favorito ? "♥" : "♡"}
        </button>

        <span className={styles.avatar}>{iniciais}</span>

        <span className={styles.cardIcone} aria-hidden="true">
          {prestador.icone}
        </span>
      </div>

      <div className={styles.cardConteudo}>
        <h3>{prestador.nome}</h3>

        <p className={styles.cardDescricao}>
          {prestador.descricao}
        </p>

        <div className={styles.cardAvaliacao}>
          <strong>
            ★ {prestador.nota.toFixed(1).replace(".", ",")}
          </strong>

          <span>({prestador.avaliacoes} avaliações)</span>
        </div>

        <p className={styles.cardLocalizacao}>
          ⌖ {prestador.regiao}
        </p>

        <div className={styles.cardRodape}>
          <div className={styles.cardPreco}>
            <span>A partir de</span>
            <strong>
              {moeda(prestador.preco)}
              <small> / {prestador.unidade}</small>
            </strong>
          </div>
          
          <button className={styles.botaoPerfil} onClick={aoAbrir}>
            Ver perfil ↗
          </button>
        </div>
      </div>
    </article>
  );
}