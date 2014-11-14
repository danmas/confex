package net.confex.db;

import java.sql.Connection;
import java.sql.SQLException;

import net.confex.translations.Translator;
import net.confex.utils.Strings;

//import oracle.jdbc.driver.OracleDriver;

public abstract class JdbcConnection {

	private Connection connection;

	// -- "jdbc:oracle:thin:@server:1521:SID"
	// -- "jdbc:mysql://localhost:3306/test"
	private String url = null;
	private String user = null;
	private String password = null;

	private boolean changed = false;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		if (!user.equals(this.user)) {
			setChanged(true);
			this.user = user;
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if (!password.equals(this.password)) {
			setChanged(true);
			this.password = password;
		}
	}

	public boolean isChanged() {
		return changed;
	}

	public boolean isClosed() {
		try {
			if (connection == null || connection.isClosed())
				return true;
		} catch (SQLException e) {
			return true;
		}
		return false;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		if (!url.equals(this.url)) {
			setChanged(true);
			this.url = url;
		}
	}

	/**
	 * Создается JDBC коннекция специфичная для каждого драйвера
	 * 
	 * @return
	 * 
	 */
	protected abstract Connection cereate_connection_on_server()
			throws Exception;

	protected abstract Connection cereate_connection(String url)
			throws Exception;

	protected abstract Connection cereate_connection(String url, String user,
			String password) throws Exception;

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Открывается коннекция на сервере
	// Если коннекции еще нет или закрыта то она будет создана
	//
	public void createConnection() {
		try {
			if (connection != null && isChanged())
				connection.close();

			if (connection == null || connection.isClosed()) {
				create_connection();
			} else {
				// Connection reset by peer: socket write error
				if (!validate()) {
					create_connection();
				}
			}
		} catch (SQLException e) {
			String err_str = Translator
					.getString("MSG_ERR_CANT_CREATE_JDBC_CONNECTION")
					+ " " + e.getMessage();
			System.err.println(err_str);
			connection = null;
		} catch (Exception ex) {
			String err_str = Translator
					.getString("MSG_ERR_CANT_CREATE_JDBC_CONNECTION")
					+ " " + ex.getMessage();
			System.err.println(err_str);
			connection = null;
		}
	}

	protected void create_connection() throws Exception {
		// -- Connect to Oracle using JDBC driver ON SERVER!
		if (!Strings.isStringEmpty(getUrl())
				&& !Strings.isStringEmpty(getUser())
				&& !Strings.isStringEmpty(getPassword())) {
			connection = cereate_connection(getUrl(), getUser(), getPassword());
		} else if (!Strings.isStringEmpty(getUrl())
				&& (Strings.isStringEmpty(getUser()) || Strings
						.isStringEmpty(getPassword()))) {
			connection = cereate_connection(getUrl());
		} else {
			System.err.println(Translator
					.getString("MSG_ERR_CANT_CREATE_JDBC_CONNECTION")
					+ " "
					+ Translator
							.getString("MSG_ERR_CONNECTIONSTRING_NOT_SETUP"));
			connection = null;
		}
		connection.setAutoCommit(false);
	}

	public boolean validate() {
		try {
			connection.getMetaData();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Открывается коннекция с клиентской машины
	// Если коннекции еще нет или закрыта то она будет создана
	//
	public void createConnection(String url) throws Exception {
		setUrl(url);

		if (connection != null && isChanged())
			connection.close();

		if (connection == null || connection.isClosed()) {
			try {
				connection = cereate_connection(url);
				connection.setAutoCommit(false);
				setChanged(false);
				// TraceLight.writeTrace("Коннекция открыта.");
			} catch (Exception e) {
				String err_str = Translator
						.getString("MSG_ERR_CANT_CREATE_JDBC_CONNECTION")
						+ " " + e.getMessage();
				System.err.println(err_str);
				// TraceLight.writeError(err_str);
				throw new Exception(err_str);
			}
		}
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Открывается коннекция с клиентской машины
	// Если коннекции еще нет или закрыта то она будет создана
	//
	public void createConnection(String url, String user, String password)
			throws Exception {
		setUrl(url);
		setUser(user);
		setPassword(password);

		if (connection != null && isChanged())
			connection.close();

		if (connection == null || connection.isClosed()) {
			try {
				connection = cereate_connection(url, user, password);
				connection.setAutoCommit(false);
				setChanged(false);
			} catch (Exception e) {
				String err_str = Translator
						.getString("MSG_ERR_CANT_CREATE_JDBC_CONNECTION")
						+ " " + e.getMessage();
				System.err.println(err_str);
				throw new Exception(err_str);
			}
		}
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Получение коннекции
	// Если коннекции еще нет или закрыта то она будет создана
	//
	public Connection getConnection() {
		createConnection();
		return connection;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Если коннекция была где-то установлена извне то можно просто работать с
	// ней
	//
	public void setConnection(Connection con) {
		connection = con;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Коннекция закрывается
	//
	public void closeConnection() {
		setUrl("");
		setUser("");
		setPassword("");

		if (connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//
	public void commitConnection() {
		if (connection != null) {
			try {
				connection.commit();
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//
	public void rollbackConnection() {
		if (connection != null) {
			try {
				connection.rollback();
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}

}
