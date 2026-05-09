import { useCallback, useEffect, useState } from 'react'
import { Modal } from '../components/Modal.jsx'
import { URL_ORDEM_ATUALIZA, URL_ORDEM_CRIA, URL_ORDEM_LISTA } from '../api/config.js'
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

const emptyCreateForm = () => ({
  clienteCpf: '',
  status: ORDEM_STATUS_OPTIONS[0],
  descricao: '',
  area: '',
})

export function OrdensServicoPage() {
  const [rows, setRows] = useState(/** @type {OrdemRow[]} */ ([]))
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(/** @type {string | null} */ (null))
  const [modalOpen, setModalOpen] = useState(false)
  const [saving, setSaving] = useState(false)
  const [createForm, setCreateForm] = useState(emptyCreateForm)

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

  function openCreateModal() {
    setError(null)
    setCreateForm(emptyCreateForm())
    setModalOpen(true)
  }

  async function submitCreate() {
    setSaving(true)
    setError(null)
    try {
      await jsonFetch(URL_ORDEM_CRIA, {
        method: 'POST',
        body: JSON.stringify({
          clienteCpf: createForm.clienteCpf.trim(),
          status: createForm.status,
          descricao: createForm.descricao.trim() || null,
          area: createForm.area.trim() || null,
        }),
      })
      setModalOpen(false)
      await load()
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Não foi possível criar a ordem.')
    } finally {
      setSaving(false)
    }
  }

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
      <div className="erp-page-toolbar">
        <h2 className="erp-page-title">Ordens de Serviço</h2>
        <button type="button" className="erp-btn-add" aria-label="Nova ordem de serviço" onClick={openCreateModal}>
          +
        </button>
      </div>
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

      <Modal
        open={modalOpen}
        title="Nova ordem de serviço"
        onClose={() => {
          if (!saving) {
            setModalOpen(false)
          }
        }}
        footer={
          <>
            <button type="button" className="erp-btn-ghost" disabled={saving} onClick={() => setModalOpen(false)}>
              Cancelar
            </button>
            <button type="button" className="erp-btn-primary" disabled={saving} onClick={() => void submitCreate()}>
              {saving ? 'Salvando…' : 'Criar'}
            </button>
          </>
        }
      >
        <div className="erp-form-stack">
          <p className="erp-form-hint">O CPF deve pertencer a um cliente já cadastrado.</p>
          <div className="erp-field">
            <label htmlFor="os-create-cpf">Cliente (CPF)</label>
            <input
              id="os-create-cpf"
              className="erp-input"
              value={createForm.clienteCpf}
              onChange={(e) => setCreateForm((f) => ({ ...f, clienteCpf: e.target.value }))}
              autoComplete="off"
            />
          </div>
          <div className="erp-field">
            <label htmlFor="os-create-status">Status</label>
            <select
              id="os-create-status"
              className="erp-select"
              style={{ width: '100%', minWidth: 0 }}
              value={createForm.status}
              onChange={(e) => setCreateForm((f) => ({ ...f, status: e.target.value }))}
            >
              {ORDEM_STATUS_OPTIONS.map((s) => (
                <option key={s} value={s}>
                  {s}
                </option>
              ))}
            </select>
          </div>
          <div className="erp-field">
            <label htmlFor="os-create-desc">Descrição</label>
            <textarea
              id="os-create-desc"
              value={createForm.descricao}
              onChange={(e) => setCreateForm((f) => ({ ...f, descricao: e.target.value }))}
            />
          </div>
          <div className="erp-field">
            <label htmlFor="os-create-area">Área</label>
            <input
              id="os-create-area"
              className="erp-input"
              value={createForm.area}
              onChange={(e) => setCreateForm((f) => ({ ...f, area: e.target.value }))}
            />
          </div>
        </div>
      </Modal>
    </>
  )
}
