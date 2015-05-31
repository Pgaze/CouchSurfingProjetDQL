package modele;

import java.sql.Connection;

import utilitaire.ConnectionMySQL;

public final class Data {
	public static final String BDD_IP = "127.0.0.1";
	public static final String BDD_PWD = "teamBifle";
	public static final String BDD_USER = "serveur";
	public static final String BDD_PORT = "3306";
	public static String BDD_NAME = "/CouchSurfing";
	public static Connection BDD_Connection = ConnectionMySQL.getInstance();
}
