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
 *
 */
@Service
public class BetService {
    private BlockChainService blockChainService;
    private BetRepository betRepository;
    private UserRepository userRepository;
    private User oracleUser;
    Timer timer;
    long delay = 1000L * 60L * 2L;

    /**
     *
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
     *
     * @param bet
     * @return
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
     *
     * @param bet
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
