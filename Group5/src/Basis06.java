import java.sql.SQLException;
import java.math.BigDecimal;

/**
 *  Base class for the solutions of practice sheet 5
 *  
 *  Praktikum Datenbanksysteme, Sommersemester 2015
 *  @author Chetan Basuray
 *  @author Saion Chatterjee
 */
public abstract class Basis06
{
	/**
	 * Insertion of new offers (missing values have to specified by the user
	 * (System.in).
	 * 
	 * @param supplier
	 *            Name of the supplier
	 * @param name
	 *            Name of the good
	 * @param availqty
	 *            Available quantity
	 * @param supplycost
	 *            Supply cost
	 * @return true if insertion is successful, else false
	 */
	public abstract boolean aufg_1_1(String supplier, String name,
			Integer availqty, BigDecimal supplycost)
	    throws SQLException, LabCourseException;
  
	/**
	 * Searching customers by name (missing values have to specified by the user
	 * (System.in). You should print a formatted list of customers found for the
	 * name.
	 * 
	 * @param name
	 *            Name of the customer searched for
	 * @return Number of results
	 */
	public abstract int aufg_1_2(String name)
	    throws SQLException, LabCourseException;
	
	/**
	 * Displaying and changing NULL-values (missing values have to specified by
	 * the user (System.in).
	 * 
	 * @param table
	 *            Name of the table
	 * @return Number of changed rows
	 */
	public abstract int aufg_1_3(String table)
	    throws SQLException, LabCourseException;
	
	/**
	 * Deletion of a customer (missing values have to specified by the user
	 * (System.in).
	 * 
	 * @param name
	 *            Name of the customer searched for deletion
	 * @return true if deletion successful, else false
	 */
	public abstract boolean aufg_1_4(String name)
	    throws SQLException, LabCourseException;
	
	 /**
	 * Incoming payments (missing values have to specified by the user
	 * (System.in).
	 * 
	 * @param name
	 *            Name of the customer
	 * @param payment
	 *            Payed amount
	 * @return new account balance of the customer (acctbal)
	 */
	public abstract BigDecimal aufg_1_5(String name, BigDecimal payment)
	    throws SQLException, LabCourseException;

	public class LabCourseException extends Exception {
		
		public LabCourseException(String exc)
    		{
    	    		super(exc);
		}

		public String getMessage()
    		{
        		return super.getMessage();
    		}

		private static final long serialVersionUID = -2949695778145416810L;
	}
}

