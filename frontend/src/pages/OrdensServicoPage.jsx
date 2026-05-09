import { useCallback, useEffect, useState } from 'react'
import { URL_ORDEM_ATUALIZA, URL_ORDEM_LISTA } from '../api/config.js'
import { jsonFetch } from '../api/http.js'
import { ORDEM_STATUS_OPTIONS } from '../constants/ordemStatus.js'

/** @typedef {{ id: string, clienteCpf: string | null, status: string | null, descricao: string | null, area: string | null }} OrdemRow */

/**
 * @param {unknown} raw
 * @returns {OrdemRow[]}
 */
function normalizeLista(raw) {
  if (!Array.isArray(raw)) {
    return []
  }
  return raw.map((item) => {
    const o = /** @type {Record<string, unknown>} */ (item && typeof item === 'object' ? item : {})
    const id = o.id != null ? String(o.id) : ''
    const clienteCpf =
      o.clienteCpf != null
        ? String(o.clienteCpf)
        : o.clientCpf != null
          ? String(o.clientCpf)
          : null
    return {
      id,
      clienteCpf,
      status: o.status != null ? String(o.status) : null,
      descricao: o.descricao != null ? String(o.descricao) : null,
      area: o.area != null ? String(o.area) : null,
    }
  })
}

/**
 * @param {OrdemRow} row
 * @param {string} status
 */
function bodyForUpdate(row, status) {
  return {
    id: row.id,
    clienteCpf: row.clienteCpf,
    status,
    descricao: row.descricao,
    area: row.area,
  }
}

export function OrdensServicoPage() {
  const [rows, setRows] = useState(/** @type {OrdemRow[]} */ ([]))
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(/** @type {string | null} */ (null))

  const load = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await jsonFetch(URL_ORDEM_LISTA)
      setRows(normalizeLista(data))
    } catch (e) {
      setRows([])
      setError(e instanceof Error ? e.message : 'Falha ao carregar ordens.')
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

  /**
   * @param {OrdemRow} row
   * @param {string} nextStatus
   */
  async function onStatusChange(row, nextStatus) {
    setError(null)
    const prev = rows
    const optimistic = rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r))
    setRows(optimistic)
    try {
      await jsonFetch(URL_ORDEM_ATUALIZA, {
        method: 'PUT',
        body: JSON.stringify(bodyForUpdate(row, nextStatus)),
      })
      await load()
    } catch (e) {
      setRows(prev)
      setError(e instanceof Error ? e.message : 'Não foi possível atualizar o status.')
    }
  }

  return (
    <>
      <h2 className="erp-page-title">Ordens de Serviço</h2>
      {error ? (
        <div className="erp-banner erp-banner--error" role="alert">
          {error}
        </div>
      ) : null}
      <div className="erp-card">
        {loading ? (
          <div className="erp-loading">Carregando…</div>
        ) : (
          <div className="erp-table-wrap">
            <table className="erp-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Cliente (CPF)</th>
                  <th>Status</th>
                  <th>Descrição</th>
                  <th>Área</th>
                </tr>
              </thead>
              <tbody>
                {rows.map((row) => (
                  <tr key={row.id}>
                    <td>
                      <code className="muted">{row.id || '—'}</code>
                    </td>
                    <td>{row.clienteCpf ?? <span className="muted">—</span>}</td>
                    <td>
                      {(() => {
                        const current = row.status ?? ''
                        const inList = ORDEM_STATUS_OPTIONS.includes(current)
                        const selectValue = inList ? current : current || ORDEM_STATUS_OPTIONS[0]
                        return (
                          <select
                            className="erp-select"
                            aria-label={`Status da ordem ${row.id}`}
                            value={selectValue}
                            onChange={(ev) => {
                              void onStatusChange(row, ev.target.value)
                            }}
                          >
                            {!inList && current ? (
                              <option value={current}>
                                {current} (atual)
                              </option>
                            ) : null}
                            {ORDEM_STATUS_OPTIONS.map((s) => (
                              <option key={s} value={s}>
                                {s}
                              </option>
                            ))}
                          </select>
                        )
                      })()}
                    </td>
                    <td>{row.descricao ?? <span className="muted">—</span>}</td>
                    <td>{row.area ?? <span className="muted">—</span>}</td>
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
