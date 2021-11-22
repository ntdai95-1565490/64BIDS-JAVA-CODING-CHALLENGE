import java.util.ArrayList;
import java.util.List;

public class FlatCallsAndPuts {
    public int calculateProfit(List<Integer> callStrikePrices, List<Integer> callVolumes,
                               List<Integer> putStrikePrices, List<Integer> putVolumes) {
        if (putVolumes.size() == 1) {
            int totalCallPay = Integer.MAX_VALUE;
            for (int i = 0; i < callVolumes.size(); i++) {
                if (callVolumes.get(i).equals(putVolumes.get(0)) && callStrikePrices.get(i) < totalCallPay) {
                    totalCallPay = callStrikePrices.get(i) * callVolumes.get(i);
                }
            }

            return Math.max(putStrikePrices.get(0) * putVolumes.get(0) - totalCallPay, 0);
        } else {
            int[][] callPriceVolume = new int[callStrikePrices.size()][];
            for (int i = 0; i < callStrikePrices.size(); i++) {
                int[] priceVolume = new int[2];
                priceVolume[0] = callStrikePrices.get(i);
                priceVolume[1] = callVolumes.get(i);
                callPriceVolume[i] = priceVolume;
            }
            List<int[]> totalCallPriceVolume = this.combinationGenerator(callPriceVolume);

            int[][] putPriceVolume = new int[putStrikePrices.size()][];
            for (int i = 0; i < putStrikePrices.size(); i++) {
                int[] priceVolume = new int[2];
                priceVolume[0] = putStrikePrices.get(i);
                priceVolume[1] = putVolumes.get(i);
                putPriceVolume[i] = priceVolume;
            }
            List<int[]> totalPutPriceVolume = this.combinationGenerator(putPriceVolume);

            int profit = 0;
            for (int[] callPV: totalCallPriceVolume) {
                for (int[] putPV: totalPutPriceVolume) {
                    if (putPV[1] - callPV[1] == 0 && (putPV[0] - callPV[0]) > profit) {
                        profit = putPV[0] - callPV[0];
                    }
                }
            }

            return profit;
        }
    }

    private List<int[]> combinationGenerator(int[][] priceVolume) {
        List<List<int[]>> allCombinations = new ArrayList<>();
        for (int len = 1; len <= priceVolume.length; len++) {
            List<List<int[]>> combinations = this.generate(priceVolume, len);
            allCombinations.addAll(combinations);
        }

        List<int[]> totalPriceVolumeCombinations = new ArrayList<>();
        for (List<int[]> combination: allCombinations) {
            int[] totalPriceVolume = new int[2];
            for (int[] value: combination) {
                totalPriceVolume[0] += value[0] * value[1];
                totalPriceVolume[1] += value[1];
            }
            totalPriceVolumeCombinations.add(totalPriceVolume);
        }

        return totalPriceVolumeCombinations;
    }

    private List<List<int[]>> generate(int[][] priceVolume, int len) {
        List<List<int[]>> result = new ArrayList<>();
        int n = priceVolume.length;
        int N = (int) Math.pow(2, n);
        for (int i = 0; i < N; i++) {
            String code = Integer.toBinaryString(N | i).substring(1);
            int counter = 0;
            List<int[]> combination = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if (code.charAt(j) == '1') {
                    combination.add(priceVolume[j]);
                    counter++;
                }
            }
            if (counter == len) {
                result.add(combination);
            }
        }
        return result;
    }
}
