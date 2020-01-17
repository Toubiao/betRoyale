package ch.heg.ig.betRoyale.api.controller;

import ch.heg.ig.betRoyale.api.dto.BlockChainDTO;
import ch.heg.ig.betRoyale.model.Block;
import ch.heg.ig.betRoyale.model.Transaction;
import ch.heg.ig.betRoyale.service.BlockChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BetRoyaleController {
    @Autowired
    private BlockChainService service;

    @RequestMapping(value="/transactions", method= RequestMethod.POST)
    public String createTransaction(@RequestBody Transaction transaction) {
        int id = service.createTransaction(transaction);
        return "Transaction will be added to Block {" + id + "}";
    }

    @RequestMapping(value="/mine", method=RequestMethod.POST)
    public Block createBlock() {
        return service.mine();
    }

    @RequestMapping("/chain")
    public BlockChainDTO getChain() {
        return service.chain();
    }

}
