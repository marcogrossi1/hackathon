FROM node:22-alpine

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm ci

COPY . .

EXPOSE 5173

# Bind mounts replace /app at runtime; node_modules is a named volume and may be
# empty or stale. Install deps on each start so packages match package-lock.json.
CMD ["sh", "-c", "npm ci && npm run dev -- --host 0.0.0.0 --port 5173"]
