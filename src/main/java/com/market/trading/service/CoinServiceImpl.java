package com.market.trading.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.trading.model.Coin;
import com.market.trading.repository.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CoinServiceImpl implements CoinService {

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public List<Coin> getAllCoins(int page) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page=" + page;

        RestTemplate restTemplate = new RestTemplate();
        try {
            // Create HTTP Headers. Set proper content type if needed, headers like authorization tokens if the API requires authentication.
            HttpHeaders headers = new HttpHeaders();
            //Encapsulate Headers and Body
            HttpEntity<String> entity = new HttpEntity<>(headers);
            // Make the HTTP Request
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            // Parse the Response
            List<Coin> coinList = objectMapper.readValue(response.getBody(), new TypeReference<List<Coin>>(){});
            return coinList;

        }catch (HttpClientErrorException | HttpServerErrorException | JsonProcessingException e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String getMarketChart(String coinId, int days) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/"+coinId+"market_chart?vs_currency=usd&days="+days;

        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();

        }catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String getCoinDetail(String coinId) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/"+coinId;

        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            //Parse the Response to a JSON Tree
            JsonNode node = objectMapper.readTree(response.getBody());
            Coin coin = new Coin();
            coin.setId(node.get("id").asText());
            coin.setName(node.get("name").asText());
            coin.setSymbol(node.get("symbol").asText());
            coin.setImage(node.get("image").get("large").asText());

            JsonNode marketNode = node.get("market");
            coin.setCurrentPrice(marketNode.get("current_price").get("usd").decimalValue());
            coin.setMarketCap(marketNode.get("market_cap").get("usd").decimalValue());
            coin.setMarketCapRank(marketNode.get("market_cap_rank").asInt());
            coin.setTotalVolume(marketNode.get("total_volume").get("usd").decimalValue());
            coin.setHigh24h(marketNode.get("high_24h").get("usd").decimalValue());
            coin.setLow24h(marketNode.get("low_24h").get("usd").decimalValue());
            coin.setPriceChange24h(marketNode.get("price_change_24h").get("usd").decimalValue());
            coin.setPriceChangePercentage24h(marketNode.get("price_change_percentage_24h").get("usd").decimalValue());
            coin.setMarketCapChange24h(marketNode.get("market_cap_change_24h").get("usd").decimalValue());
            coin.setMarketCapChangePercentage24h(marketNode.get("market_cap_change_percentage_24h").get("usd").decimalValue());
            coin.setTotalSupply(marketNode.get("total_supply").get("usd").decimalValue());
            coinRepository.save(coin);
            return response.getBody();

        }catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Coin findCoinById(String coinId) throws Exception {
        Optional<Coin> coin = coinRepository.findById(coinId);
        if (coin.isEmpty()) {
            throw new Exception("Coin not found");
        }
        return coin.get();
    }

    @Override
    public String searchCoinByName(String coinName) throws Exception {
        String url = "https://api.coingecko.com/api/v3/search?query="+coinName;

        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();

        }catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String getTop50CoinsByMarketCap() throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/markets?order=market_cap_asc&vs_currency=usd&per_page=50&page=1";

        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();

        }catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String getTradingCoins() throws Exception {
        String url = "https://api.coingecko.com/api/v3/search/trading";

        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();

        }catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Exception(e.getMessage());
        }
    }
}
