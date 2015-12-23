package cloudFinal11;



import java.sql.SQLException;

public class initCheckCount {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		Rds.stmt.execute("truncate table checkcount;");
		Rds.stmt.execute("INSERT INTO checkcount values('butler2', 0)");
		Rds.stmt.execute("INSERT INTO checkcount values('butler3', 0)");
		Rds.stmt.execute("INSERT INTO checkcount values('butler4', 0)");
		Rds.stmt.execute("INSERT INTO checkcount values('butler5', 0)");
		Rds.stmt.execute("INSERT INTO checkcount values('butler6', 0)");
		Rds.stmt.execute("INSERT INTO checkcount values('nwc4', 0)");
		Rds.stmt.execute("INSERT INTO checkcount values('nwc5', 0)");
		Rds.stmt.execute("INSERT INTO checkcount values('avery1', 0)");
		Rds.stmt.execute("INSERT INTO checkcount values('avery2', 0)");
		Rds.stmt.execute("INSERT INTO checkcount values('avery3', 0)");
		Rds.stmt.execute("INSERT INTO checkcount values('starr', 0)");
		Rds.stmt.execute("INSERT INTO checkcount values('lehman2', 0)");
		Rds.stmt.execute("INSERT INTO checkcount values('lehman3', 0)");
		Rds.stmt.execute("INSERT INTO checkcount values('uris', 0)");
		System.out.println("Done.");
	}

}
