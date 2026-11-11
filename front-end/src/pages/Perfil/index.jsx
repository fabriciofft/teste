
import { useState } from "react";

import Container from "../../components/Container";
import CabecalhoVoltar from '../../components/CabecalhoVoltar';
import usePerfil from "../../hooks/usePerfil";
import styles from "./Perfil.module.css";
import useUsuario from "../../hooks/useUsuario";

export default function Perfil() {

  const {meusDados} = useUsuario();
  const [exibirFormulario, setExibirFormulario] = useState(false);

  const {
    perfil, perfilValido,
    preencherPerfil,
    enviarFormularioSalvarPerfil
  } = usePerfil(meusDados?.id || meusDados?.usuario_id);
  
  return (
    <>
      <CabecalhoVoltar
        destino={"/"}
      />

      <Container classeAdicional="centralizar">
        <main className={styles.containerPerfil}>
          <section className={styles.perfil}>

            <span className={styles.subtitulo}>
              Minha conta
            </span>

            <h1 className={styles.titulo}>
              Meu perfil de {meusDados.user?.role}
            </h1>

            <div className={styles.campo}>
              <label>Nome completo</label>
              <div className={styles.valor}>
                {meusDados.user?.nome || "Não informado"}
              </div>
            </div>

            <div className={styles.campo}>
              <label>E-mail</label>
              <div className={styles.valor}>
                {meusDados.user?.email || "Não informado"}
              </div>
            </div>

            <div className={styles.campo}>
              <label>Senha</label>
              <div className={styles.valor}>
                ••••••••••••
              </div>
            </div>

            {meusDados.user?.role === "prestador" && meusDados.profile == null && (
              <button
                type="button"
                className={styles.botao}
                onClick={() => setExibirFormulario(!exibirFormulario)}
              >
                {exibirFormulario ? "Cancelar" : "Criar perfil →"}
              </button>
            )}

          </section>

          {(
            meusDados.profile != null ||
            (
              meusDados.user?.role === "prestador" &&
              exibirFormulario
            )
          ) && (
            <section className={styles.formulario}>
              <span className={styles.subtitulo}>
                Seja encontrado por clientes
              </span>

              <h2 className={styles.titulo}>
                {meusDados.profile ? "Editar perfil profissional" : "Criar perfil profissional"}
              </h2>

              <form onSubmit={(e) => enviarFormularioSalvarPerfil(e, meusDados.user?.id)}>

                <div className={styles.campo}>
                  <label>Categoria de atuação</label>

                  <div className={styles.listaCategorias}>
                    {[
                      "Eletricista",
                      "Encanador",
                      "Limpeza",
                      "Pintor",
                      "Jardinagem",
                      "Montador",
                      "Ar-condicionado",
                      "Reformas"
                    ].map((categoria) => {
                      const categoriasSelecionadas = Array.isArray(perfil?.categoria)
                        ? perfil.categoria
                        : typeof perfil?.categoria === "string"
                          ? perfil.categoria.split(",").map((item) => item.trim())
                          : [];

                      return (
                        <label key={categoria} className={styles.opcaoCategoria}>
                          <input
                            type="checkbox"
                            name="categoria"
                            value={categoria}
                            checked={categoriasSelecionadas.includes(categoria)}
                            onChange={preencherPerfil}
                          />

                          <span>{categoria}</span>
                        </label>
                      );
                    })}
                  </div>
                </div>

                <div className={styles.campo}>
                  <label>Título profissional</label>
                  <input
                    type="text"
                    name="titulo"
                    value={perfil?.titulo ?? ""}
                    onChange={preencherPerfil}
                    placeholder="Ex: Pintor residencial"
                    required
                  />
                </div>

                <div className={styles.campo}>
                  <label>Biografia</label>
                  <textarea
                    name="biografia"
                    value={perfil?.biografia ?? ""}
                    onChange={preencherPerfil}
                    placeholder="Conte um pouco sobre seu trabalho"
                    required
                  />
                </div>

                <div className={styles.campo}>
                  <label>Cidade</label>
                  <input
                    type="text"
                    name="cidade"
                    value={perfil?.cidade ?? ""}
                    onChange={preencherPerfil}
                    placeholder="Ex: Paulínia"
                    required
                  />
                </div>

                <div className={styles.campo}>
                  <label>Região de atendimento</label>
                  <input
                    type="text"
                    name="regiao"
                    value={perfil?.regiao ?? ""}
                    onChange={preencherPerfil}
                    placeholder="Ex: Campinas e região"
                    required
                  />
                </div>

                <div className={styles.campo}>
                  <label>Unidade de cobrança</label>

                  <select
                    name="unidade"
                    value={perfil?.unidade ?? ""}
                    onChange={preencherPerfil}
                    required
                  >
                    <option value="" disabled>
                      Selecione uma unidade
                    </option>

                    <option value="por serviço">por serviço</option>
                    <option value="por hora">por hora</option>
                    <option value="por diária">por diária</option>
                    <option value="a combinar">a combinar</option>
                  </select>
                </div>

                <div className={styles.campo}>
                  <label>Celular / WhatsApp</label>
                  <input
                    type="tel"
                    name="celular"
                    value={perfil?.celular ?? ""}
                    onChange={preencherPerfil}
                    placeholder="Ex: 19990141823"
                    required
                  />
                </div>

                <div className={styles.campo}>
                  <label>Raio de atendimento (km)</label>
                  <input
                    type="number"
                    name="raio"
                    min="0"
                    step="any"
                    value={perfil?.raio ?? ""}
                    onChange={preencherPerfil}
                    placeholder="Ex: 90"
                    required
                  />
                </div>

                <div className={styles.campo}>
                  <label>Preço de referência (R$)</label>
                  <input
                    type="number"
                    name="preco"
                    min="0"
                    step="any"
                    value={perfil?.preco ?? ""}
                    onChange={preencherPerfil}
                    placeholder="Ex: 200"
                    required
                  />
                </div>

                <div className={styles.campo}>
                  <label>Latitude (opcional)</label>
                  <input
                    type="number"
                    name="latitude"
                    step="any"
                    value={perfil?.latitude ?? ""}
                    onChange={preencherPerfil}
                    placeholder="Latitude"
                  />
                </div>

                <div className={styles.campo}>
                  <label>Longitude (opcional)</label>
                  <input
                    type="number"
                    name="longitude"
                    step="any"
                    value={perfil?.longitude ?? ""}
                    onChange={preencherPerfil}
                    placeholder="Longitude"
                  />
                </div>

                <button
                  type="submit"
                  className={styles.botao}
                  disabled={!perfilValido}
                >
                  {meusDados.profile ? "Atualizar perfil →" : "Salvar perfil →"}
                </button>

              </form>

            </section>
          )}
        </main>
      </Container>
    </>
  );
}