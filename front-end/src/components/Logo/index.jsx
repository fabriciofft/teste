import styles from './Logo.module.css';

export default function Logo() {

  return (
    <a className={styles.logo} href="#" aria-label="QuemResolve, início">
      quem<span>resolve</span>
      <span className={styles.logoPonto}>.</span>
    </a>
  );
}