package model;

import java.io.Serializable;
import java.util.Objects;

public class Profile implements Serializable {

    private static final long serialVersionUID = -2565570290688784024L;

    private String name;
    private Integer id;
    private Integer id_user;
    private String fam;
    private String otch;
    private String emailUser;
    private  Integer phone;

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public Profile(String name, Integer id, String fam, String otch, String emailUser, Integer phone) {
        this.name = name;
        this.id = id;
        this.fam = fam;
        this.otch = otch;
        this.emailUser = emailUser;
        this.phone = phone;
    }

    public Profile() {
    }

    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFam() {
        return fam;
    }

    public void setFam(String fam) {
        this.fam = fam;
    }

    public String getOtch() {
        return otch;
    }

    public void setOtch(String otch) {
        this.otch = otch;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profile)) return false;
        Profile profile = (Profile) o;
        return name.equals(profile.name) &&
                fam.equals(profile.fam) &&
                otch.equals(profile.otch) &&
                phone.equals(profile.phone) &&
                id.equals(profile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, fam, otch, phone, id);
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", id_user=" + id_user +
                ", fam='" + fam + '\'' +
                ", otch='" + otch + '\'' +
                ", emailUser='" + emailUser + '\'' +
                ", phone=" + phone +
                '}';
    }
}
