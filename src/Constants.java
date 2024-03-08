import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Constants {
    public static final int DEFAULT_TOTAL_SUBREGIONS = 10;
    public static final int DEFAULT_THREAD_COUNT = 200;
    public static final int DEFAULT_DARTS_PER_THREAD = 100000;

    public static final String UNIFORM_SAMPLING = "UNIFORM";
    public static final String STRATIFIED_SAMPLING = "STRATIFIED";
    public static final String DEFAULT_SAMPLING = UNIFORM_SAMPLING;
    public static final Set<String> VALID_SAMPLING_METHODS = new HashSet<>(Arrays.asList(UNIFORM_SAMPLING, STRATIFIED_SAMPLING));
}
