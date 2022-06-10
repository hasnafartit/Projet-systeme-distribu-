package org.miaad.billingservice;

import org.miaad.billingservice.entities.Bill;
import org.miaad.billingservice.entities.ProductItem;
import org.miaad.billingservice.feign.CustomerRestClient;
import org.miaad.billingservice.feign.ProductItemRestClient;
import org.miaad.billingservice.model.Customer;
import org.miaad.billingservice.model.Product;
import org.miaad.billingservice.repository.BillRepository;
import org.miaad.billingservice.repository.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.PagedModel;

import java.util.Collection;
import java.util.Date;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(BillRepository billRepository,
                            ProductItemRepository productItemRespository,
                            CustomerRestClient customerRestClient,
                            ProductItemRestClient productItemRestClient){
        return args -> {
            Customer customer=customerRestClient.getCustomerById(1L);
            Bill bill1=billRepository.save(new Bill(null,new Date(),null,customer.getId(),null));
            PagedModel<Product> productPagedModel=productItemRestClient.pageProducts();
            productPagedModel.forEach(p->{
                ProductItem productItem=new ProductItem();
                productItem.setPrice(p.getPrice());
                productItem.setQuantity(1+new Random().nextInt(100));
                productItem.setBill(bill1);
                productItem.setProductID(p.getId());
                productItemRespository.save(productItem);
            });
        };
    }
}
