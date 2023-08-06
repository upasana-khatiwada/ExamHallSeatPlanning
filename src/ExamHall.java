public class ExamHall {
    private int rows;
    private int columns;
    private Seat[][] seats;

    public ExamHall(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        seats = new Seat[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                seats[i][j] = new Seat();
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public boolean isSeatOccupied(int row, int column) {
        return seats[row][column].isOccupied();
    }

    public String getStudentNameAt(int row, int column) {
        return seats[row][column].getStudentName();
    }

    public void assignSeat(int row, int column, String studentName) {
        if (isValidSeat(row, column)) {
            seats[row][column].assignStudent(studentName);
        }
    }

    private boolean isValidSeat(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns && !seats[row][column].isOccupied();
    }
}
