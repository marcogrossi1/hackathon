/** Direct backend origin (browser → Spring; no Vite proxy). */
export const BACKEND_ORIGIN = 'http://localhost:8000/api'

/** Paths match this repo’s controllers (no `/api` prefix on the JVM). */
export const URL_ATIVOS = `${BACKEND_ORIGIN}/equipamento`
export const URL_ORDEM = `${BACKEND_ORIGIN}/ordemServico`
export const URL_ORDEM_LISTA = `${BACKEND_ORIGIN}/ordemServico/lista`
export const URL_ORDEM_ATUALIZA = `${BACKEND_ORIGIN}/ordemServico/atualiza`
