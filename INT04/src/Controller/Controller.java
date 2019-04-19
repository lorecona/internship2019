package Controller;

import Domain.Card;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Controller {

    private Float price;
    private Float tva;
    private List<Card> cards;
    private LocalDate date;

    public Controller(Float price, Float tva, List<Card> cards, LocalDate date) {
        this.price = price;
        this.tva = tva;
        this.cards = cards;
        this.date = date;
    }


    private void eliminateNonValid(List<Card> list, LocalDate date){
        list.removeIf(c -> c.getExpirationDate().isBefore(date) || c.getExpirationDate().isEqual(date));
    }

    private void eliminateEmpty(List<Card> list) {
        list.removeIf(c -> c.getAmount() == 0);
    }


    private Integer selectNext(List<Card> sortedFee, LocalDate iterateDate){

        for(Card card : sortedFee){

            if(iterateDate.plusDays(1).isEqual(card.getExpirationDate()))
                return sortedFee.indexOf(card);//if there is a card that expires the next day, we select that one
        }
        return 0; // select the one with the smallest fee

    }

    public Map<Card, Pair<Float, Float>> getCardsCost() {

        Map<Card, Pair<Float, Float>> costs = new HashMap<>();

        this.eliminateNonValid(this.cards, this.date);//we work only with valid cards

        this.cards = cards.stream().sorted(Comparator.comparing(Card::getExpirationDate)).collect(Collectors.toList());
        Card baseCard = cards.get(cards.size()-1);//we choose as base card the one that expires last, in case we have a large sum to pay and a small withdrawal limit
        cards.remove(baseCard);
        costs.put(baseCard, new Pair<Float, Float>(baseCard.getAmount() - baseCard.getAmount()/((100 + this.tva)/100), this.price * (baseCard.getFee()/100))); //we update its tva, and fee (considering that at the end it'll contain the total sum)

        List<Card> sortedFee = cards.stream().sorted(Comparator.comparing(Card::getFee)).collect(Collectors.toList());

        Float paid_sum = (float)0;
        LocalDate iterateDate = this.date;

        paid_sum += baseCard.getAmount();

        //we go as long as the total sum isn't yet paid, and as long as the we have valid cards
        while(paid_sum < this.price && iterateDate.isBefore(cards.get(cards.size()-1).getExpirationDate())) {

            Card nextCard = sortedFee.get(selectNext(sortedFee, iterateDate)); //we choose the next card
            sortedFee.remove(nextCard);

            Float sum_to_extract;

            Float fee, tva;
            if(nextCard.getLimit() > this.price - paid_sum + this.price * (baseCard.getFee()/100)){ //if the limit is larger than what we need + the fee from the basecard that will be charged at the end, we extract only the sum we need

                sum_to_extract = this.price - paid_sum + this.price * (baseCard.getFee()/100);
                fee = sum_to_extract * (nextCard.getFee()/100);
                tva = sum_to_extract - fee - (sum_to_extract/((100 + this.tva)/100));
                paid_sum += sum_to_extract; // here we assume we transferred sum_to_extract + fee, so after the transfer we get the actual amount we needed without the fee

            } else { //else we extract the most we can

                sum_to_extract = nextCard.getLimit();
                fee = sum_to_extract * (nextCard.getFee()/100);
                tva = sum_to_extract - fee - (sum_to_extract/((100 + this.tva)/100));
                paid_sum += sum_to_extract - fee;
            }


            Pair<Float, Float> new_cost = new Pair<Float, Float>(costs.containsKey(nextCard) ? costs.get(nextCard).getKey() + tva : tva, costs.containsKey(nextCard) ? costs.get(nextCard).getValue() + fee : fee);
            costs.put(nextCard, new_cost);

            //after we update the costs we use sortedDate to retain the changes
            cards.get(cards.indexOf(nextCard)).setAmount(nextCard.getAmount() - sum_to_extract);

            if(paid_sum < price && sortedFee.isEmpty()) { //if sortedFee is empty, a day has passed without paying the whole sum

                iterateDate = iterateDate.plusDays(1);//we move to the next day
                sortedFee = cards.stream().sorted(Comparator.comparing(Card::getFee)).collect(Collectors.toList());//and update sortedFee
                eliminateEmpty(sortedFee);
                eliminateNonValid(sortedFee, iterateDate);
            }
        }
        //this is to fill the costs of the cards that weren't used to the default values
        fillTheRest(costs);
        return costs;

    }

    private void fillTheRest(Map<Card, Pair<Float, Float>> costs){

        for(Card card : this.cards)
            if(!costs.containsKey(card)) {
                costs.put(card, new Pair<>(null, (float) 0));
            }
    }
}
