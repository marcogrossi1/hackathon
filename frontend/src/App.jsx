import { Navigate, Route, Routes } from 'react-router-dom'
import { AppShell } from './layout/AppShell.jsx'
import { AtivosPage } from './pages/AtivosPage.jsx'
import { OrdensServicoPage } from './pages/OrdensServicoPage.jsx'

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<AppShell />}>
        <Route index element={<Navigate to="/ativos" replace />} />
        <Route path="ativos" element={<AtivosPage />} />
        <Route path="ordens-servico" element={<OrdensServicoPage />} />
      </Route>
    </Routes>
  )
}
