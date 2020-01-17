package ch.heg.ig.betRoyale.api.controller;

import ch.heg.ig.betRoyale.api.dto.BlockChainDTO;
import ch.heg.ig.betRoyale.api.dto.BlockChainDTOV1;
import ch.heg.ig.betRoyale.model.Block;
import ch.heg.ig.betRoyale.model.BlockV1;
import ch.heg.ig.betRoyale.model.Transaction;
import ch.heg.ig.betRoyale.service.BlockChainServiceV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class BetRoyaleControllerV1 {
    @Autowired
    private BlockChainServiceV1 service;

    @RequestMapping(value="/transactions", method= RequestMethod.POST)
    public String createTransaction(@RequestBody Transaction transaction) {
        int id = service.createTransaction(transaction);
        return "Transaction will be added to Block {" + id + "}";
    }

    @RequestMapping(value="/mine", method=RequestMethod.POST)
    public BlockV1 createBlock() {
        return service.mine();
    }

    @RequestMapping("/chain")
    public BlockChainDTOV1 getChain() {
        return service.chain();
    }

}
