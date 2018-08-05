package by.baranov.connectionpool;

import java.util.Scanner;

public class RunnerConnectionPool {
		private static final int POOL_SIZE = 5;
		private static final String URL = "jdbc:mysql://localhost:3306/mydatabasecarproduction?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		private static final String USER_LOGIN = "root";
		private static final String USER_PASSWORD = "";
//		jdbc:mysql://localhost:3306/mydatabasecarproduction"; 	
		
	public static void main(String[] args) throws InterruptedException {
		
		ConnectionPool connectionPool = ConnectionPool.getConnectionPool();		
		connectionPool.setUrl(URL);
		connectionPool.setName(USER_LOGIN);
		connectionPool.setPassword(USER_PASSWORD);
		connectionPool.setPoolSize(POOL_SIZE);
		connectionPool.initialize();

		for (int i = 1; i < 10; i++) {
			UserThread thread = new UserThread(connectionPool);
			thread.start();
		}
		
//		Scanner scan = new Scanner(System.in);
//		scan.next();
//		connectionPool.cleaningConnectionPool();

	}
}

