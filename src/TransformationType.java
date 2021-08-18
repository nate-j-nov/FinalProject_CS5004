/**
 * @author Nate Novak
 * CS5004 Summer 2021
 * Enum of transformation type. Indicates if the level data, monthly change, or yearly data
 * should be reported
 */
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
