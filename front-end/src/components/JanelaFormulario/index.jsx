import styles from './JanelaFormulario.module.css';

import { moeda } from "../../utils/moeda";

export default function JanelaFormulario({modal, dialogRef, cadastroDemonstrado, tipoCadastro, setModal, profissionalSelecionado, mostrarContato, setMostrarContato}){

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
          {modal === "cadastro" && (
            <>
              <span className={styles.sobretitulo}>
                Faça parte do QuemResolve
              </span>
              <h2 id="titulo-modal">
                {cadastroDemonstrado
                  ? "Você conheceu a etapa de cadastro!"
                  : "Seu próximo passo começa aqui."}
              </h2>
              {cadastroDemonstrado ? (
                <>
                  <p>
                    No sistema completo, esta etapa criará a conta.
                    Prestadores também informarão serviços, região
                    de atendimento e valores de referência.
                  </p>
                  <div className={styles.avisoModal}>
                    Nenhum dado foi enviado ou armazenado.
                  </div>
                  <button
                    className={styles.botao+" "+styles["botaoPrincipal", "botaoCompleto"]}
                    onClick={() => setModal(null)}
                  >
                    Voltar à busca
                  </button>
                </>
              ) : (
                <form
                  className={styles.formularioCadastro}
                  onSubmit={(evento) => {
                    evento.preventDefault();
                    setCadastroDemonstrado(true);
                  }}
                >
                  <label htmlFor="tipo-cadastro">
                    Quero me cadastrar como
                  </label>
                  <select
                    id="tipo-cadastro"
                    value={tipoCadastro}
                    onChange={(evento) =>
                      setTipoCadastro(evento.target.value)
                    }
                  >
                    <option value="cliente">Cliente</option>
                    <option value="prestador">Prestador</option>
                  </select>
                  <label htmlFor="nome-cadastro">Nome completo</label>
                  <input
                    id="nome-cadastro"
                    autoComplete="name"
                    required
                  />
                  <label htmlFor="email-cadastro">E-mail</label>
                  <input
                    id="email-cadastro"
                    type="email"
                    autoComplete="email"
                    required
                  />
                  <div className={styles.avisoModal}>
                    Demonstração da interface. Os dados não serão
                    enviados nem armazenados.
                  </div>
                  <button
                    className={styles.botao+" "+styles["botaoPrincipal", "botaoCompleto"]}
                    type="submit"
                  >
                    Visualizar cadastro →
                  </button>
                </form>
              )}
            </>
          )}
        </dialog> 
      )}
    </>
  );
}