package ch.bfh.anuto.util.math;

public class MathUtils {

    private MathUtils() {
    }

    public static float square(float x) {
        return x * x;
    }

    public static float sgn(float x) {
        return (x < 0f) ? -1f : 1f;
    }
}
