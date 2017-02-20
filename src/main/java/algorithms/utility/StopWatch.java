package algorithms.utility;

/**
 * Created by abw286 on 06.10.2016.
 */
public class StopWatch {
    private long timeStart;
    private long timeEnd;

    public StopWatch() {
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
