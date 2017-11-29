package plmedia.intranet.dao.DBUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import plmedia.intranet.dao.ConMan;
import plmedia.intranet.dao.Statements;
import plmedia.intranet.model.Child;
import plmedia.intranet.model.Employee;
import plmedia.intranet.model.Parent;
import plmedia.intranet.model.Wing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class DBcreate {

  Util util = new Util();
  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public int createParent(Parent parent) {
    try(
        PreparedStatement stmt = ConMan.prepStat(Statements.DEF_CREATE_PARENT_USER_SQL);
    ) {

      if (util.checkEmail(parent.getUserEmail()) != 10){
        String parentPass = parent.getPassword();
        String hashedPassword = passwordEncoder.encode(parentPass);

        stmt.setString(1, hashedPassword);
        stmt.setString(2, parent.getUserEmail());
        stmt.setString(3, parent.getFirstName());
        stmt.setString(4, parent.getLastName());
        stmt.setString(5, "ROLE_PAR");
        stmt.setInt(6, 1);

        stmt.executeUpdate();
        System.out.println("Parent user created");
        return 1; // Error codes?
      }
      System.out.println("Parent user NOT created");
    } catch (SQLException e){
      e.printStackTrace();
    }
    return -1; // Error codes?
  }

  public int createEmployee(Employee employee) {
    try(
        PreparedStatement stmt = ConMan.prepStat(Statements.DEF_CREATE_EMPLOYEE_USER_SQL);
    ) {

      if (util.checkEmail(employee.getUserEmail()) != 10){
        String employeePass = employee.getPassword();
        String hashedPassword = passwordEncoder.encode(employeePass);

        stmt.setString(1, hashedPassword);
        stmt.setString(2, employee.getUserEmail());
        stmt.setString(3, employee.getFirstName());
        stmt.setString(4, employee.getLastName());
        stmt.setString(5, "ROLE_EMP");
        stmt.setInt(6, 1);


        stmt.executeUpdate();
        System.out.println("Employee user created");
        return 1; // Error codes?
      }
      System.out.println("Employee user NOT created");
    } catch (SQLException e){
      e.printStackTrace();
    }
    return -1; // Error codes?
  }

  /**
   * Creates an initial child object using simplified constructor.
   * Maybe test for already existing children.
   * @param child
   * @return int
   */
  public int createChild(Child child) {
    try (
        PreparedStatement stmt = ConMan.prepStat(Statements.DEF_CREATE_CHILD_SQL);
    ) {
      stmt.setString(1, child.getFirstName());
      stmt.setString(2, child.getLastName());
      stmt.setDate(3, (Date) child.getBirthday());
      stmt.setString(4, child.getAddress());

      stmt.executeUpdate();
      System.out.println("Child created");
      return 1; // Error codes?
    } catch (SQLException e) {
      e.printStackTrace();
    }
    System.out.println("Child NOT created");
    return -1; // Error codes?
  }

  public int createWing(Wing wing) {
    return 0;
  }
}