import styles from './MessageBox.module.css';

import useMessageBox from '../../hooks/useMessageBox';

export default function MessageBox(){

  const {visibilidadeMessageBox, dados, esconderMessageBox} = useMessageBox();

  return(
    <>
      {
        (visibilidadeMessageBox && dados) && (
          <div className={styles.containerMessageBox}>
            <div className={styles.messageBox}>
              <div className={styles.margemMessageBox}>
                <div className={styles.mensagem}>
                  {
                    typeof(dados.msg) == "string" ? dados.msg 
                    : (
                      <>
                        {
                          dados.msg.map((linha, index) => (
                            <p key={index}>- {linha}</p>
                          ))
                        }
                      </>
                    )
                  }
                </div>

                <button 
                  type='button'
                  className={styles.botao}
                  onClick={esconderMessageBox}
                >
                  {dados.txtBotao}
                </button>
              </div>
            </div>
          </div>
        )
      }
    </>
  );
}