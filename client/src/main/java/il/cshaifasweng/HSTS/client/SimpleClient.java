package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.*;
import il.cshaifasweng.HSTS.entities.*;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.HSTS.client.ocsf.AbstractClient;

import java.util.List;

public class SimpleClient extends AbstractClient {

	private List<Student> studentList;
	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		System.out.print("Receiving a message");
		if (msg instanceof MsgToLogIn) {
			MsgToLogIn message = (MsgToLogIn) msg;
			if (message.getRequest().equals("#LogInReply")) {
				EventBus.getDefault().post(new LogInEvent(message));
			}
		} else if (msg instanceof QuestionMsg) {
			QuestionMsg message2 = (QuestionMsg) msg;
			if (message2.getRequest().equals("#ReturningQuestion")) {
				EventBus.getDefault().post(new ReceivingQuestionEvent(message2));
			}
		} else if (msg instanceof List<?>) {
			studentList = (List<Student>) msg;
			EventBus.getDefault().post(studentList);
		} else if (msg instanceof MsgExamCreation) {
			MsgExamCreation message = (MsgExamCreation) msg;
			if(message.getRequest().equals("#ExamCreationDone")){
				EventBus.getDefault().post(new CreateExamEvent(message));
			}
		} else if(msg instanceof MsgUpdateExam){
			MsgUpdateExam message = (MsgUpdateExam) msg;
			if(message.getRequest().equals("#ExamUpdatingDone")){
				EventBus.getDefault().post(new UpdateExamEvent(message));
			}
		} else if(msg instanceof ShareExamMsg){
			ShareExamMsg message =(ShareExamMsg) msg;
			if(message.getRequest().equals("#ExamSharingDone")) {
				EventBus.getDefault().post(new ShareExamEvent(message));
			}
		} else if(msg instanceof TakeExamMsg){
			TakeExamMsg message =(TakeExamMsg) msg;
			EventBus.getDefault().post(new StartExamEvent(message));
		} else if(msg instanceof MsgExamSubmittion){
			MsgExamSubmittion message =(MsgExamSubmittion) msg;
			EventBus.getDefault().post(new ExamSubmitEvent(message));
		} else if(msg instanceof MsgBringExecutedExams){
			MsgBringExecutedExams message =(MsgBringExecutedExams) msg;
			EventBus.getDefault().post(new ReceivingExecutedExamsEvent(message));
		} else if(msg instanceof MsgGetGrades){
			MsgGetGrades message =(MsgGetGrades) msg;
			EventBus.getDefault().post(new GradesRecievedEvent(message));
		} else {
			System.out.print("UNKOWN MESSAGE TYPE");
		}
	}


	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

	public List<Student> getStudentList(){return this.studentList;}

}
