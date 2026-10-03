package com.learn.entities01inheritance.entity01basics;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("UPI")
@Getter @Setter @NoArgsConstructor
public class UpiPayment extends Payment {

    private String upiId;
    private String phoneNumber;
    private String appName;
}


