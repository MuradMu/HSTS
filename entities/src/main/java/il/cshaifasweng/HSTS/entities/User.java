package il.cshaifasweng.HSTS.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Users")
public class User  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_num;

    @Column(name = "ID")
    private String id;

    @Column(name = "FirstName")
    private String first;

    @Column(name = "LastName")
    private String last;

    @Column(name = "Username")
    private String username;

    @Column(name = "Role")
    private String role;

    @Column(name = "Mail")
    private String mail;

    @Column(name = "Password")
    private String password;

    public User(String id, String first, String last, String username, String role, String mail, String password){
        this.id = id;
        this.first = first;
        this.last = last;
        this.username = username;
        this.role = role;
        this.mail = mail;
        this.password = password;
    }

    public User() {

    }

    public String getUsername(){return this.username;}
    public String getId(){return this.id;}
    public String getFirst(){return this.first;}
    public String getLast(){return this.last;}
    public String getRole(){return this.role;}
    public String getMail(){return this.mail;}

    public String getPassword(){return this.password;}

    public String getFullName() {return (this.first + " " + this.last);}

    public int getId_num() {
        return id_num;
    }
}
