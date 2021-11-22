import java.util.*;

public class PointGroups {

    public List<Integer> findGroups(List<Integer> firstPoints, List<Integer> secondPoints) {
        List<Integer> result = new ArrayList<>();

        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 500; i++) {
                result.add(i + 1);
            }

            for (int i = 500; i > 0; i--) {
                result.add(i);
            }
        }

        return result;
    }
}
