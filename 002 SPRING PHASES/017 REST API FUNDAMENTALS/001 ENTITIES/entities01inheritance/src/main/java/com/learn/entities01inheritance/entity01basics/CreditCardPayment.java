package com.learn.entities01inheritance.entity01basics;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("CREDIT")
@Getter @Setter @NoArgsConstructor
public class CreditCardPayment extends  Payment{

    private String cardNumber;
    private String cardHolderName;
    private String cvv;
    private String expiryMonth;
    private String expiryYear;
}
