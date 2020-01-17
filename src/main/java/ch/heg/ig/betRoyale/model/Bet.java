package ch.heg.ig.betRoyale.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Bet is a entity object persisted in the mongodb
 * Actually the result of the Bet is not managed
 * We have try to do a sub-class but doesn't work as expected and we have no time to do another implementation
 */
@Document
public class Bet {
    @Id
    private String id;
    private User player1;
    private User player2;
    private double amount;

    public Bet() {
    }

    /**
     * Constructor
     * @param player1 part of the bet
     * @param player2 part of the bet
     * @param amount the amount betted by user (is the same for the two (logic...))
     */
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


    /**
     * Sub class used to store the winner of the bet
     *  Unfornately not worked as expected and not used for the moment
     */
    public class BetResult{
        private User winner;
        private final double amountRewards = amount * 2;

        /**
         * Constructor
         * @param winner player who have win the bet
         */
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
