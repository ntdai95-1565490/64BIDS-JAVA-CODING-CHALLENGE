public class TimeValue {
    public double calculateTime(int days, int hours, int minutes) {
        if (days < 0 || days > 10000 || hours < 0 || hours >= 24
                || minutes < 0 || minutes >= 60) {
            throw new IllegalArgumentException("Invalid time values!");
        } else {
            double totalMinutes = ((double) days) * 24.0 * 60.0
                    + ((double) hours) * 60.0 + (double) minutes;
            return totalMinutes / (365.0 * 24.0 * 60.0);
        }
    }
}