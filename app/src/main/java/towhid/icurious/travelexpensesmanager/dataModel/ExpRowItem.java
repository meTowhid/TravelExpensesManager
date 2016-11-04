package towhid.icurious.travelexpensesmanager.dataModel;

/**
 * Created by Towhid on 11/2/2016.
 */

public class ExpRowItem {
    private Member member;
    private ExpField expField;

    public ExpRowItem(Member member, ExpField expField) {
        this.member = member;
        this.expField = expField;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public ExpField getExpField() {
        return expField;
    }

    public void setExpField(ExpField expField) {
        this.expField = expField;
    }
}
