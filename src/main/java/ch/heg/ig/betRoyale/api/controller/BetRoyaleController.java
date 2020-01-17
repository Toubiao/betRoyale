package ch.heg.ig.betRoyale.api.controller;

import ch.heg.ig.betRoyale.api.dto.BlockChainDTO;
import ch.heg.ig.betRoyale.model.Bet;
import ch.heg.ig.betRoyale.model.Block;
import ch.heg.ig.betRoyale.model.Transaction;
import ch.heg.ig.betRoyale.model.User;
import ch.heg.ig.betRoyale.repository.BetRepository;
import ch.heg.ig.betRoyale.repository.UserRepository;
import ch.heg.ig.betRoyale.service.BetService;
import ch.heg.ig.betRoyale.service.BlockChainService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/")
@Api(tags = {"BetRoyale API"})
public class BetRoyaleController {

    @Autowired
    private BlockChainService blockChainService;
    @Autowired
    private BetService betService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BetRepository betRepository;

    @ApiOperation(value = "Add a bet, this operation will persist the bet in the db and create the transactions resulting from the bet. At end a block is mined ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added in the db and mined"),
            @ApiResponse(code = 500, message = "The bet was not persisted and not mined")
    })
    @PostMapping(value="/bet")
    public ResponseEntity addBet(@RequestBody @ApiParam(value = "Bet between two players", required = true) Bet bet) {
        if (betService.addBet(bet)){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ApiOperation(value = "Add a user, this operation will persist the user in the db")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added in the db")
    })
    @PostMapping(value="/user")
    public ResponseEntity<User> createUser(@Valid @RequestBody @ApiParam(value = "User to add in the DB", required = true) User user) {
       return ResponseEntity.ok(userRepository.save(user));
    }

    @ApiOperation(value = "This operation pertains to get the entire blockchain")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the blockchain")
    })
    @GetMapping("/chain")
    public ResponseEntity<BlockChainDTO> getChain() {
        return ResponseEntity.ok(blockChainService.chain());
    }

    @ApiOperation(value = "This operation pertains to get all users stored in the db")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the users")
    })
    @GetMapping(value="/user")
    public ResponseEntity<Iterable<User>> getUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @ApiOperation(value = "This operation pertains to get all bets stored in the db")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the bets")
    })
    @GetMapping(value="/bet")
    public ResponseEntity<Iterable<Bet>> getBets() {
        return ResponseEntity.ok(betRepository.findAll());
    }

    @ApiOperation(value = "This operation pertains to add a transaction for the next block mined. It's only for test purpose")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the users")
    })
    @PostMapping(value="/transactions")
    public String createTransaction(@RequestBody @ApiParam(value = "Transaction to add in the next block", required = true)Transaction transaction) {
        int id = blockChainService.createTransaction(transaction);
        return "Transaction will be added to Block {" + id + "}";
    }

    @ApiOperation(value = "This operation pertains mine a new block with all actual transactions. It's only for test purpose")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the users")
    })
    @PostMapping(value="/mine")
    public Block createBlock() {
        return blockChainService.mine();
    }

    @ApiOperation(value = "This operation pertains to get sample of bet data. The data aren't persisted  It's only for test purpose")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully returned the sample  data")
    })
    @GetMapping(value = "sampleBetData")
    public ResponseEntity<Iterable<Bet>> getBetDataTest(){
        List<User> users = new ArrayList<>(userRepository.findAll());

        List<Bet> bets = new ArrayList<>(3);
        bets.add(new Bet(users.get(1),users.get(2),50));
        bets.add(new Bet(users.get(3),users.get(1),40));
        bets.add(new Bet(users.get(4),users.get(2),100));
        return ResponseEntity.ok(bets);
    }

}
