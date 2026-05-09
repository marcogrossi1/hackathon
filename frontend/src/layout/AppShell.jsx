import { useEffect, useRef, useState } from 'react'
import { NavLink, Outlet } from 'react-router-dom'
import logoImg from '../assets/logo.png'
import insightsLogo from '../assets/_logo.png'
import '../App.css'

const NOME_TECNICO = 'Marco Antônio Araújo Grossi'

export function AppShell() {
  const [insightsOpen, setInsightsOpen] = useState(false)
  const containerRef = useRef(/** @type {HTMLDivElement | null} */ (null))

  useEffect(() => {
    if (!insightsOpen) {
      return undefined
    }
    const onKey = (e) => {
      if (e.key === 'Escape') {
        setInsightsOpen(false)
      }
    }
    const onClick = (e) => {
      const root = containerRef.current
      if (root && !root.contains(e.target)) {
        setInsightsOpen(false)
      }
    }
    window.addEventListener('keydown', onKey)
    document.addEventListener('mousedown', onClick)
    return () => {
      window.removeEventListener('keydown', onKey)
      document.removeEventListener('mousedown', onClick)
    }
  }, [insightsOpen])

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
          <div className="erp-header__actions" ref={containerRef}>
            <button
              type="button"
              className="erp-insights-btn"
              aria-label="PreventechAI Insights"
              aria-haspopup="dialog"
              aria-expanded={insightsOpen}
              onClick={() => setInsightsOpen((v) => !v)}
            >
              <img src={insightsLogo} alt="" className="erp-insights-btn__logo" width={28} height={28} />
            </button>
            {insightsOpen ? (
              <div
                className="erp-insights-popover"
                role="dialog"
                aria-modal="false"
                aria-labelledby="erp-insights-title"
              >
                <div className="erp-insights-popover__header">
                  <h3 id="erp-insights-title" className="erp-insights-popover__title">
                    PREVENTECHAI INSIGHTS
                  </h3>
                  <button
                    type="button"
                    className="erp-insights-popover__close"
                    aria-label="Fechar"
                    onClick={() => setInsightsOpen(false)}
                  >
                    ×
                  </button>
                </div>
                <div className="erp-insights-popover__body">
                  Oi, {NOME_TECNICO}, você não tem nenhuma manutenção preventiva ou preditiva para esta semana! =)
                </div>
              </div>
            ) : null}
          </div>
        </header>
        <main className="erp-content">
          <Outlet />
        </main>
      </div>
    </div>
  )
}
