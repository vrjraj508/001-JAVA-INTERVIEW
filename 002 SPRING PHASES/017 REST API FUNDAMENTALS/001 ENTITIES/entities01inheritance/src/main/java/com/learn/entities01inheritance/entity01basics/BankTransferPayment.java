package com.learn.entities01inheritance.entity01basics;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("BANK")
@Getter @Setter @NoArgsConstructor
public class BankTransferPayment extends Payment{

    private String accountNumber;
    private String ifscCode;
    private String bankName;
    private String accountHolderName;
}


