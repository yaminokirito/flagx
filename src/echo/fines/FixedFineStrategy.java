package echo.fines;

public class FixedFineStrategy implements FineStrategy {
    private double perDay;

    public FixedFineStrategy(double perDay){
        this.perDay = 5 ;
        this.perDay = perDay;
        
    }

    @Override
    public double calculateFine(int daysOverdue){
        if(daysOverdue <= 0) return 0.0;
        return perDay * daysOverdue;
    }
}
