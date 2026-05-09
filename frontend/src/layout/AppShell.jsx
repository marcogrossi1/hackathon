import { NavLink, Outlet } from 'react-router-dom'
import logoImg from '../assets/logo.png'
import '../App.css'

export function AppShell() {
  return (
    <div className="erp-root">
      <aside className="erp-sidebar" aria-label="Navegação principal">
        <div className="erp-sidebar__brand">
          <img src={logoImg} alt="" className="erp-sidebar__logo" width={40} height={40} />
          <span className="erp-sidebar__title">Gestão</span>
        </div>
        <nav className="erp-nav">
          <NavLink to="/ativos" className="erp-nav__link">
            Ativos
          </NavLink>
          <NavLink to="/ordens-servico" className="erp-nav__link">
            Ordens de Serviço
          </NavLink>
        </nav>
      </aside>
      <div className="erp-main">
        <header className="erp-header">
          <div className="erp-header__left">
            <img src={logoImg} alt="" className="erp-header__logo" width={36} height={36} />
            <div>
              <h1 className="erp-header__product">PreventechAI</h1>
              <p className="erp-header__tagline">Operações e serviços</p>
            </div>
          </div>
        </header>
        <main className="erp-content">
          <Outlet />
        </main>
      </div>
    </div>
  )
}
