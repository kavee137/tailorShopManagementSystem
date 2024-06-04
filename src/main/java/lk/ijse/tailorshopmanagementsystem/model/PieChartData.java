package lk.ijse.tailorshopmanagementsystem.model;

public class PieChartData {
    private String label;
    private double value;

    public PieChartData(String label, double value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
