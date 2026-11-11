import styles from './Container.module.css';

export default function Container({children, classeAdicional}){

  return(
    <div className={styles.container}>
      <div className={styles.margemContainer+" "+styles[classeAdicional]}>
        {children}
      </div>
    </div>
  )
}