import { useCallback, useEffect, useState } from 'react'
import { URL_ATIVOS } from '../api/config.js'
import { jsonFetch } from '../api/http.js'

/** @typedef {{ key: string, label: string, alts: string[] }} Col */

/** @type {Col[]} */
const COLUMNS = [
  { key: 'id', label: 'ID', alts: ['uuid'] },
  { key: 'nome', label: 'Nome', alts: ['name', 'titulo', 'descricao'] },
  { key: 'codigo', label: 'Código', alts: ['code', 'sigla', 'sku'] },
  { key: 'categoria', label: 'Categoria', alts: ['tipo', 'classificacao', 'grupo'] },
  { key: 'status', label: 'Status', alts: ['situacao', 'estado'] },
]

/**
 * @param {Record<string, unknown>} row
 * @param {Col} col
 */
function cellValue(row, col) {
  const keys = [col.key, ...col.alts]
  for (const k of keys) {
    const v = row[k]
    if (v != null && v !== '') {
      return String(v)
    }
  }
  return '—'
}

export function AtivosPage() {
  const [rows, setRows] = useState(/** @type {Record<string, unknown>[]} */ ([]))
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(/** @type {string | null} */ (null))

  const load = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await jsonFetch(URL_ATIVOS)
      if (!Array.isArray(data)) {
        setRows([])
        setError('Resposta inesperada: esperado um array JSON.')
        return
      }
      setRows(data)
    } catch (e) {
      setRows([])
      setError(e instanceof Error ? e.message : 'Falha ao carregar ativos.')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    const t = setTimeout(() => {
      void load()
    }, 0)
    return () => clearTimeout(t)
  }, [load])

  return (
    <>
      <h2 className="erp-page-title">Ativos</h2>
      {error ? (
        <div className="erp-banner erp-banner--error" role="alert">
          {error}
        </div>
      ) : null}
      {!error && !loading && rows.length === 0 ? (
        <div className="erp-banner erp-banner--info">Nenhum ativo retornado.</div>
      ) : null}
      <div className="erp-card">
        {loading ? (
          <div className="erp-loading">Carregando…</div>
        ) : (
          <div className="erp-table-wrap">
            <table className="erp-table">
              <thead>
                <tr>
                  {COLUMNS.map((c) => (
                    <th key={c.key}>{c.label}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {rows.map((row, i) => (
                  <tr key={String(row.id ?? row.uuid ?? row.codigo ?? i)}>
                    {COLUMNS.map((c) => (
                      <td key={c.key}>{cellValue(row, c)}</td>
                    ))}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </>
  )
}
