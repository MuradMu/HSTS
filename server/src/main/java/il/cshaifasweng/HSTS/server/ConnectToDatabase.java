package il.cshaifasweng.HSTS.server;

import il.cshaifasweng.HSTS.entities.*;
import il.cshaifasweng.HSTS.entities.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConnectToDatabase {
    private static Session session;

    private static List<Student> students;
    private static List<Teacher> teachers;

    private static List<User> users;
    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(Grade.class);
        configuration.addAnnotatedClass(Student.class);
        configuration.addAnnotatedClass(Course.class);
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Teacher.class);
        configuration.addAnnotatedClass(Exam.class);
        configuration.addAnnotatedClass(Question.class);
        configuration.addAnnotatedClass(ExamSubmittion.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    private static List<Course> getAllCourses() throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Course> query = builder.createQuery(Course.class);
        query.from(Course.class);
        List<Course> data = session.createQuery(query).getResultList();
        return data;
    }

    private static List<Grade> getAllGrades() throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Grade> query = builder.createQuery(Grade.class);
        query.from(Grade.class);
        List<Grade> data = session.createQuery(query).getResultList();
        return data;
    }

    public static List<Student> getAllStudents(){
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Student> query = builder.createQuery(Student.class);
        query.from(Student.class);
        List<Student> data = session.createQuery(query).getResultList();
        return data;
    }

    public static List<User> getAllUsers(){
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        query.from(User.class);
        List<User> data = session.createQuery(query).getResultList();
        if (data.isEmpty()) {
            return null;
        }
        return data;
    }

    public static List<Teacher> getAllTeachers(){
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Teacher> query = builder.createQuery(Teacher.class);
        query.from(Teacher.class);
        List<Teacher> data = session.createQuery(query).getResultList();
        return data;
    }

    public static List<Question> getQuestionsByTeacher(Teacher teacher) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Question> questionQuery = builder.createQuery(Question.class);
        Root<Question> questionRoot = questionQuery.from(Question.class);

        // Join the Teacher entity to filter by teacher_id
        Join<Question, Teacher> teacherJoin = questionRoot.join("teacher");

        questionQuery.select(questionRoot)
                .where(builder.equal(teacherJoin.get("id"), teacher.getId()));

        TypedQuery<Question> questionTypedQuery = session.createQuery(questionQuery);
        return questionTypedQuery.getResultList();
    }
    public static List<Exam> getExamsByTeacher(Teacher teacher) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Exam> ExamQuery = builder.createQuery(Exam.class);
        Root<Exam> ExamRoot = ExamQuery.from(Exam.class);

        // Join the Teacher entity to filter by teacher_id
        Join<Exam, Teacher> teacherJoin = ExamRoot.join("teacher");

        ExamQuery.select(ExamRoot)
                .where(builder.equal(teacherJoin.get("id"), teacher.getId()));

        TypedQuery<Exam> ExamTypedQuery = session.createQuery(ExamQuery);
        return ExamTypedQuery.getResultList();
    }
    public static void save_all(Session session) throws Exception {
        for(Student student : getAllStudents()){
            session.save(student);
            session.flush();
        }
        for(Course course : getAllCourses()){
            session.save(course);
            session.flush();
        }
    }

    public static Student CreateStudent(String id, String first, String last, String username, String role, String mail, String password){
        Student student = new Student(id,first,last,username,role,mail,password);
        session.save(student);
        session.flush();
        return student;
    }

    public static Teacher CreateTeacher(String id, String first, String last, String username, String role, String mail, String password){
        Teacher teacher = new Teacher(id,first,last,username,role,mail,password);
        session.save(teacher);
        session.flush();
        return teacher;
    }

    public static Course CreateCourse(String id, String name, Teacher teacher){
        Course course = new Course(id, name, teacher);
        session.save(course);
        session.flush();
        return course;
    }

    public static void set_courseGrade(Student student, Course course, int grade, ExamSubmittion exam){
        Grade grade1 = new Grade(grade, student, course, exam);
        session.save(grade1);
        session.save(student);
        session.save(course);
        session.flush();
    }

    public static void set_courseGrade2(Student student, Course course, int sum, ExamSubmittion exam) throws Exception {
        List<Student> studentList = getAllStudents();
        List<Course> courseList = getAllCourses();
        Course course1 = null;
        Student student1 = null;
        for(Student student2 : studentList){
            if(student.getId_num() == student2.getId_num()){
                student1 = student2;
            }
        }
        for(Course course2 : courseList){
            if(course2.getId().equals(course.getId())){
                course1 = course2;
            }
        }
        boolean flag = true;
        if(student1 != null){
            if(course1 != null){
                List<Grade> grades = student1.getGrades();
                for(Grade grade1 : grades){
                    if(grade1.getCourse() == course1){
                        grade1.setGrade(sum);
                        session.flush();
                        flag = false;
                    }
                }
                if(flag){
                    Grade grade = new Grade(sum, student1, course1, exam);
                    session.save(grade);
                    session.flush();
                }
            }
        }
    }

    public static void Add_Course(Student student, Course course){
        if (student != null && course != null) {
            student.add_course(course);
        } else {
            System.out.print("STUDENT IS NULL!!\n");
        }
        session.save(student);
        session.save(course);
        session.flush();
    }
    public static void CreateData() throws Exception {

        String role = "student";
        Student s1 = CreateStudent("32411", "Faisal", "Omari","faisalo321", role, "faisal@gmail.com", "1231");
        Student s2 = CreateStudent("32412", "Mohammad", "Arrabi","mohammadar", role, "mohammadar@gmail.com", "4213");
        Student s3 = CreateStudent("32413", "Abbas", "Ismail","abbasosh", role, "abbasosh@gmail.com", "5432");
        Student s4 = CreateStudent("32414", "Murad", "Murad","muradm", role, "murdam@gmail.com", "3241");
        Student s5 = CreateStudent("32415", "Zaher", "Zoabi","zozo", role, "zozozoabi@gmail.com", "6423");
        Student s6 = CreateStudent("32416", "David", "Jonas","david_jo", role, "david_jo@gmail.com", "6525");
        Student s7 = CreateStudent("32417", "Sam", "Brize","sambrize", role, "sambrize21@gmail.com", "9945");
        Student s8 = CreateStudent("32418", "Walter", "White","wallte", role, "walteroo@gmail.com", "5242");
        Student s9 = CreateStudent("32419", "Lionel", "Jordan","leojo", role, "leojoff@gmail.com", "5534");
        Student s10 = CreateStudent("32420", "Chris", "Barry","chrisboy", role, "chrisboy@gmail.com", "4213");

        role = "teacher";
        Teacher t1 = CreateTeacher("24210", "Mohammad", "Omari","mohamom", role, "23mohamom@gmail.com", "8667");
        Teacher t2 = CreateTeacher("24211", "Dan", "Lior","dany", role, "34dany2@gmail.com", "7755");
        Teacher t3 = CreateTeacher("24212", "George", "Williams","georgewill", role, "43georgewill2@gmail.com", "6543");

        Course c1 = CreateCourse("2313-21", "Algorithms", t1);
        Course c2 = CreateCourse("3214-41", "Software Engineering", t3);
        Course c3 = CreateCourse("3288-53", "Operating Systems", t1);
        Course c4 = CreateCourse("4324-16", "Image processing", t2);
        Course c5 = CreateCourse("3211-29", "Computer Graphics", t3);

        Add_Course(s1, c1);
        Add_Course(s1, c2);
        Add_Course(s1, c5);
        Add_Course(s2, c3);
        Add_Course(s2, c2);
        Add_Course(s2, c4);
        Add_Course(s2, c1);
        Add_Course(s3, c1);
        Add_Course(s3, c3);
        Add_Course(s4, c4);
        Add_Course(s4, c5);
        Add_Course(s4, c1);
        Add_Course(s4, c2);
        Add_Course(s5, c4);
        Add_Course(s5, c5);
        Add_Course(s5, c2);
        Add_Course(s5, c3);
        Add_Course(s6, c1);
        Add_Course(s6, c2);
        Add_Course(s6, c4);
        Add_Course(s6, c3);
        Add_Course(s7, c4);
        Add_Course(s7, c5);
        Add_Course(s7, c1);
        Add_Course(s7, c2);
        Add_Course(s8, c4);
        Add_Course(s8, c5);
        Add_Course(s8, c3);
        Add_Course(s8, c2);
        Add_Course(s8, c4);
        Add_Course(s9, c5);
        Add_Course(s9, c3);
        Add_Course(s10, c1);
        Add_Course(s10, c2);

      List<String> answers = new ArrayList<>();
      String questionText = "Which of the following standard algorithms is not Dynamic Programming based.";
      String correctAns = "D";
      answers.add("Bellman–Ford Algorithm for single source shortest path");
      answers.add("Floyd Warshall Algorithm for all pairs shortest paths");
      answers.add("0-1 Knapsack problem");
      answers.add("Prim's Minimum Spanning Tree");
      Question q1 = new Question(questionText, answers, correctAns, t1);
      answers.clear();

      questionText = "Which of the following is not true about comparison-based sorting algorithms?";
      correctAns = "D";
      answers.add("The minimum possible time complexity of a comparison-based sorting algorithm is O(n(log(n)) for a random input array");
      answers.add("Any comparison based sorting algorithm can be made stable by using position as a criteria when two elements are compared");
      answers.add("Counting Sort is not a comparison based sorting algorithm");
      answers.add("Heap Sort is not a comparison based sorting algorithm.");
      Question q2 = new Question(questionText, answers, correctAns, t1);
      answers.clear();

      questionText = "Which of the following is not O(n^2)?";
      correctAns = "C";
      answers.add("(15) * n2");
      answers.add("n1.98");
      answers.add("n3/(sqrt(n))");
      answers.add("(20) * n2");
      Question q3 = new Question(questionText, answers, correctAns, t1);
      answers.clear();

      questionText = "Which of the following is not a backtracking algorithm?";
      correctAns = "C";
      answers.add("Knight tour problem");
      answers.add("N queen problem");
      answers.add("Tower of hanoi");
      answers.add("M coloring problem");
      Question q4 = new Question(questionText, answers, correctAns, t1);
      answers.clear();

      questionText = "In a complete k-ary tree, every internal node has exactly k children. The number of leaves in such a tree with n internal nodes is: ";
      correctAns = "C";
      answers.add("nk");
      answers.add("(n – 1) k+ 1");
      answers.add("n( k – 1) + 1");
      answers.add("n( k – 1)");
      Question q5 = new Question(questionText, answers, correctAns, t1);
      answers.clear();

      questionText = "What is the time complexity of Floyd–Warshall algorithm to calculate all pair shortest path in a graph with n vertices?";
      correctAns = "D";
      answers.add("O(n2log(n))");
      answers.add("Theta(n2log(n))");
      answers.add("Theta(n4)");
      answers.add("Theta(n3)");
      Question q6 = new Question(questionText, answers, correctAns, t1);
      answers.clear();

      questionText = "Which thing help people to understand the world?";
      correctAns = "A";
      answers.add("Symbol");
      answers.add("Language");
      answers.add("People");
      answers.add("Communication");
      Question q7 = new Question(questionText, answers, correctAns, t1);
      answers.clear();

      questionText = "Cultural universal revolve around human survival such as";
      correctAns = "D";
      answers.add("Food");
      answers.add("Cloth");
      answers.add("Shelter");
      answers.add("All of above");
      Question q8 = new Question(questionText, answers, correctAns, t1);
      answers.clear();

      questionText = "Marx' developed is the concept of";
      correctAns = "B";
      answers.add("True consciousness");
      answers.add("false consciousness");
      answers.add("Class consciousness");
      answers.add("class consciousness");
      Question q9 = new Question(questionText, answers, correctAns, t1);
      answers.clear();

      questionText = "A culture standard for discerning what is good in society is";
      correctAns = "C";
      answers.add("Value");
      answers.add("Norms");
      answers.add("Belief");
      answers.add("Practice");
      Question q10 = new Question(questionText, answers, correctAns, t1);
      answers.clear();

      questionText = "A society is created by humans and human interaction is known as";
      correctAns = "B";
      answers.add("Institutionalization");
      answers.add("Habitualization");
      answers.add("Socialization");
      answers.add("Capitalization");
      Question q11 = new Question(questionText, answers, correctAns, t2);
      answers.clear();

      questionText = "What is the capital city of Afghanistan ?";
      correctAns = "C";
      answers.add("Tripoli");
      answers.add("Beijing");
      answers.add("Kabul");
      answers.add("Algiers");
      Question q12 = new Question(questionText, answers, correctAns, t2);
      answers.clear();

      questionText = "What is the capital city of Algeria ?";
      correctAns = "C";
      answers.add("Tallinn");
      answers.add("Ankarea");
      answers.add("Algiers");
      answers.add("Ankara");
      Question q13 = new Question(questionText, answers, correctAns, t2);
      answers.clear();

      questionText = "What is the capital city of Albania ?";
      correctAns = "D";
      answers.add("Thimphu");
      answers.add("Tripoli");
      answers.add("Sofia");
      answers.add("Tirana");
      Question q14 = new Question(questionText, answers, correctAns, t2);
      answers.clear();

      questionText = "What is the capital city of Andorra ?";
      correctAns = "A";
      answers.add("Andorra la Vella");
      answers.add("Jakarta");
      answers.add("Tripoli");
      answers.add("Sofia");
      Question q15 = new Question(questionText, answers, correctAns, t2);
      answers.clear();

      questionText = "What is the capital city of Antigua and Barbuda ?";
      correctAns = "C";
      answers.add("Canberra");
      answers.add("Barcelona");
      answers.add("Saint John's");
      answers.add("Dhaka");
      Question q16 = new Question(questionText, answers, correctAns, t2);
      answers.clear();

      questionText = "Number of primitive data types in Java are?";
      correctAns = "C";
      answers.add("6");
      answers.add("7");
      answers.add("8");
      answers.add("9");
      Question q17 = new Question(questionText, answers, correctAns, t2);
      answers.clear();

      questionText = "What is the size of float and double in java?";
      correctAns = "A";
      answers.add("32 and 64");
      answers.add("32 and 32");
      answers.add("64 and 64");
      answers.add("64 and 32");
      Question q18 = new Question(questionText, answers, correctAns, t2);
      answers.clear();

      questionText = "Automatic type conversion is possible in which of the possible cases?";
      correctAns = "B";
      answers.add("Byte to int");
      answers.add("Int to Long");
      answers.add("Long to int");
      answers.add("Short to int");
      Question q19 = new Question(questionText, answers, correctAns, t2);
      answers.clear();

      questionText = "\n" +
              "Select the valid statement.";
      correctAns = "B";
      answers.add("char[] ch = new char(5)");
      answers.add("char[] ch = new char[5]");
      answers.add("char[] ch = new char()");
      answers.add("char[] ch = new char[]");
      Question q20 = new Question(questionText, answers, correctAns, t2);
      answers.clear();

      questionText = "When an array is passed to a method, what does the method receive?";
      correctAns = "A";
      answers.add("The reference of the array");
      answers.add("A copy of the array");
      answers.add("Length of the array");
      answers.add("Copy of first element");
      Question q21 = new Question(questionText, answers, correctAns, t3);
      answers.clear();

      questionText = "Select the valid statement to declare and initialize an array.";
      correctAns = "D";
      answers.add("int[] A = {}");
      answers.add("int[] A = (1, 2, 3)");
      answers.add("int[][] A = {1,2,3}");
      answers.add("int[] A = {1, 2, 3}");
      Question q22 = new Question(questionText, answers, correctAns, t3);
      answers.clear();

      questionText = "What is the main function of the command interpreter?";
      correctAns = "C";
      answers.add("to provide the interface between the API and application program");
      answers.add("to handle the files in the operating system");
      answers.add("to get and execute the next user-specified command");
      answers.add("none of the mentioned");
      Question q23 = new Question(questionText, answers, correctAns, t3);
      answers.clear();

      questionText = "In Operating Systems, which of the following is/are CPU scheduling algorithms?";
      correctAns = "D";
      answers.add("Priority");
      answers.add("Round Robin");
      answers.add("Shortest Job First");
      answers.add("All of the mentioned");
      Question q24 = new Question(questionText, answers, correctAns, t3);
      answers.clear();

      questionText = "To access the services of the operating system, the interface is provided by the ___________";
      correctAns = "B";
      answers.add("Library");
      answers.add("System calls");
      answers.add("Assembly instructions");
      answers.add("API");
      Question q25 = new Question(questionText, answers, correctAns, t3);
      answers.clear();

      questionText = " CPU scheduling is the basis of ___________";
      correctAns = "A";
      answers.add("multiprogramming operating systems");
      answers.add("larger memory sized systems");
      answers.add("multiprocessor systems");
      answers.add("none of the mentioned");
      Question q26 = new Question(questionText, answers, correctAns, t3);
      answers.clear();

      questionText = "Which one of the following is not true?";
      correctAns = "B";
      answers.add("kernel remains in the memory during the entire computer session");
      answers.add(" kernel is made of various modules which can not be loaded in running operating system");
      answers.add("kernel is the first part of the operating system to load into memory during booting");
      answers.add(" kernel is the program that constitutes the central core of the operating system");
      Question q27 = new Question(questionText, answers, correctAns, t3);
      answers.clear();

      questionText = "Which one of the following errors will be handle by the operating system?";
      correctAns = "D";
      answers.add("lack of paper in printer");
      answers.add("connection failure in the network");
      answers.add("power failure");
      answers.add("all of the mentioned");
      Question q28 = new Question(questionText, answers, correctAns, t3);
      answers.clear();

      questionText = "If a process fails, most operating system write the error information to a ______";
      correctAns = "C";
      answers.add("new file");
      answers.add("another running process");
      answers.add("log file");
      answers.add("none of the mentioned");
      Question q29 = new Question(questionText, answers, correctAns, t3);
      answers.clear();

      questionText = "Where is the operating system placed in the memory?";
      correctAns = "A";
      answers.add("either low or high memory (depending on the location of interrupt vector)");
      answers.add("in the low memory");
      answers.add("in the high memory");
      answers.add("none of the mentioned");
      Question q30 = new Question(questionText, answers, correctAns, t3);
      answers.clear();


      AddQuestion2(q1);
      AddQuestion2(q2);
      AddQuestion2(q3);
      AddQuestion2(q4);
      AddQuestion2(q5);
      AddQuestion2(q6);
      AddQuestion2(q7);
      AddQuestion2(q8);
      AddQuestion2(q9);
      AddQuestion2(q10);

      AddQuestion2(q11);
      AddQuestion2(q12);
      AddQuestion2(q13);
      AddQuestion2(q14);
      AddQuestion2(q15);
      AddQuestion2(q16);
      AddQuestion2(q17);
      AddQuestion2(q18);
      AddQuestion2(q19);
      AddQuestion2(q20);

      AddQuestion2(q21);
      AddQuestion2(q22);
      AddQuestion2(q23);
      AddQuestion2(q24);
      AddQuestion2(q25);
      AddQuestion2(q26);
      AddQuestion2(q27);
      AddQuestion2(q28);
      AddQuestion2(q29);
      AddQuestion2(q30);
    }

    public static List<String> Students_names(Session session) throws Exception {
        List<Student> Students = getAllStudents();
        List<String> names = new ArrayList<>();
        for (Student student:Students) {
            names.add(student.getStudentName());
        }
        return names;
    }

    public static Map<String, Integer> getStudentGrades(Student student){
        Map<String, Integer> grades = student.getGradesMap();
        return grades;
    }

    public static void updateGrade(int Grade_ID, int new_grade) throws Exception {
        List<Grade> grades = getAllGrades();
        for(Grade grade : grades){
            if (grade.getId() == Grade_ID){
                grade.updateGrade(new_grade);
                session.beginTransaction();
                session.save(grade);
                session.flush();
                session.getTransaction().commit();
                break;
            }
        }
    }
    public static QuestionMsg AddQuestion(Question question, Teacher teacher) throws Exception {
        System.out.print("\nADDING QUESTION\n");
        session.beginTransaction();
        Question QuestionToADD = new Question(question.getQuestionText(),question.getAnswers(),
                question.getCorrectAnswer(),question.getTeacher());
        session.save(QuestionToADD);
        session.flush();
        teacher.getTeacherQuestionsList().add(QuestionToADD);
        session.getTransaction().commit();
        QuestionMsg msg = new QuestionMsg("#ReturningQuestion",QuestionToADD,teacher);
        return msg;
    }
    public static void AddQuestion2(Question question) throws Exception {
//        session.beginTransaction();
        session.save(question);
        question.getTeacher().addQuestion(question);
        session.flush();
//        session.getTransaction().commit();
    }
    public static Exam addExam(Exam exam) throws Exception {
        System.out.print("\nADDING Exam\n");
        session.beginTransaction();
        Exam ExamToADD = new Exam(exam.getTeacher(), exam.getCourse(), exam.getQuestions(), exam.getTime(),
                exam.getQuestionPoints(), exam.getDescription_Teacher(), exam.getDescription_Student());
        session.save(ExamToADD);
        session.flush();
        session.getTransaction().commit();
        return ExamToADD;
    }
    public static ExamSubmittion AddExamSubmittin(ExamSubmittion exam) {
        System.out.print("\nADDING Exam\n");
        session.beginTransaction();
        ExamSubmittion ExamSubmittionToADD = new ExamSubmittion(exam.getStudent(), exam.getExam(), exam.getAnswers());
        session.save(ExamSubmittionToADD);
        session.flush();
        session.getTransaction().commit();
        return ExamSubmittionToADD;
    }

    public static Exam ShareExam ( Exam exam, String Password ){
        session.beginTransaction();
        // Load or get the entity you want to update
        Exam ExamToShare = session.load(Exam.class,exam.getId_num() );
        if (ExamToShare != null) {
            // Modify the desired property
            ExamToShare.setPassword(Password);
            ExamToShare.setShared(true);
            // Commit the transaction to persist the changes
            session.save(ExamToShare);
            session.flush();
            session.getTransaction().commit();

            // Changes successfully saved
            System.out.println("Exam shared: " + exam.getId_num());
        } else {
            // Exam not found
            System.out.println("Exam not found. ERROR sharing exam ");
        }

        return ExamToShare;
    }

    public static List<Student> getStudents() {
        return students;
    }

    public static List<User> getUsers() {
        return users;
    }

    public static Session initializeDatabase() throws IOException
    {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            users = getAllUsers();
            if(users == null){
                CreateData();
            }
            users = getAllUsers();
            students = getAllStudents();
            teachers = getAllTeachers();
            session.getTransaction().commit();
            return session;
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
        }
        return null;
    }
    public static void EndConnection(){
        session.getTransaction().commit(); // Save everything.
        session.close();
    }

}
