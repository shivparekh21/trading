package com.market.trading.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.trading.model.Coin;
import com.market.trading.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coins")
public class CoinController {

    @Autowired
    private CoinService coinService;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping
    private ResponseEntity<List<Coin>> getCoinList(@RequestParam("page") int page) throws Exception {
        List<Coin> coins = coinService.getAllCoins(page);
        return new ResponseEntity<>(coins, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{coinId}/market-chart")
    private ResponseEntity<JsonNode> getMarketChart(@PathVariable("coinId") String coinId,
                                                      @RequestParam("days") int days) throws Exception {
        String marketChart = coinService.getMarketChart(coinId, days);
        JsonNode jsonNode = objectMapper.readTree(marketChart);  //Parses JSON into a JsonNode Tree Model.
        return new ResponseEntity<>(jsonNode, HttpStatus.ACCEPTED);
    }

    @GetMapping("/search-coin")
    private ResponseEntity<JsonNode> searchCoin(@RequestParam("search") String search) throws Exception {
        String coin = coinService.searchCoinByName(search);
        return new ResponseEntity<>(objectMapper.readTree(coin), HttpStatus.ACCEPTED);
    }

    @GetMapping("/top50")
    private ResponseEntity<JsonNode> getTop50() throws Exception {
        String coins = coinService.getTop50CoinsByMarketCap();
        return new ResponseEntity<>(objectMapper.readTree(coins), HttpStatus.ACCEPTED);
    }

    @GetMapping("/trading")
    private ResponseEntity<JsonNode> getTrading() throws Exception {
        String coins = coinService.getTradingCoins();
        return new ResponseEntity<>(objectMapper.readTree(coins), HttpStatus.ACCEPTED);
    }

    @GetMapping("/coin-details/{coinId}")
    private ResponseEntity<JsonNode> getCoinDetails(@PathVariable("coinId") String coinId) throws Exception {
        String coinDetails = coinService.getCoinDetail(coinId);
        return new ResponseEntity<>(objectMapper.readTree(coinDetails), HttpStatus.ACCEPTED);
    }



}
