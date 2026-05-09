import { useCallback, useEffect, useState } from 'react'
import { Modal } from '../components/Modal.jsx'
import { URL_ATIVOS, URL_EQUIPAMENTO_CRIA, URL_LOCALIZACAO_LISTA } from '../api/config.js'
import { jsonFetch } from '../api/http.js'

/** @typedef {{ key: string, label: string, alts: string[] }} Col */

/** @typedef {{ id: string, nome: string }} LocRow */

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

/**
 * @param {unknown} raw
 * @returns {LocRow[]}
 */
function normalizeLoc(raw) {
  if (!Array.isArray(raw)) {
    return []
  }
  return raw
    .map((item) => {
      const o = /** @type {Record<string, unknown>} */ (item && typeof item === 'object' ? item : {})
      const id = o.id != null ? String(o.id) : ''
      const nome = o.nome != null ? String(o.nome) : id || '—'
      return { id, nome }
    })
    .filter((r) => r.id)
}

const emptyEquipForm = () => ({
  codigo: '',
  nome: '',
  descricao: '',
  status: '',
  localizacaoId: '',
})

export function AtivosPage() {
  const [rows, setRows] = useState(/** @type {Record<string, unknown>[]} */ ([]))
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(/** @type {string | null} */ (null))
  const [modalOpen, setModalOpen] = useState(false)
  const [saving, setSaving] = useState(false)
  const [locLoading, setLocLoading] = useState(false)
  const [locUseManual, setLocUseManual] = useState(false)
  const [localizacoes, setLocalizacoes] = useState(/** @type {LocRow[]} */ ([]))
  const [createForm, setCreateForm] = useState(emptyEquipForm)

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

  useEffect(() => {
    if (!modalOpen) {
      return undefined
    }
    let cancelled = false
    const t = setTimeout(() => {
      setLocLoading(true)
      setLocUseManual(false)
      void (async () => {
        try {
          const data = await jsonFetch(URL_LOCALIZACAO_LISTA)
          const list = normalizeLoc(data)
          if (!cancelled) {
            setLocalizacoes(list)
            setLocUseManual(list.length === 0)
          }
        } catch {
          if (!cancelled) {
            setLocalizacoes([])
            setLocUseManual(true)
          }
        } finally {
          if (!cancelled) {
            setLocLoading(false)
          }
        }
      })()
    }, 0)
    return () => {
      cancelled = true
      clearTimeout(t)
    }
  }, [modalOpen])

  function openCreateModal() {
    setError(null)
    setCreateForm(emptyEquipForm())
    setModalOpen(true)
  }

  async function submitCreate() {
    const lid = createForm.localizacaoId.trim()
    if (!lid) {
      setError('Informe a localização (UUID).')
      return
    }
    setSaving(true)
    setError(null)
    try {
      await jsonFetch(URL_EQUIPAMENTO_CRIA, {
        method: 'POST',
        body: JSON.stringify({
          codigo: createForm.codigo.trim(),
          nome: createForm.nome.trim(),
          descricao: createForm.descricao.trim(),
          status: createForm.status.trim(),
          localizacao: { id: lid },
        }),
      })
      setModalOpen(false)
      await load()
    } catch (e) {
      setError(e instanceof Error ? e.message : 'Não foi possível criar o equipamento.')
    } finally {
      setSaving(false)
    }
  }

  return (
    <>
      <div className="erp-page-toolbar">
        <h2 className="erp-page-title">Ativos</h2>
        <button type="button" className="erp-btn-add" aria-label="Novo ativo" onClick={openCreateModal}>
          +
        </button>
      </div>
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

      <Modal
        open={modalOpen}
        title="Novo equipamento (ativo)"
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
          {locLoading ? <p className="erp-form-hint">Carregando localizações…</p> : null}
          {!locLoading && locUseManual ? (
            <p className="erp-form-hint">Não foi possível listar localizações. Informe o UUID manualmente.</p>
          ) : null}
          <div className="erp-field">
            <label htmlFor="eq-create-loc">Localização (UUID)</label>
            {locUseManual || localizacoes.length === 0 ? (
              <input
                id="eq-create-loc"
                className="erp-input"
                value={createForm.localizacaoId}
                onChange={(e) => setCreateForm((f) => ({ ...f, localizacaoId: e.target.value }))}
                placeholder="xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
              />
            ) : (
              <select
                id="eq-create-loc"
                className="erp-select"
                style={{ width: '100%', minWidth: 0 }}
                value={createForm.localizacaoId}
                onChange={(e) => setCreateForm((f) => ({ ...f, localizacaoId: e.target.value }))}
              >
                <option value="">Selecione…</option>
                {localizacoes.map((l) => (
                  <option key={l.id} value={l.id}>
                    {l.nome}
                  </option>
                ))}
              </select>
            )}
          </div>
          <div className="erp-field">
            <label htmlFor="eq-create-codigo">Código</label>
            <input
              id="eq-create-codigo"
              className="erp-input"
              value={createForm.codigo}
              onChange={(e) => setCreateForm((f) => ({ ...f, codigo: e.target.value }))}
            />
          </div>
          <div className="erp-field">
            <label htmlFor="eq-create-nome">Nome</label>
            <input
              id="eq-create-nome"
              className="erp-input"
              value={createForm.nome}
              onChange={(e) => setCreateForm((f) => ({ ...f, nome: e.target.value }))}
            />
          </div>
          <div className="erp-field">
            <label htmlFor="eq-create-desc">Descrição</label>
            <textarea
              id="eq-create-desc"
              value={createForm.descricao}
              onChange={(e) => setCreateForm((f) => ({ ...f, descricao: e.target.value }))}
            />
          </div>
          <div className="erp-field">
            <label htmlFor="eq-create-status">Status</label>
            <input
              id="eq-create-status"
              className="erp-input"
              value={createForm.status}
              onChange={(e) => setCreateForm((f) => ({ ...f, status: e.target.value }))}
            />
          </div>
        </div>
      </Modal>
    </>
  )
}
