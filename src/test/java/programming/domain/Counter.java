package programming.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;

public class Counter implements Serializable {

    private static final long serialVersionUID = 1L;
    private int count = 0;
    private Employee emp;
    private boolean isEnable = false;

    volatile int x = 1;
    int y = 2;
    double doub = 123_456.0;

    public Counter copy() {
        try {
            return new ObjectMapper().readValue(new JSONObject(this).toString(), Counter.class);
        }
        catch (final IOException e) {
            throw new RuntimeException("Object Clone Failure", e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Counter other = (Counter) obj;
        if (count != other.count) {
            return false;
        }
        if (emp == null) {
            if (other.emp != null) {
                return false;
            }
        }
        else if (!emp.equals(other.emp)) {
            return false;
        }
        if (isEnable != other.isEnable) {
            return false;
        }
        return true;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @return the emp
     */
    public Employee getEmp() {
        return emp;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + count;
        result = prime * result + (emp == null ? 0 : emp.hashCode());
        result = prime * result + (isEnable ? 1231 : 1237);
        return result;
    }

    /**
     * @return the isEnable
     */
    public boolean isEnable() {
        return isEnable;
    }

    public void m1() {
        x = 11;
        y = 22;
    }

    public void m2() {
        x = 22;
        y = 33;
    }

    /**
     * @param count
     *            the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @param emp
     *            the emp to set
     */
    public void setEmp(Employee emp) {
        this.emp = emp;
    }

    /**
     * @param isEnable
     *            the isEnable to set
     */
    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * @param x
     *            the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @param y
     *            the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }
}