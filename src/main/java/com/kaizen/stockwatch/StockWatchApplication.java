package com.kaizen.stockwatch;

//import javafx.concurrent.Service;
//import javafx.concurrent.Task;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
public class StockWatchApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(StockWatchApplication.class, args);
		//	StockService.analyse();
	}

}
