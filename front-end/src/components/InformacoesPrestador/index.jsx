import styles from './InformacoesPrestador.module.css';

import { moeda } from "../../utils/moeda";

export default function InformacoesPrestador({modal, dialogRef, setModal, profissionalSelecionado, mostrarContato, setMostrarContato}){

  return(
    <>
      {modal && (
        <dialog
          className={styles.modal}
          aria-labelledby="titulo-modal"
          ref={dialogRef}
          onCancel={(evento) => { evento.preventDefault(); setModal(null); }}
          onClick={(evento) => {
            if (evento.target === evento.currentTarget) {
              const limites = evento.currentTarget.getBoundingClientRect();
              if (
                evento.clientX < limites.left ||
                evento.clientX > limites.right ||
                evento.clientY < limites.top ||
                evento.clientY > limites.bottom
              ) {
                setModal(null);
              }
            }
          }}
        >
          <button
            className={styles.fecharModal}
            onClick={() => setModal(null)}
            aria-label="Fechar janela"
            autoFocus
          >
            ✕
          </button>
          {modal === "perfil" && profissionalSelecionado && (
            <>
              <span className={styles.sobretitulo}>
                {profissionalSelecionado.categoria}
              </span>
              <h2 id="titulo-modal">
                {profissionalSelecionado.nome}
              </h2>
              <p className={styles.notaModal}>
                ★{" "}
                {profissionalSelecionado.nota
                  .toFixed(1)
                  .replace(".", ",")}
                {" · "}
                {profissionalSelecionado.avaliacoes} avaliações
                demonstrativas
              </p>
              <p>{profissionalSelecionado.sobre}</p>
              <div className={styles.informacaoModal}>
                <strong>Região de atendimento</strong>
                <p>{profissionalSelecionado.regiao}</p>
              </div>
              <div className={styles.informacaoModal}>
                <strong>Valor de referência</strong>
                <p>
                  A partir de {moeda(profissionalSelecionado.preco)}
                  {" / "}
                  {profissionalSelecionado.unidade}
                </p>
              </div>
              <div className={styles.avisoModal}>
                {mostrarContato
                  ? "Este profissional é fictício e não possui um contato real. No sistema final, aqui será aberto o WhatsApp, telefone ou e-mail cadastrado."
                  : "A negociação acontece fora da plataforma, diretamente com o profissional."}
              </div>
              <button
                className={styles.botao+" "+styles["botaoPrincipal", "botaoCompleto"]}
                onClick={() => setMostrarContato(true)}
              >
                Consultar contato ↗
              </button>
            </>
          )}
        </dialog> 
      )}
    </>
  );
}