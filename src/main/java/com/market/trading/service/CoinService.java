package com.market.trading.service;

import com.market.trading.model.Coin;

import java.util.List;

public interface CoinService {
    List<Coin> getAllCoins(int page) throws Exception;
    String getMarketChart(String coinId, int days) throws Exception;
    String getCoinDetail(String coinId) throws Exception;
    Coin findCoinById(String coinId) throws Exception;
    String searchCoinByName(String coinName) throws Exception;
    String getTop50CoinsByMarketCap() throws Exception;
    String getTradingCoins() throws Exception;
}
