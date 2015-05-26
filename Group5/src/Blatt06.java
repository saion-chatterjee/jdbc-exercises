import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Blatt06 extends Basis06 {
	String URL = "jdbc:postgresql:PRAKT";
	String user = "postgres";
	String password = "postgre123";
	Connection connection;
	PreparedStatement preparedStatement;
	ResultSet resultSet, tempResultSet;

	@SuppressWarnings("unused")
	@Override
	public boolean aufg_1_1(String supplier, String name, Integer availqty,
			BigDecimal supplycost) throws SQLException, LabCourseException {
		// TODO Auto-generated method stub
		int supplierKey = 0;
		int partKey = 0;
		String type, size, container, retailprice;
		String suppkeyFetchQuery = "select suppkey from supplier where name = ?";
		String partkeyFetchQuery = "select partkey from part where name = ?";
		String maxPartKeyFetchQuery = "select max(partkey) from part";
		String partInsertQuery = "insert into part values (?,?,?,?,?,?)";
		String partsuppSearchQuery = "select * from partsupp where partkey = ? and suppkey = ?";
		String partsuppInsertQuery = "insert into partsupp values (?,?,?,?)";
		try {
			connection = createConnection();

			// Fetch supplier key from the supplier table using the supplier
			// name that user provides as input
			preparedStatement = connection.prepareStatement(suppkeyFetchQuery);
			preparedStatement.setString(1, supplier);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				do {
					supplierKey = resultSet.getInt(1);
				} while (resultSet.next());
				resultSet.close();
				preparedStatement.close();
			} else {
				// then there are no rows.
				throw new LabCourseException(
						"You must enter a supplier name which already exists in our database!");
			}

			// Fetch part key from the part table using the part name that user
			// provides as input
			// If part name provided by user is not present in the database,
			// insert a new part
			preparedStatement = connection.prepareStatement(partkeyFetchQuery);
			preparedStatement.setString(1, name);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				do {
					partKey = resultSet.getInt(1);
				} while (resultSet.next());
				resultSet.close();
				preparedStatement.close();
			} else {
				// then there are no rows.
				System.out
						.println("The part name you entered does not exist!\nPlease enter details so that we can add a new part:");
				Scanner in = new Scanner(System.in);
				System.out.println("Enter the Part Type:");
				type = in.nextLine();
				System.out.println("Enter the Size of the part:");
				size = in.nextLine();
				System.out.println("Enter the number of container:");
				container = in.nextLine();
				System.out.println("Enter the retail price:");
				retailprice = in.nextLine();
				if (type.equals(null)) {
					throw new LabCourseException(
							"Type of a part cannot be null");
				} else if (retailprice == null) {
					throw new LabCourseException(
							"Retail price of a part cannot be null");
				} else {
					try {
						int randomSize = Integer.parseInt(size);
					} catch (NumberFormatException e) {
						throw new LabCourseException(
								"Size of a part can only be an integer");
					}
					try {
						int randomContainer = Integer.parseInt(container);
					} catch (NumberFormatException e) {
						throw new LabCourseException(
								"Part Container can only be an integer");
					}
					try {
						Double randomRetailPrice = Double
								.parseDouble(retailprice);
					} catch (NumberFormatException e) {
						throw new LabCourseException(
								"Retail Price of a part should be a decimal");
					}
				}

				preparedStatement = connection
						.prepareStatement(maxPartKeyFetchQuery);
				resultSet = preparedStatement.executeQuery();
				if (resultSet.next()) {
					do {
						partKey = resultSet.getInt(1);
						partKey++;
						System.out.println(partKey);
					} while (resultSet.next());
					resultSet.close();
					preparedStatement.close();
				}
				preparedStatement = connection
						.prepareStatement(partInsertQuery);
				preparedStatement.setInt(1, partKey);
				preparedStatement.setString(2, name);
				preparedStatement.setString(3, type);
				preparedStatement.setInt(4, Integer.valueOf(size));
				preparedStatement.setInt(5, Integer.valueOf(container));
				preparedStatement.setDouble(6, Double.valueOf(retailprice));
				preparedStatement.executeUpdate();
				preparedStatement.close();
			}

			// check if the partsupp already exists
			preparedStatement = connection.prepareStatement(partsuppSearchQuery);
			preparedStatement.setInt(1, partKey);
			preparedStatement.setInt(2, supplierKey);
			resultSet = preparedStatement.executeQuery();
			
			if (!resultSet.next()) {
				preparedStatement.close();
				// insert values into the partsupp table
				preparedStatement = connection.prepareStatement(partsuppInsertQuery);
				preparedStatement.setInt(1, partKey);
				preparedStatement.setInt(2, supplierKey);
				preparedStatement.setInt(3, availqty);
				preparedStatement.setBigDecimal(4, supplycost);
				preparedStatement.executeUpdate();
				preparedStatement.close();
				resultSet.close();
				preparedStatement.close();
			} else {
				// then there already exists a partsupp
				throw new LabCourseException(
						"You have entered a part and supplier combination which already exists. Please select a new one");
			}
			return true;
		} catch (LabCourseException labEx) {
			System.out.println(labEx.getMessage());
			return false;
		} catch (SQLException sqlEx) {
			System.out.println(sqlEx.getMessage());
			return false;
		} finally {
			if (resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException ignored) {
					System.out.println(ignored.getMessage());
				}
			if (preparedStatement != null)
				try {
					preparedStatement.close();
				} catch (SQLException ignored) {
					System.out.println(ignored.getMessage());
				}
			closeConnection(connection);
		}
	}

	@Override
	public int aufg_1_2(String name) throws SQLException, LabCourseException {
		String fetchCustomerDetailsQuery = "select name, address, phone, fax, acctbal from customer where name like ?";
		String custName, address, phone, fax, nameForQuery;
		double accountBalance;
		try {
			connection = createConnection();
			nameForQuery = "%"+ name + "%";
			// Fetch supplier key from the supplier table using the supplier
			// name that user provides as input
			preparedStatement = connection.prepareStatement(fetchCustomerDetailsQuery);
			preparedStatement.setString(1, nameForQuery);
			resultSet = preparedStatement.executeQuery();
			System.out.println("Customer Details:");
			if (resultSet.next()) {
				do {
					custName = resultSet.getString(1);
					address = resultSet.getString(2);
					phone = resultSet.getString(3);
					fax = resultSet.getString(4);
					accountBalance = resultSet.getDouble(5);
					System.out.println(custName+"\t"+address+"\t"+phone+"\t"+fax+"\t"+accountBalance);
				} while (resultSet.next());
				resultSet.close();
				preparedStatement.close();
				return 1;
			} else {
				// then there are no rows.
				throw new LabCourseException(
						"There are no entries which match the string you entered!");
			}
		}catch(LabCourseException labEx){
			System.out.println(labEx.getMessage());
			return 0;
		}catch(SQLException sqlEx){
			System.out.println(sqlEx.getMessage());
			return 0;
		}
		finally{
			if (resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException ignored) {
					System.out.println(ignored.getMessage());
				}
			if (preparedStatement != null)
				try {
					preparedStatement.close();
				} catch (SQLException ignored) {
					System.out.println(ignored.getMessage());
				}
			closeConnection(connection);
		}
	}

	@Override
	public int aufg_1_3(String table) throws SQLException, LabCourseException {
		String queryString = null;
		int numberOfColumnsCounter = 0;
		String tableColumnFetchQuery = "select a.attname from pg_attribute a "+
										"inner join pg_class c on a.attrelid = c.oid "+
										"where c.relname = '"+table+"' and a.attname "+
										"not in ('cmax','cmin','xmax','xmin','ctid','tableoid')";
		
		try {
			connection = createConnection();

			// Fetch supplier key from the supplier table using the supplier
			// name that user provides as input
			System.out.println(tableColumnFetchQuery);
			preparedStatement = connection.prepareStatement(tableColumnFetchQuery);
			resultSet = preparedStatement.executeQuery();
			System.out.println("got result set");
			if (resultSet.next()) {
				numberOfColumnsCounter++;
				queryString = resultSet.getString(1);
				while (resultSet.next()){
					numberOfColumnsCounter++;
					queryString = queryString + " IS NULL OR " + resultSet.getString(1);
				}
				queryString = queryString + " IS NULL ";
				resultSet.close();
				preparedStatement.close();
			} else {
				// then there are no rows.
				throw new LabCourseException(
						"The table name you entered does not exist in our database!");
			}
			System.out.println(queryString);
			System.out.println(numberOfColumnsCounter);
			String findNullValuesQuery = "SELECT * FROM " +table+ " WHERE "+queryString;
			System.out.println(findNullValuesQuery);
			preparedStatement = connection.prepareStatement(findNullValuesQuery);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				do {
					int i = 1;
					for(i = 1; i < numberOfColumnsCounter; i++){
						System.out.print(resultSet.getString(i)+ "\t");
					}
					System.out.println();
				} while (resultSet.next());
				resultSet.close();
				preparedStatement.close();
			} else {
				// then there are no rows.
				throw new LabCourseException(
						"The table name you entered does not have any rows which are null!");
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return 0;
	}

	@Override
	public boolean aufg_1_4(String name) throws SQLException,
			LabCourseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BigDecimal aufg_1_5(String name, BigDecimal payment)
			throws SQLException, LabCourseException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean status;
		Blatt06 blatt06 = new Blatt06();
		try {
			Class.forName("org.postgresql.Driver");
			
			/*//exercise 1 start
			status = blatt06.aufg_1_1("Twistfix", "saion", 10,
					BigDecimal.valueOf(20.56));
			if (status) {
				System.out.println("Data input successful!");
			} else {
				System.out.println("Exception occurred!");
			}
			//exercise 1 end
			//exercise 2 start
			blatt06.aufg_1_2("c");
			//exercise 2 end*/
			//esercise 3 start
			blatt06.aufg_1_3("lineitem");
			//exercise 3 end
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (LabCourseException e) {
			System.out.println(e.getMessage());
		}
	}

	private Connection createConnection() {
		Connection openConnection = null;
		try {
			openConnection = DriverManager.getConnection(URL, user, password);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return openConnection;
	}

	private void closeConnection(Connection closingConnection) {
		// TODO Auto-generated method stub
		try {
			closingConnection.close();
		} catch (Exception ignored) {
			System.out.println(ignored.getMessage());
		}
	}
}