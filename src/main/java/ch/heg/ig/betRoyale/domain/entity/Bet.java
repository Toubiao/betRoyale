package ch.heg.ig.betRoyale.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Bet {
    @Id
    private String id;
    private User player1;
    private User player2;
    private double amount;

    public Bet() {
    }

    public Bet(User player1, User player2, double amount) {
        this.player1 = player1;
        this.player2 = player2;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getPlayer1() {
        return player1;
    }

    public void setPlayer1(User player1) {
        this.player1 = player1;
    }

    public User getPlayer2() {
        return player2;
    }

    public void setPlayer2(User player2) {
        this.player2 = player2;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    class BetResult{
        private User winner;
        private final double amountRewards = amount * 2;

        public BetResult(User winner) {
            this.winner = winner;
        }

        public User getWinner() {
            return winner;
        }

        public double getAmountRewards() {
            return amountRewards;
        }
    }
}
