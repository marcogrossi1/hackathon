/**
 * @template T
 * @param {string} url
 * @param {RequestInit} [options]
 * @returns {Promise<T>}
 */
export async function jsonFetch(url, options = {}) {
  const headers = new Headers(options.headers)
  const body = options.body
  if (body != null && typeof body === 'string' && !headers.has('Content-Type')) {
    headers.set('Content-Type', 'application/json')
  }

  const res = await fetch(url, { ...options, headers })
  const text = await res.text()

  if (!res.ok) {
    let detail = text || res.statusText
    try {
      const parsed = text ? JSON.parse(text) : null
      if (parsed && typeof parsed.message === 'string') {
        detail = parsed.message
      }
    } catch {
      /* use raw text */
    }
    throw new Error(detail || `Request failed (${res.status})`)
  }

  if (res.status === 204 || text === '') {
    return /** @type {T} */ (null)
  }

  return /** @type {T} */ (JSON.parse(text))
}
