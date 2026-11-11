
import CabecalhoVoltar from "../../components/CabecalhoVoltar";
import Container from "../../components/Container";
import useUsuario from "../../hooks/useUsuario";
import styles from "./Login.module.css";

export default function Login() {

  const {
    usuario,
    preencherUsuario,
    enviarFormularioLogin
  } = useUsuario();

  return (
    <>
      <CabecalhoVoltar
        destino={"/"}
      />

      <Container classeAdicional={"centralizar"}>
        <div className={styles.login}>

          <span className={styles.subtitulo}>
            Bem-vindo de volta ao QuemResolve
          </span>

          <h1 className={styles.titulo}>
            Que bom ter você por aqui.
          </h1>

          <form onSubmit={enviarFormularioLogin}>

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
              <label htmlFor="senha">
                Senha
              </label>

              <input
                type="password"
                id="senha"
                name="senha"
                value={usuario.senha}
                onChange={preencherUsuario}
                placeholder="Digite sua senha"
                autoComplete="current-password"
                required
              />
            </div>

            <button
              className={styles.botao}
              disabled={!usuario.email || !usuario.senha}
            >
              Login
            </button>
          </form>
        </div>
      </Container>
    </>
  );
}
