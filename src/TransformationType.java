
public enum TransformationType {
    Level,
    MonthlyDelta,
    YearlyDelta;

    @Override
    public String toString() {
        return switch (this) {
            case Level -> "Level";
            case MonthlyDelta -> "Period Change";
            case YearlyDelta -> "Yearly Change";
        };
    }
}
