package model;

import java.io.Serializable;
import java.util.Objects;

public class Salary implements Serializable {

    private static final long serialVersionUID = -2565570290688784024L;

    private Integer id;
    private String login;
    private Double salary;
    private Integer month;
    private Integer tax;
    private Double total;
    private Double award;

    public Salary(Integer id, String login, Double salary, Integer month, Integer tax, Double total) {
        this.id = id;
        this.login = login;
        this.salary = salary;
        this.month = month;
        this.tax = tax;
        this.total = total;
    }

    public Double getAward() {
        return award;
    }

    public void setAward(Double award) {
        this.award = award;
    }

    public Salary(Integer id, String login, Double salary, Integer month, Integer tax) {
        this.id = id;
        this.login = login;
        this.salary = salary;
        this.month = month;
        this.tax = tax;
    }

    public Salary(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin(){
        return login;
    }

    public void setLogin(String login){
        this.login = login;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getTax() {
        return tax;
    }

    public void setTax(Integer tax) {
        this.tax = tax;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Salary)) return false;
        Salary salary = (Salary) o;
        return id.equals(salary.id) &&
                login.equals(salary.login) &&
                this.salary.equals(salary.salary) &&
                month.equals(salary.month) &&
                tax.equals(salary.tax) &&
                total.equals(salary.total) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, salary, month, tax, total);
    }

    @Override
    public String toString() {
        return "Salary{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", salary=" + salary +
                ", month=" + month +
                ", tax=" + tax +
                ", total=" + total +
                ", award=" + award +
                '}';
    }
}
