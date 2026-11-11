import { Outlet } from "react-router-dom";
import "./App.css";
import MessageBox from './components/MessageBox';

function App() {

  return (
    <>
      <MessageBox/>
      <Outlet/>
    </>
  );
}
export default App;