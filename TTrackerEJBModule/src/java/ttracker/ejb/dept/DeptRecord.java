package ttracker.ejb.dept;

/**
 * Information of department
 */
public class DeptRecord {

    private int deptId;/* department number */
    private String deptName;/* department name */

    public DeptRecord(int deptId, String deptName) {
        this.deptId = deptId;
        this.deptName = deptName;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getName() {
        return deptName;
    }

    public void setName(String deptName) {
        this.deptName = deptName;
    }
}
