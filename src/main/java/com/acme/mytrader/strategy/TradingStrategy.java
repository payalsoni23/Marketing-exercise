package com.acme.mytrader.strategy;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.PriceListener;
import com.acme.mytrader.price.PriceSource;
//import com.acme.mytrader.price.PriceSource;

/**
 * <pre>
 * User Story: As a trader I want to be able to monitor stock prices such
 * that when they breach a trigger level orders can be executed automatically
 * </pre>
 * 
 * Assumptions: MIN - least price value to trigger buy operation. Any price
 * value less than or equal to this value will trigger buy operation. MAX -
 * least price value to trigger buy operation. Any price value greater or equal
 * to this value will trigger sell operation. newUnits - units bought/sold in
 * single buy/sell operation STOCK - name of stock whose price will be
 * monitored. doTrade() - method to change stock price from 0.0 to 1000, with
 * increase rate of 100.5
 * 
 * @author Payal
 */
public class TradingStrategy implements PriceListener, PriceSource {

	ExecutionService service = new ExecutionService() {

		@Override
		public void sell(String security, double price, int volume) {
			System.out.println("Sell method called - " + volume + " units of " + security + " sold at " + price);
		}

		@Override
		public void buy(String security, double price, int volume) {
			System.out.println("Buy method called - " + volume + " units of " + security + " bought at " + price);
		}
	};

	public static final double MIN = 100;
	public static final double MAX = 500;
	public static int newUnits = 10;
	public static String STOCK = "IBM";

	@Override
	public void priceUpdate(String security, double price) {
		if (price <= MIN) {
			// trigger buy method
			service.buy(security, price, newUnits);
		} else if (price >= MAX) {
			// trigger sell method
			service.sell(security, price, newUnits);
		}
	}

	PriceListener listener;

	@Override
	public void removePriceListener(PriceListener listener) {
		this.listener = null;
		System.out.println("Price listener removed.");
	}

	@Override
	public void addPriceListener(PriceListener listener) {
		this.listener = listener;
		System.out.println("Price listener added.");
	}

	// Asynchronous task to trigger operation
	public void doTrade() throws InterruptedException {

		// An Async task always executes in new thread
		Thread thread = new Thread(() -> {
			double price = 0.0;
			// perform any operation
			while (price < 1000) {
				System.out.println("Stock price - " + price);
				// check if listener is registered.
				if (listener != null)
					listener.priceUpdate(STOCK, price);

				price += 100.5;
			}
		});

		thread.start();
		thread.join();
	}

	public static void main(String args[]) throws InterruptedException {

		PriceSource priceSource = new TradingStrategy();
		priceSource.addPriceListener((PriceListener) priceSource);
		((TradingStrategy) priceSource).doTrade();
		priceSource.removePriceListener((PriceListener) priceSource);
	}

}
