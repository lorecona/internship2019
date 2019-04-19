package Domain;

import java.time.LocalDate;

public class Card {
    private String type;
    private Float fee;
    private Float limit;
    private LocalDate expirationDate;
    private Float amount;

    public Card(String type, Float fee, Float limit, LocalDate expirationDate, Float amount) {
        this.type = type;
        this.fee = fee;
        this.limit = limit;
        this.expirationDate = expirationDate;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public Float getFee() {
        return fee;
    }

    public void setFee(Float fee) {
        this.fee = fee;
    }

    public Float getLimit(){
        return limit;
    }

    public void setLimit(Float limit) {
        this.limit = limit;
    }

    public LocalDate getExpirationDate(){
        return expirationDate;
    }

    public void setExpirationDate(LocalDate date){
        expirationDate = date;
    }

    public Float getAmount(){
        return this.amount;
    }

    public void setAmount(Float amount){
        this.amount = amount;
    }

    @Override
    public String toString() {
        return this.type;
    }
}

