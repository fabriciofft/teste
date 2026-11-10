import styles from './BannerPrestador.module.css';

export default function BannerPrestador({abrirCadastro}){

  return(
    <section className={styles.bannerPrestador}>
      <div>
        <span className={styles.sobretitulo}>Para quem faz acontecer</span>
        <h2>Seu talento merece ser encontrado.</h2>
        <p>
          Apresente seus serviços e conecte-se com clientes
          da sua região.
        </p>
      </div>
      <button
        className={styles.botao+" "+styles["botaoBranco"]}
        onClick={() => abrirCadastro("prestador")}
      >
        Quero ser prestador ↗
      </button>
    </section>
  );
}