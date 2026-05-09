import { useEffect } from 'react'

/**
 * @param {{
 *   open: boolean
 *   title: string
 *   onClose: () => void
 *   children: import('react').ReactNode
 *   footer?: import('react').ReactNode
 * }} props
 */
export function Modal({ open, title, onClose, children, footer }) {
  useEffect(() => {
    if (!open) {
      return undefined
    }
    const onKey = (e) => {
      if (e.key === 'Escape') {
        onClose()
      }
    }
    window.addEventListener('keydown', onKey)
    return () => window.removeEventListener('keydown', onKey)
  }, [open, onClose])

  if (!open) {
    return null
  }

  return (
    <div className="erp-modal-root" role="presentation">
      <button type="button" className="erp-modal-backdrop" aria-label="Fechar" onClick={onClose} />
      <div className="erp-modal-panel" role="dialog" aria-modal="true" aria-labelledby="erp-modal-title">
        <div className="erp-modal-header">
          <h3 id="erp-modal-title" className="erp-modal-title">
            {title}
          </h3>
          <button type="button" className="erp-modal-close" onClick={onClose} aria-label="Fechar">
            ×
          </button>
        </div>
        <div className="erp-modal-body">{children}</div>
        {footer ? <div className="erp-modal-footer">{footer}</div> : null}
      </div>
    </div>
  )
}
