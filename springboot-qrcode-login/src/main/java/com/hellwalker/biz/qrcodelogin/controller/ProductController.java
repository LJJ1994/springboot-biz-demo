package com.hellwalker.biz.qrcodelogin.controller;

import com.hellwalker.biz.qrcodelogin.model.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private static final CopyOnWriteArrayList<Product> productList = new CopyOnWriteArrayList<>();

    static {
        Product p1 = new Product();
        p1.setId(1);
        p1.setPrice(new BigDecimal(10000));
        p1.setName("mate 60 pro");

        Product p2 = new Product();
        p2.setId(2);
        p2.setPrice(new BigDecimal(12000));
        p2.setName("iphone 15 pro max");

        Product p3 = new Product();
        p3.setId(3);
        p3.setPrice(new BigDecimal(11000));
        p3.setName("s24 ultra");

        productList.add(p1);
        productList.add(p2);
        productList.add(p3);
    }

    @GetMapping("/findall")
    public List<Product> findAll() {
        return productList;
    }

    @GetMapping("/find_by_id")
    public Product findById(@RequestParam("id") Integer id) {
        Optional<Product> productOptional = productList.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
        return productOptional.orElse(null);
    }
}
