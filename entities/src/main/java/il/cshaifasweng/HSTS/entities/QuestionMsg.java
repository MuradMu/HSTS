package il.cshaifasweng.HSTS.entities;
import java.io.Serializable;

public class QuestionMsg implements Serializable{
    private static final long serialVersionUID = 1L;
    String request;
    private Question question;

    private Teacher TeacherWhoCreate;

    private int Question_num;

    public QuestionMsg(String hashtag, Question question, Teacher teacher) {
        this.request = hashtag;
        this.question = question;
        this.TeacherWhoCreate = teacher;
    }
    public QuestionMsg(String hashtag, Question question) {
        this.request = hashtag;
        this.question = question;
    }
   // public List<Course> getCourses_of_question(){return this.courses_of_question;}
    public void setQuestion_num(int question_num) {
        Question_num = question_num;
    }
    public int getQuestion_num(){
        return this.Question_num;
    }

    public Question getQuestion(){return this.question;}

    public Object getRequest() {return this.request;}

    public Teacher getTeacherWhoCreate() {
        return TeacherWhoCreate;
    }
}
