import Home from './Pages/Home'
import Login from './Pages/Login'
import Register from './Pages/Register'
import { BrowserRouter, Routes, Route} from 'react-router-dom'
import './App.css'

function App() {

  return (
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<Login/>}/>
          <Route path='/login' element={<Login />}/>
          <Route path='/register' element={<Register />}/>
          <Route path='/home' element={<Home/>}/>
          <Route path='*' element={<h1>ERROR 404 - Not found</h1>}/>
        </Routes>
      </BrowserRouter>
  )
}

export default App
