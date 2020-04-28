package com.acme.mytrader.strategy;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.acme.mytrader.execution.ExecutionService;

@RunWith(MockitoJUnitRunner.class)
public class TradingStrategyTest {
	
	@InjectMocks
	TradingStrategy trade = new TradingStrategy();
	
	@Mock
	ExecutionService service;
	
	String stock = "IBM";
	
    @Test
    public void priceOutOfTradeLimits() {
    	    	
    	//when stock price is between MIN and MAX limits.
    	trade.priceUpdate(stock, 200);
    	
    	//verify whether buy/sell method is never called
    	verify(service,never()).buy(stock, 200, 10);
    	verify(service,never()).sell(stock, 200, 10);
    }
    
    @Test
    public void priceInBuyLimits() {
    	    	
    	//when stock price is less than or equal to MIN.
    	trade.priceUpdate(stock, 50);

    	//verify whether buy method is called
    	verify(service, times(1)).buy(stock, 50, 10);
    	
    	trade.priceUpdate(stock, 100);
    	verify(service, times(1)).buy(stock, 100, 10);
    }
    
    @Test
    public void priceInSellLimits() {
    	    	
    	//when stock price is greater than or equal to MAX.
    	trade.priceUpdate(stock, 600);
    	//verify whether sell method is called
    	verify(service, times(1)).sell(stock, 600, 10);
    	
    	trade.priceUpdate(stock, 500);
    	verify(service, times(1)).sell(stock, 500, 10);
    }
}
