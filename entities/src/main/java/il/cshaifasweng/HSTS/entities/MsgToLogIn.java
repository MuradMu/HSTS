package il.cshaifasweng.HSTS.entities;
import java.io.Serializable;

public class MsgToLogIn implements Serializable {
    private static final long serialVersionUID = 1L;
    String request;
    String password;
    String username;

    String LogInFlag;
    private User user = null;

    private Teacher teacher;


    public MsgToLogIn(String request, String password, String username) {
        this.request = request;
        this.password = password;
        this.username = username;
    }

    public MsgToLogIn(String request) {
        this.request = request;
    }
    public void setRequest(String request) {
        this.request = request;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUser(User user){
        this.user = user;
    }
    public void setLogInFlag(String bool){
        this.LogInFlag = bool;
    }
    public String getLogInFlag(){return this.LogInFlag;}
    public User getUser(){return this.user;}
    public String getRequest() {
        return request;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setTeacher(Teacher teacher){this.teacher = teacher;}
    public Teacher getTeacher(){return this.teacher;}
}
