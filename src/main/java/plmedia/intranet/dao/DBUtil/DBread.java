package plmedia.intranet.dao.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import plmedia.intranet.dao.ConMan;
import plmedia.intranet.dao.Statements;
import plmedia.intranet.model.Allergen;
import plmedia.intranet.model.Child;
import plmedia.intranet.model.Employee;
import plmedia.intranet.model.Group;
import plmedia.intranet.model.Parent;
import plmedia.intranet.model.Permission;
import plmedia.intranet.model.Wing;

/**
 *
 * @author Simon le Févre Ryom
 * @author Tobias Thomsen
 * @author Jonas Holm
 */
public class DBread {

  // Parents
  public Parent readParentByEmail(String userEmail) {
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_PARENT_BY_EMAIL_SQL);
      stmt.setString(1, userEmail);
      ResultSet rs = stmt.executeQuery();
      rs.first();
      int userID = rs.getInt("user_id");
      ArrayList<Permission> permissions = readPermissionsByUserID((userID));
      return new Parent(
        userID,
        null,
        rs.getString("user_email"),
        rs.getString("first_name"),
        rs.getString("last_name"),
        permissions);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Parent readParentByID(int id) {
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_PARENT_BY_ID_SQL);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      rs.first();
      ArrayList<Permission> permissions = readPermissionsByUserID(id);
      return new Parent(
        rs.getInt("user_id"),
        null,
        rs.getString("user_email"),
        rs.getString("first_name"),
        rs.getString("last_name"),
        permissions);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ArrayList<Parent> readAllParents() {
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_ALL_USERS_BY_TYPE_SQL);
      stmt.setString(1, "ROLE_PAR");
      ResultSet rs = stmt.executeQuery();
      ArrayList<Parent> parents = new ArrayList<>();
      while (rs.next()) {
        ArrayList<Permission> permissions = readPermissionsByUserID((rs.getInt("user_id")));
        parents.add(new Parent(
          rs.getInt("user_id"),
          null,
          rs.getString("user_email"),
          rs.getString("first_name"),
          rs.getString("last_name"),
          permissions));
      }
      return parents;

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ArrayList<Parent> readParentByChildID(int id) {
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_PARENT_ID_BY_CHILD_ID_SQL);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      rs.beforeFirst();
      ArrayList<Parent> parents = new ArrayList<>();
      while (rs.next()) {
        parents.add(readParentByID(rs.getInt("fk_parent_user_id")));
      }
      return parents;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  // Employees
  public Employee readEmployeeByID(int id) {
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_EMPLOYEE_BY_ID_SQL);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      rs.first();
      ArrayList<Permission> permissions = readPermissionsByUserID((rs.getInt("user_id")));
      Group group = readGroupByUserID((rs.getInt("user_id")));
      return new Employee(
        rs.getInt("user_id"),
        null,
        rs.getString("user_email"),
        rs.getString("first_name"),
        rs.getString("last_name"),
        group,
        permissions);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Employee readEmployeeByEmail(String userEmail) {
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_EMPLOYEE_BY_EMAIL_SQL);
      stmt.setString(1, userEmail);
      ResultSet rs = stmt.executeQuery();
      rs.first();
      int userID = rs.getInt("user_id");
      ArrayList<Permission> permissions = readPermissionsByUserID((userID));
      Group group = readGroupByUserID(userID);
      return new Employee(
        userID,
        null,
        rs.getString("user_email"),
        rs.getString("first_name"),
        rs.getString("last_name"),
        group,
        permissions);

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ArrayList<Employee> readAllEmployees()  {
      try (
          Connection con = ConMan.getConnection()
      ) {
        PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_ALL_USERS_BY_TYPE_SQL);
        stmt.setString(1, "ROLE_EMP");
        ResultSet rs = stmt.executeQuery();
        ArrayList<Employee> employees = new ArrayList<>();
        while (rs.next()) {
          ArrayList<Permission> permissions = readPermissionsByUserID((rs.getInt("user_id")));
          Group group = readGroupByUserID((rs.getInt("user_id")));
          employees.add(new Employee(
            rs.getInt("user_id"),
            null,
            rs.getString("user_email"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            group,
            permissions));
        }
        return employees;

      } catch (SQLException e) {
        e.printStackTrace();
      }
      return null;
    }

  public ArrayList<Employee> readAllEmployeesByGroup(int id)  {
      try (
          Connection con = ConMan.getConnection()
      ) {
        PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_ALL_EMPLOYEES_BY_GROUP_ID);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        ArrayList<Employee> employees = new ArrayList<>();
        while (rs.next()) {
          ArrayList<Permission> permissions = readPermissionsByUserID((rs.getInt("user_id")));
          Group group = readGroupByUserID((rs.getInt("user_id")));
          employees.add(new Employee(
            rs.getInt("user_id"),
            null,
            rs.getString("user_email"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            group,
            permissions));
        }
        return employees;

      } catch (SQLException e) {
        e.printStackTrace();
      }
      return null;
    }

  // Children
  public ArrayList<Integer> readChildrenIDByParentID(int id) {
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_CHILDREN_ID_BY_PARENT_ID_SQL);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      rs.beforeFirst();
      ArrayList<Integer> children = new ArrayList<>();
      while (rs.next()) {
        children.add(rs.getInt("fk_child_id"));
      }
      return children;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ArrayList<Child> readChildrenByParentID(int parentId) {
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_CHILDREN_BY_PARENT_ID_SQL);
      stmt.setInt(1, parentId);
      ResultSet rs = stmt.executeQuery();
      rs.beforeFirst();
      ArrayList<Child> children = new ArrayList<>();
      while (rs.next()) {
        int childId = rs.getInt("child_id");
        Wing wing = readWingByChildID(childId);
        ArrayList<Parent> parents = readParentByChildID(childId);
        ArrayList<Allergen> allergens = readAllergenByChildID(childId);
        int wingid = wing.getWingID();
        children.add(new Child(
          childId,
          rs.getString("first_name"),
          rs.getString("last_name"),
          rs.getDate("birthday"),
          rs.getString("address"),
          wingid,
          null,
          parents,
          allergens));
      }
      return children;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ArrayList<Child> readNotChildrenByParentID(int parentId) {
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_NOT_CHILDREN_BY_PARENT_ID_SQL);
      stmt.setInt(1, parentId);
      ResultSet rs = stmt.executeQuery();
      rs.beforeFirst();
      ArrayList<Child> children = new ArrayList<>();
      while (rs.next()) {
        int childId = rs.getInt("child_id");
        Wing wing = readWingByChildID(childId);
        ArrayList<Parent> parents = readParentByChildID(childId);
        ArrayList<Allergen> allergens = readAllergenByChildID(childId);
        int wingid = wing.getWingID();
        children.add(new Child(
          childId,
          rs.getString("first_name"),
          rs.getString("last_name"),
          rs.getDate("birthday"),
          rs.getString("address"),
          wingid,
          null,
          parents,
          allergens));
      }
      return children;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Child readChildById(int id) {
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_CHILD_BY_ID_SQL);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      rs.first();
      Wing wing = readWingByChildID(id);
      ArrayList<Parent> parents = readParentByChildID(id);
      ArrayList<Allergen> allergens = readAllergenByChildID(id);
      int wingid = wing.getWingID();
      return new Child(
        rs.getInt("child_id"),
        rs.getString("first_name"),
        rs.getString("last_name"),
        rs.getDate("birthday"),
        rs.getString("address"),
        wingid,
        null,
        parents,
        allergens);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ArrayList<Child> readAllChildren()  {
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_ALL_CHILDREN_SQL);
      ResultSet rs = stmt.executeQuery();
      rs.beforeFirst();
      ArrayList<Child> children = new ArrayList<>();
      while (rs.next()) {
        int id = rs.getInt("child_id");
        Wing wing = readWingByChildID(id);
        ArrayList<Parent> parents = readParentByChildID(id);
        ArrayList<Allergen> allergens = readAllergenByChildID(id);
        int wingid = wing.getWingID();
        children.add(new Child(
          id,
          rs.getString("first_name"),
          rs.getString("last_name"),
          rs.getDate("birthday"),
          rs.getString("address"),
          wingid,
          null,
          parents,
          allergens));
      }
      return children;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ArrayList<Child> readChildrenByWingID(int id) {
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_CHILDREN_BY_WING_ID_SQL);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      rs.beforeFirst();
      ArrayList<Child> children = new ArrayList<>();
      while (rs.next()) {
        Wing wing = readWingByChildID(id);
        ArrayList<Parent> parents = readParentByChildID(id);
        ArrayList<Allergen> allergens = readAllergenByChildID(id);
        int wingid = wing.getWingID();
        children.add(new Child(
          rs.getInt("child_id"),
          rs.getString("first_name"),
          rs.getString("last_name"),
          rs.getDate("birthday"),
          rs.getString("address"),
          wingid,
          null,
          parents,
          allergens));
      }
      return children;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  // Permissions
  public Permission readPermissionByID(int id) {
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_PERMISSIONS_BY_ID_SQL);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      rs.first();
      return new Permission(
        rs.getInt(1),
        rs.getString(2),
        rs.getString(3));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ArrayList<Permission> readAllPermissions() {
    try (
        ResultSet rs = ConMan.regStat(Statements.DEF_GET_ALL_PERMISSIONS_SQL)
    ) {
      ArrayList<Permission> permissions = new ArrayList<>();
      while (rs.next()) {
        permissions.add(readPermissionByID(rs.getInt(1)));
      }
      return permissions;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ArrayList<Permission> readPermissionsByUserID(int id) {
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_PERMISSION_ID_BY_USER_ID_SQL);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      ArrayList<Permission> permissions = new ArrayList<>();
      while (rs.next()) {
        permissions.add(readPermissionByID(rs.getInt("fk_permission_id")));
      }
      return permissions;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  // Wing
  public Wing readWingByID(int id)  {
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_WING_BY_ID);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      if (!rs.next()){ // checking if resultset is empty.
        return new Wing();
      }
      rs.first();
      return new Wing(
        rs.getInt(1),
        rs.getString(2),
        rs.getString(3));
    } catch (SQLException e){
      e.printStackTrace();
    }
    return null;
  }

  public ArrayList<Wing> readAllWings()  {
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_ALL_WINGS);
      ResultSet rs = stmt.executeQuery();
      ArrayList<Wing> wings = new ArrayList<>();
      while (rs.next()) {
        wings.add(new Wing(
          rs.getInt(1),
          rs.getString(2),
          rs.getString(3)));
      }
      return wings;
      } catch (SQLException e){
      e.printStackTrace();
    }
    return null;
  }

  public Wing readWingByUserID(int id) {
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_WING_BY_USER_ID);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      rs.first();
      return new Wing(
        rs.getInt(1),
        rs.getString(2),
        rs.getString(3));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Wing readWingByChildID(int id) {
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_WING_BY_child_ID);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      if (!rs.next()){ // checking if resultset is empty.
        return new Wing();
      }
      rs.first();
      return new Wing(
        rs.getInt(1),
        rs.getString(2),
        rs.getString(3));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ArrayList<Wing> readWingIDsByUserID(int id) {
    try(
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_WING_IDS_BY_USER_ID);
      ArrayList<Wing> wings = new ArrayList<>();
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      rs.first();
      while (rs.next()) {
        wings.add(readWingByID(rs.getInt("fk_wing_id")));
      }
      return wings;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ArrayList<Wing> readWingIDsByChildID(int id) {
    try(
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_WING_IDS_BY_CHILD_ID);
      ArrayList<Wing> wings = new ArrayList<>();
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      rs.beforeFirst();
      while (rs.next()) {
        wings.add(readWingByID(rs.getInt("fk_wing_id")));
      }
      return wings;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  // Group
  public Group readGroupByID(int id)  {
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_GROUP_BY_ID);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      rs.first();
      return new Group(
        rs.getInt(1),
        rs.getString(2),
        rs.getString(3));
    } catch (SQLException e){
      e.printStackTrace();
    }
    return null;
  }

  public ArrayList<Group> readAllGroups()  {
    try (
        ResultSet rs = ConMan.regStat(Statements.DEF_GET_ALL_GROUPS)
    ) {
      ArrayList<Group> groups = new ArrayList<>();
      while (rs.next()){
        groups.add(new Group(
          rs.getInt(1),
          rs.getString(2),
          rs.getString(3)));
      }
      return groups;
    } catch (SQLException e){
      ConMan.processException(e);
    }
    return null;
  }

  public Group readGroupByUserID(int id) {
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_GROUP_BY_USER_ID);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      if (!rs.next()){ // checking if resultset is empty.
        return new Group();
      }
      rs.first();
      return new Group(
        rs.getInt(1),
        rs.getString(2),
        rs.getString(3));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ArrayList<Group> readGroupIDsByUserID(int id) {
    try(
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_GROUPS_IDS_BY_USER_ID);
      ArrayList<Group> groups = new ArrayList<>();
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      rs.beforeFirst();
      while (rs.next()) {
        groups.add(readGroupByID(rs.getInt("fk_group_id")));
      }
      return groups;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  //Allergen
  public Allergen readAllergenByID(int id)  {
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_ALLERGEN_BY_ID);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      if (!rs.next()){ // checking if ResultSet is empty.
        return new Allergen();
      }
      rs.first();
      return new Allergen(
        rs.getInt(1),
        rs.getString(2),
        rs.getString(3));
    } catch (SQLException e){
      e.printStackTrace();
    }
    return null;
  }

  public ArrayList<Allergen> readAllAllergens()  {
    try (
        ResultSet rs = ConMan.regStat(Statements.DEF_GET_ALL_ALLERGENS)
    ) {
      ArrayList<Allergen> allergens = new ArrayList<>();
      rs.first();
      while (rs.next()){
        allergens.add(new Allergen(
          rs.getInt(1),
          rs.getString(2),
          rs.getString(3)));
      }
      return allergens;
    } catch (SQLException e){
      ConMan.processException(e);
    }
    return null;
  }

  public ArrayList<Allergen> readAllergenByChildID(int id){
    try (
        Connection con = ConMan.getConnection()
    ) {
      PreparedStatement stmt = ConMan.prepStat(con, Statements.DEF_GET_ALLERGEN_BY_CHILD_ID);
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      rs.beforeFirst();
      ArrayList<Allergen> allergens = new ArrayList<>();
      while (rs.next()) {
        allergens.add(readAllergenByID(rs.getInt("fk_allergen_id")));
      }
      return allergens;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
}