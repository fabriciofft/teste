
import CabecalhoVoltar from "../../components/CabecalhoVoltar";
import Container from "../../components/Container";
import useUsuario from "../../hooks/useUsuario";

import styles from "./Cadastro.module.css";

export default function Cadastro() {

  const {
    usuario,
    preencherUsuario,
    enviarFormularioCadastro
  } = useUsuario();

  return (
    <>
      <CabecalhoVoltar
        destino={"/"}
      />
    
       <Container classeAdicional={"centralizar"}>

      <div className={styles.cadastro}>
        <span className={styles.subtitulo}>
          Faça parte do QuemResolve
        </span>

        <h1 className={styles.titulo}>
          Seu próximo passo começa aqui.
        </h1>

        <form onSubmit={enviarFormularioCadastro}>

          <div className={styles.campo}>
            <label htmlFor="role">
              Quero me cadastrar como
            </label>

            <select
              id="role"
              name="role"
              value={usuario.role}
              onChange={preencherUsuario}
            >
              <option value="cliente">Cliente</option>
              <option value="prestador">Prestador</option>
            </select>
          </div>

          <div className={styles.campo}>
            <label htmlFor="nome">
              Nome completo
            </label>

            <input
              type="text"
              id="nome"
              name="nome"
              value={usuario.nome}
              onChange={preencherUsuario}
              placeholder="Digite seu nome completo"
              autoComplete="nome"
              required
            />
          </div>

          <div className={styles.campo}>
            <label htmlFor="email">
              E-mail
            </label>

            <input
              type="email"
              id="email"
              name="email"
              value={usuario.email}
              onChange={preencherUsuario}
              placeholder="Digite seu e-mail"
              autoComplete="email"
              required
            />
          </div>

          <div className={styles.campo}>
            <label htmlFor="password">
              Senha
            </label>

            <input
              type="password"
              id="senha"
              name="senha"
              value={usuario.senha}
              onChange={preencherUsuario}
              placeholder="Crie uma senha"
              autoComplete="new-senha"
              required
            />
          </div>

          <button
            className={styles.botao}
            disabled={!usuario.nome || !usuario.email || !usuario.senha}
          >
            Cadastrar
          </button>
        </form>
      </div>
    </Container>
    </>
  );
}