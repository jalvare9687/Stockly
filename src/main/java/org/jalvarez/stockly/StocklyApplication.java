package org.jalvarez.stockly;

import org.jalvarez.stockly.location.Location;
import org.jalvarez.stockly.sales.MenuItem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StocklyApplication {

    public static void main(String[] args) {
        SpringApplication.run(StocklyApplication.class, args);
    }

}
