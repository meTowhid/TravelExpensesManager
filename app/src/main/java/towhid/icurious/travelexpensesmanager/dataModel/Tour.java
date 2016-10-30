package towhid.icurious.travelexpensesmanager.dataModel;


import java.util.ArrayList;

public class Tour {

    private int id;
    private String title;
    private String description;
    private String goingDate;
    private String returnDate;
    private double budget;
    private double totalExpenses;
    private ArrayList<Member> members;

    public Tour() {
    }

    public Tour(int id, String title, String description, String goingDate, String returnDate, double budget, double totalExpenses) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.goingDate = goingDate;
        this.returnDate = returnDate;
        this.budget = budget;
        this.totalExpenses = totalExpenses;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGoingDate() {
        return goingDate;
    }

    public void setGoingDate(String goingDate) {
        this.goingDate = goingDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }
}