package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Ensures the JDBC target database exists before Spring initializes the DataSource.
 */
public final class DatabaseEnsurer {

	private static final Logger log = Logger.getLogger(DatabaseEnsurer.class.getName());

	private static final Pattern SAFE_DB_NAME = Pattern.compile("^[a-z][a-z0-9_]*$");

	private DatabaseEnsurer() {
	}

	public static void ensure() {
		String jdbcUrl = firstNonBlank(
				System.getenv("SPRING_DATASOURCE_URL"),
				System.getProperty("spring.datasource.url"),
				"jdbc:postgresql://localhost:5432/hackathon");
		String username = firstNonBlank(
				System.getenv("SPRING_DATASOURCE_USERNAME"),
				System.getProperty("spring.datasource.username"),
				"postgres");
		String password = firstNonBlank(
				System.getenv("SPRING_DATASOURCE_PASSWORD"),
				System.getProperty("spring.datasource.password"),
				"hackathon");

		ParsedJdbc parsed;
		try {
			parsed = parsePostgresqlUrl(jdbcUrl);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException("Invalid PostgreSQL JDBC URL: " + jdbcUrl, e);
		}

		if (!SAFE_DB_NAME.matcher(parsed.database()).matches()) {
			throw new IllegalStateException(
					"Refusing to create database with non-simple name (use lowercase letters, digits, underscore): "
							+ parsed.database());
		}

		String adminUrl = buildAdminUrl(parsed);
		try (Connection conn = DriverManager.getConnection(adminUrl, username, password)) {
			conn.setAutoCommit(true);
			if (databaseExists(conn, parsed.database())) {
				log.log(Level.INFO, "PostgreSQL database \"{0}\" already exists", parsed.database());
				return;
			}
			try (Statement st = conn.createStatement()) {
				String quoted = "\"" + parsed.database().replace("\"", "\"\"") + "\"";
				st.executeUpdate("CREATE DATABASE " + quoted);
			}
			log.log(Level.INFO, "Created PostgreSQL database \"{0}\"", parsed.database());
		} catch (SQLException e) {
			throw new IllegalStateException(
					"Could not ensure database \"" + parsed.database() + "\" exists (admin URL: " + adminUrl + ")",
					e);
		}
	}

	private static boolean databaseExists(Connection conn, String name) throws SQLException {
		try (Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(
						"SELECT 1 FROM pg_database WHERE datname = '" + name.replace("'", "''") + "'")) {
			return rs.next();
		}
	}

	private static String buildAdminUrl(ParsedJdbc parsed) {
		StringBuilder sb = new StringBuilder();
		sb.append("jdbc:postgresql://").append(parsed.host()).append(":").append(parsed.port()).append("/postgres");
		if (parsed.querySuffix() != null && !parsed.querySuffix().isEmpty()) {
			sb.append("?").append(parsed.querySuffix());
		}
		return sb.toString();
	}

	static ParsedJdbc parsePostgresqlUrl(String jdbcUrl) {
		String prefix = "jdbc:postgresql://";
		if (jdbcUrl == null || !jdbcUrl.toLowerCase(Locale.ROOT).startsWith(prefix)) {
			throw new IllegalArgumentException("URL must start with " + prefix);
		}
		String rest = jdbcUrl.substring(prefix.length());
		int slash = rest.indexOf('/');
		if (slash < 0) {
			throw new IllegalArgumentException("URL must contain a /database path segment");
		}
		String authority = rest.substring(0, slash);
		String dbAndQuery = rest.substring(slash + 1);
		if (dbAndQuery.isEmpty()) {
			throw new IllegalArgumentException("Database name is missing");
		}
		int q = dbAndQuery.indexOf('?');
		String database = q >= 0 ? dbAndQuery.substring(0, q) : dbAndQuery;
		if (database.isEmpty()) {
			throw new IllegalArgumentException("Database name is empty");
		}
		String querySuffix = q >= 0 ? dbAndQuery.substring(q + 1) : null;

		String host;
		int port;
		if (authority.isEmpty()) {
			throw new IllegalArgumentException("Host is missing");
		}
		if (authority.startsWith("[")) {
			int endBracket = authority.indexOf(']');
			if (endBracket < 0) {
				throw new IllegalArgumentException("Invalid IPv6 host in URL");
			}
			host = authority.substring(1, endBracket);
			if (authority.length() > endBracket + 1 && authority.charAt(endBracket + 1) == ':') {
				port = Integer.parseInt(authority.substring(endBracket + 2));
			} else {
				port = 5432;
			}
		} else if (authority.contains(":")) {
			int colon = authority.lastIndexOf(':');
			host = authority.substring(0, colon);
			port = Integer.parseInt(authority.substring(colon + 1));
		} else {
			host = authority;
			port = 5432;
		}

		return new ParsedJdbc(host, port, database, querySuffix);
	}

	private static String firstNonBlank(String a, String b, String fallback) {
		if (a != null && !a.isBlank()) {
			return a;
		}
		if (b != null && !b.isBlank()) {
			return b;
		}
		return fallback;
	}

	record ParsedJdbc(String host, int port, String database, String querySuffix) {
	}
}
