package ch.heg.ig.betRoyale.service;

import ch.heg.ig.betRoyale.model.Bet;
import ch.heg.ig.betRoyale.model.Transaction;
import ch.heg.ig.betRoyale.model.User;
import ch.heg.ig.betRoyale.repository.BetRepository;
import ch.heg.ig.betRoyale.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Service class used for manage the bet in the application
 */
@Service
public class BetService {

    private BlockChainService blockChainService;
    private BetRepository betRepository;
    private UserRepository userRepository;

    /**
     * This remplace the smart contract
     * this is a poor alternative
     */
    private User oracleUser;

    private Timer timer;

    /**
     * Represent two minutes
     * for test purpose, normally they will represent 45 minutes it's represent the delay of the brawls star api refresh
     */
    long delay = 1000L * 60L * 2L;

    /**
     * Constructor is auto injected by spring boot
     * @param blockChainService
     * @param betRepository
     * @param userRepository
     */
    @Autowired
    public BetService(BlockChainService blockChainService,BetRepository betRepository,UserRepository userRepository) {
        this.blockChainService = blockChainService;
        this.betRepository = betRepository;
        this.userRepository = userRepository;
        oracleUser = this.userRepository.findUserByPseudoEquals("Oracle");
        timer  = new Timer("BetVerifierTimer");
    }

    /**
     * This method will handle a bet and transform into transactions and mine a block
     * @param bet bet between two players
     * @return the boolean if the block is mined
     */
    public boolean addBet(Bet bet){
        betRepository.save(bet);

        boolean result;
        Transaction t1 = new Transaction(bet.getPlayer1().getId(),oracleUser.getId(),bet.getAmount());
        Transaction t2 = new Transaction(bet.getPlayer2().getId(),oracleUser.getId(),bet.getAmount());
        result = blockChainService.createTransaction(t1) == blockChainService.createTransaction(t2);

        timer.schedule(controlBetTask(bet),delay);
        return blockChainService.mine() != null && result;
    }

    /**
     * Normally this method will call the brawls stars api, to get the winner of the bet
     * but in this case it's only a randomBoolean
     * this create the transaction and mine the block
     * @param bet to control
     * @return
     */
    private TimerTask controlBetTask(Bet bet){
        return new TimerTask() {
            public void run() {
                Random random = new Random();
                Transaction t1;
                double reward = bet.getAmount() * 2;
                if(random.nextBoolean()){
                    t1 = new Transaction(oracleUser.getId(),bet.getPlayer1().getId(),reward);
                }else{
                    t1 = new Transaction(oracleUser.getId(),bet.getPlayer2().getId(),reward);
                }
                blockChainService.createTransaction(t1);
                blockChainService.mine();
            }
        };
    }
}
