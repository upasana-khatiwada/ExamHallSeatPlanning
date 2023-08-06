public class Seat {
    private boolean isOccupied;
    private String studentName;

    public Seat() {
        isOccupied = false;
        studentName = "";
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void assignStudent(String studentName) {
        this.studentName = studentName;
        isOccupied = true;
    }

    public void unassignStudent() {
        this.studentName = "";
        isOccupied = false;
    }

    public String getStudentName() {
        return studentName;
    }
}