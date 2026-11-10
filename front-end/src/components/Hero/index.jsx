import styles from './Hero.module.css';

export default function Hero({irParaProfissionais}){

  return(
    <section className={styles.hero}>
      <div className={styles.heroTexto}>
        <span className={styles.sobretitulo}>
          Gente boa, perto de você
        </span>

        <h1>
          Precisou?
          <br />
          Encontre quem <span>resolve.</span>
        </h1>

        <p>
          Da tomada ao jardim. Encontre profissionais na sua
          região, compare perfis e escolha com mais confiança.
        </p>
        
        <button
          className={styles.botao+" "+styles["botaoPrincipal"]}
          onClick={irParaProfissionais}
        >
          Encontrar um profissional →
        </button>
        <div className={styles.heroDetalhes}>
          <span>✓ Avaliações de clientes</span>
          <span>✓ Contato direto</span>
        </div>
      </div>
      <div className={styles.heroVisual} aria-hidden="true">
        <div className={styles.circuloDecorativo} />
        <div className={styles.ilustracaoCasa}>
          <svg viewBox="0 0 400 300" focusable="false">
            <path
              d="M45 267H355"
              stroke="#D8C3B4"
              strokeWidth="3"
            />
            <path
              d="M110 135L200 58L290 135V267H110Z"
              fill="#FFFDF8"
            />
            <path
              d="M95 137L200 47L305 137"
              fill="none"
              stroke="#CC2000"
              strokeWidth="18"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
            <rect
              x="179"
              y="180"
              width="46"
              height="87"
              rx="6"
              fill="#CC2000"
            />
            <circle cx="215" cy="225" r="3" fill="#E7BE76" />
            <rect
              x="130"
              y="155"
              width="32"
              height="39"
              rx="4"
              fill="#E4CFA8"
            />
            <rect
              x="242"
              y="155"
              width="32"
              height="39"
              rx="4"
              fill="#E4CFA8"
            />
            <path
              d="M146 155V194M130 174H162M258 155V194M242 174H274"
              stroke="#FFFDF8"
              strokeWidth="4"
            />
            <path
              d="M325 267V214"
              stroke="#CC2000"
              strokeWidth="5"
            />
            <ellipse
              cx="325"
              cy="208"
              rx="23"
              ry="36"
              fill="#7D9A82"
            />
            <rect
              x="309"
              y="244"
              width="32"
              height="25"
              rx="4"
              fill="#C79470"
            />
          </svg>
        </div>
        <span className={styles.ferramenta+" "+styles["ferramentaUm"]}>🔧</span>
        <span className={styles.ferramenta+" "+styles["ferramentaDois"]}>💡</span>
        <div className={styles.seloVisual}>
          <span className={styles.seloIcone}>✓</span>
          <div>
            <strong>Uma boa escolha começa aqui</strong>
            <span>Compare antes de entrar em contato.</span>
          </div>
        </div>
      </div>
    </section>
  );
}