package algorithms.utility;

/**
 * Stopwatch for Runtime test
 */
public class Stopwatch {
    private long timeStart;
    private long timeEnd;

    public Stopwatch() {
        timeStart = System.currentTimeMillis();
    }

    public void start() {
        timeStart = System.currentTimeMillis();
    }

    public String stop() {
        timeEnd = System.currentTimeMillis();
        return getEndTimeString();
    }

    public String getActualTimeString() {
        return runtimeToString(System.currentTimeMillis() - timeStart);
    }

    public long getActualTime() {
        return System.currentTimeMillis() - timeStart;
    }

    public String getEndTimeString() {
        return runtimeToString(timeEnd - timeStart);
    }

    public long getEndTime() {
        return timeEnd - timeStart;
    }

    private String runtimeToString(final long runtime) {
        String output = "error";
        if (runtime < 1000) {
            output = runtime + " ms";
        } else {
            if (runtime > 1000) {
                output = runtime / 1000 + " sec";
            }
            if (runtime > 1000 * 60) {
                output = runtime / 1000 * 60 + " min";
            }
        }
        return (output);
    }
}
