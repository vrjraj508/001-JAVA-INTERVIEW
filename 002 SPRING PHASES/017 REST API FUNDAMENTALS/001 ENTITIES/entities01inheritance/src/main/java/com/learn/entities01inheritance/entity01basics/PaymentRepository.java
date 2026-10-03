package com.learn.entities01inheritance.entity01basics;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {

    List<CreditCardPayment> findAllCreditCardPaymentBy();
    List<BankTransferPayment> findAllBankTransferPaymentBy();
    List<UpiPayment> findAllUpiPaymentBy();

    @Query("select p from Payment p where type(p) = CreditCardPayment ")
    List<Payment> findOnlyCreditCards();

    @Query("select p from Payment p where type(p) = BankTransferPayment ")
    List<Payment> findOnlyBankTransfers();

    @Query("select p from Payment p where type(p) = UpiPayment ")
    List<Payment> findOnlyUpiPayments();

    @Query("select p from Payment p where p.status = : status")
    List<Payment> findByStatus(String status);

    @Query("select p from Payment p where p.amount > :amount")
    List<Payment> findByAmountGreaterThan(Double amount);

    @Query("select p from Payment p where p.amount between :min and :max")
    List<Payment> findByAmountBetween(@Param("min") Double min,@Param("max") Double max);

}
