import java.util.*;

public class FastRead {
    public String fastRead(double maxBuyPrice, double minSellPrice, String marketState) {
        if (maxBuyPrice <= 0 || minSellPrice <= 0 || minSellPrice > 1000 || minSellPrice <= maxBuyPrice) {
            throw new IllegalArgumentException("Invalid parameters");
        } else {
            StringBuilder actions = new StringBuilder();
            List<Integer> skipIndexes = new ArrayList<>();
            skipIndexes.add(0);
            skipIndexes.add(12);

            int indexOfBook = marketState.indexOf("\"book\":\"");
            skipIndexes.add(indexOfBook);
            skipIndexes.add(indexOfBook + 7);

            int indexOfBids = marketState.indexOf(",\"bids\":[");
            skipIndexes.add(indexOfBids);
            skipIndexes.add(indexOfBids + 8);

            boolean keepReading = true;
            String beforeAsks = marketState.substring(0, marketState.indexOf(",\"asks\":["));
            int countOfPriceBids = (beforeAsks.length() -
                    beforeAsks.replace("\"price\":", "").length()) / "\"price\":".length();
            if (marketState.charAt(indexOfBids + 9) == '{') {
                int indexOfPriceBids;
                int indexOfVolumeBids = indexOfBids;
                for (int i = 0; i < countOfPriceBids; i++) {
                    if (keepReading) {
                        if (i == 0) {
                            indexOfPriceBids = marketState.indexOf("\"price\":", indexOfVolumeBids);
                        } else {
                            indexOfPriceBids = marketState.indexOf("{\"price\":", indexOfVolumeBids);
                        }

                        skipIndexes.add(indexOfPriceBids);
                        if (i == 0) {
                            skipIndexes.add(indexOfPriceBids + 7);
                        } else {
                            skipIndexes.add(indexOfPriceBids + 8);
                        }

                        indexOfVolumeBids = marketState.indexOf("\"volume\":", indexOfPriceBids);
                        if (i == 0 && minSellPrice <= Double.parseDouble(marketState.substring(indexOfPriceBids + 8, indexOfVolumeBids - 1))) {
                            keepReading = false;
                        } else if (i != 0 && minSellPrice <= Double.parseDouble(marketState.substring(indexOfPriceBids + 9, indexOfVolumeBids - 1))) {
                            keepReading = false;
                        }

                        skipIndexes.add(indexOfVolumeBids);
                        if (keepReading) {
                            skipIndexes.add(indexOfVolumeBids + 8);
                        }
                    }
                }
            }

            if (keepReading) {
                int indexOfAsks = marketState.indexOf(",\"asks\":[");
                skipIndexes.add(indexOfAsks);
                skipIndexes.add(indexOfAsks + 8);

                String afterAsks = marketState.substring(marketState.indexOf(",\"asks\":["), marketState.length() - 1);
                int countOfPriceAsks = (afterAsks.length() -
                        afterAsks.replace("\"price\":", "").length()) / "\"price\":".length();
                if (marketState.charAt(indexOfAsks + 9) == '{') {
                    int indexOfPriceAsks;
                    int indexOfVolumeAsks = indexOfAsks;
                    for (int i = 0; i < countOfPriceAsks; i++) {
                        if (keepReading) {
                            if (i == 0) {
                                indexOfPriceAsks = marketState.indexOf("\"price\":", indexOfVolumeAsks);
                            } else {
                                indexOfPriceAsks = marketState.indexOf("{\"price\":", indexOfVolumeAsks);
                            }

                            skipIndexes.add(indexOfPriceAsks);
                            if (i == 0) {
                                skipIndexes.add(indexOfPriceAsks + 7);
                            } else {
                                skipIndexes.add(indexOfPriceAsks + 8);
                            }

                            indexOfVolumeAsks = marketState.indexOf("\"volume\":", indexOfPriceAsks);
                            if (i == 0 && maxBuyPrice >= Double.parseDouble(marketState.substring(indexOfPriceAsks + 8, indexOfVolumeAsks - 1))) {
                                keepReading = false;
                            } else if (i != 0 && maxBuyPrice >= Double.parseDouble(marketState.substring(indexOfPriceAsks + 9, indexOfVolumeAsks - 1))) {
                                keepReading = false;
                            }

                            skipIndexes.add(indexOfVolumeAsks);
                            if (keepReading) {
                                skipIndexes.add(indexOfVolumeAsks + 8);
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < skipIndexes.size() - 1; i++) {
                if (i % 2 == 0) {
                    actions.append(" S").append(skipIndexes.get(i + 1) - skipIndexes.get(i) + 1);
                } else {
                    actions.append(" R").append(skipIndexes.get(i + 1) - skipIndexes.get(i) - 1);
                }
            }

            if (keepReading && skipIndexes.get(skipIndexes.size() - 1) != marketState.length()) {
                actions.append(" R").append(marketState.length() - skipIndexes.get(skipIndexes.size() - 1) - 2);
            }

            return actions.substring(1);
        }
    }
}