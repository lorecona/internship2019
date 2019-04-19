import Controller.Controller;
import Domain.Card;
import javafx.util.Pair;

import java.awt.geom.FlatteningPathIterator;
import java.time.LocalDate;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        LocalDate date = LocalDate.of(2019, 3, 19);
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("SILVER", (float)0.2, (float)2000, LocalDate.of(2020, 5, 23), (float)4000));
        cards.add(new Card("GOLD", (float)0.1, (float)2000, LocalDate.of(2018, 8, 15), (float)2000));
        cards.add(new Card("PLATINUM", (float)0.3, (float)2000, LocalDate.of(2019, 3, 20), (float)3000));
        cards.add(new Card("IRIDIUM", (float)0.2, (float)2000, LocalDate.of(2020, 6, 23), (float)5000));
        cards.add(new Card("BRONZE", (float)0.5, (float)2000, LocalDate.of(2019, 7, 15), (float)2500));
        cards.add(new Card("PREMIUM", (float)0.15, (float)2000, LocalDate.of(2019, 8, 20), (float)2000));

        Controller ctrl = new Controller((float)10000, (float)19, cards, date);
        Map<Card, Pair<Float, Float>> costs = ctrl.getCardsCost();
        System.out.println(prettyPrint(costs));

    }

    public static String prettyPrint(Map<Card, Pair<Float, Float>> costs){
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<Card, Pair<Float, Float>>> iterator = costs.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Card, Pair<Float, Float>> entry = iterator.next();
            sb.append("[" + entry.getKey() + "]");
            sb.append(" -> ").append("[");
            sb.append("tva = " +entry.getValue().getKey()).append(",").append(" fee = ").append(entry.getValue().getValue());
            sb.append("]");
            if (iterator.hasNext()) {
                sb.append(" \n");
            }
        }
        return sb.toString();

    }
}
