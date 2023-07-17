package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.ShareExamMsg;

public class ShareExamEvent {
    private ShareExamMsg shareExamMsg;

    public ShareExamEvent(ShareExamMsg shareExamMsg) {
        this.shareExamMsg = shareExamMsg;
    }

    public ShareExamMsg getShareExamMsg() {
        return shareExamMsg;
    }

    public void setShareExamMsg(ShareExamMsg shareExamMsg) {
        this.shareExamMsg = shareExamMsg;
    }
}
