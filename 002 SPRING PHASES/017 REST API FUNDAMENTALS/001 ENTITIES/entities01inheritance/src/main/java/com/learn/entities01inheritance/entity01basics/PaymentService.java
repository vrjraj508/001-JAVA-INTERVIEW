package com.learn.entities01inheritance.entity01basics;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    //Creating

    public CreditCardPayment createCreditCardPayment(CreditCardPayment payment){
        payment.setStatus("PENDING");
        return paymentRepository.save(payment);
    }

    public BankTransferPayment creditBankPayment(BankTransferPayment payment){
        payment.setStatus("PENDING");
        return paymentRepository.save(payment);
    }

    public UpiPayment createUpiPayment(UpiPayment payment){
        payment.setStatus("PENDING");
        return paymentRepository.save(payment);
    }

    // Reading

    public List<Payment> getAllPayments(){
        return paymentRepository.findAll();

    }

    public Payment getPaymentById(Long id){
        return  paymentRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Payment not found with provided id: "+id));
    }

    public List<Payment> getCreditCardsOnly(){
        return paymentRepository.findOnlyCreditCards();
    }

    public List<Payment> getBankTransfersOnly(){
        return  paymentRepository.findOnlyBankTransfers();
    }

    public List<Payment> getUpiPaymentsOnly(){
        return paymentRepository.findOnlyUpiPayments();
    }

    public List<Payment> getByStatus(String staus){
        return  paymentRepository.findByStatus(staus);
    }

    public List<Payment> getHighValuePayments(Double amount){
        return paymentRepository.findByAmountGreaterThan(amount);
    }

    public List<Payment> getPaymentByAmountBetween(Double min,Double max){
        return paymentRepository.findByAmountBetween(min,max);
    }

    //Update payment
    public Payment updateStatus(Long id,String status){
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Payment nt found with id: "+id));

        payment.setStatus(status);

        return paymentRepository.save(payment);
    }

    //Delete
    public String deletePayment(Long id){
        paymentRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Payment not found with id:"+id));

        paymentRepository.deleteById(id);

        return "Payment with id:"+id+"deleted successfully";
    }

    public String seedData() {
        CreditCardPayment cc1 = new CreditCardPayment();
        cc1.setAmount(5000.0);
        cc1.setCardNumber("4111111111111111");
        cc1.setCardHolderName("Rahul Sharma");
        cc1.setCvv("123");
        cc1.setExpiryMonth("12");
        cc1.setExpiryYear("2027");
        paymentRepository.save(cc1);

        CreditCardPayment cc2 = new CreditCardPayment();
        cc2.setAmount(12000.0);
        cc2.setCardNumber("5500005555555559");
        cc2.setCardHolderName("Amit Verma");
        cc2.setCvv("456");
        cc2.setExpiryMonth("06");
        cc2.setExpiryYear("2026");
        paymentRepository.save(cc2);

        BankTransferPayment bank1 = new BankTransferPayment();
        bank1.setAmount(25000.0);
        bank1.setAccountNumber("9876543210");
        bank1.setIfscCode("HDFC0001234");
        bank1.setBankName("HDFC Bank");
        bank1.setAccountHolderName("Priya Singh");
        paymentRepository.save(bank1);

        BankTransferPayment bank2 = new BankTransferPayment();
        bank2.setAmount(75000.0);
        bank2.setAccountNumber("1234567890");
        bank2.setIfscCode("SBIN0005678");
        bank2.setBankName("State Bank of India");
        bank2.setAccountHolderName("Neha Gupta");
        paymentRepository.save(bank2);

        UpiPayment upi1 = new UpiPayment();
        upi1.setAmount(1500.0);
        upi1.setUpiId("rahul@okaxis");
        upi1.setPhoneNumber("9999999999");
        upi1.setAppName("PhonePe");
        paymentRepository.save(upi1);

        UpiPayment upi2 = new UpiPayment();
        upi2.setAmount(3200.0);
        upi2.setUpiId("priya@oksbi");
        upi2.setPhoneNumber("8888888888");
        upi2.setAppName("Google Pay");
        paymentRepository.save(upi2);

        return "Seeded 6 payments: 2 credit card, 2 bank transfer, 2 UPI";
    }
}
