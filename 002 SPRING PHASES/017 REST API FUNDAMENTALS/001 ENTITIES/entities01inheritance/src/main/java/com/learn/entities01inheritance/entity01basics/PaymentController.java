package com.learn.entities01inheritance.entity01basics;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ap/single-table/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/seed")
    public ResponseEntity<String> seed(){
        return ResponseEntity.ok(paymentService.seedData());
    }

    //Creating payments

    @PostMapping("/credit-card")
    public ResponseEntity<CreditCardPayment> createCreditCard(@RequestBody CreditCardPayment payment){
        return ResponseEntity.ok(paymentService.createCreditCardPayment(payment));
    }

    @PostMapping("/bank-transfer")
    public ResponseEntity<BankTransferPayment> createBankTransfer(@RequestBody BankTransferPayment payment){
        return ResponseEntity.ok(paymentService.creditBankPayment(payment));
    }

    @PostMapping("/upi-payments")
    public ResponseEntity<UpiPayment> createUpiPayment(@RequestBody UpiPayment payment){
        return ResponseEntity.ok(paymentService.createUpiPayment(payment));
    }


    //Reading All Data
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable Long id){
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    public ResponseEntity<>

    public ResponseEntity<List<Payment>> getHighValuePayments(@PathVariable Double amount){
        return ResponseEntity.ok(paymentService.getHighValuePayments(amount));
    }

}

/**
 * ============================================================
 * ANNOTATION MASTER REFERENCE
 * ============================================================
 *
 * @RestController
 *   = @Controller + @ResponseBody
 *   Every method return value is auto-serialized to JSON.
 *   No need to annotate each method with @ResponseBody.
 *
 * @RequestMapping("/api/products")
 *   Sets the BASE PATH for all endpoints in this class.
 *   All method-level mappings are appended to this.
 *   Options:
 *     value      = URL path (alias: path)
 *     method     = HTTP method array (rarely needed at class level)
 *     produces   = "application/json" (what this controller returns)
 *     consumes   = "application/json" (what body it accepts)
 *     headers    = restrict by request header
 *     params     = restrict by query param presence
 *
 * @GetMapping, @PostMapping, @PutMapping, @PatchMapping, @DeleteMapping
 *   These are shortcuts for @RequestMapping(method = RequestMethod.XXX)
 *   Options on all of them:
 *     value/path = URL (appended to class-level mapping)
 *     produces   = restrict to returning certain content type
 *     consumes   = restrict to accepting certain content type
 *     headers    = require specific headers
 *     params     = require specific query params
 *     name       = logical name (rarely used)
 *
 * @PathVariable
 *   Extracts a value embedded IN the URL path.
 *   URL: /products/{id}  → @PathVariable Long id
 *   Options:
 *     value/name = the {placeholder} name (auto-matched if same name)
 *     required   = true by default; false makes it Optional<Long>
 *   Types: Long, Integer, String, UUID — Spring auto-converts
 *
 * @RequestParam
 *   Extracts a value from the QUERY STRING: /products?category=phones
 *   Options:
 *     value/name = query param key name
 *     required   = true (default) throws 400 if missing; false = optional
 *     defaultValue = fallback string if param absent (implies required=false)
 *   Types: String, Integer, Double, Boolean, List<String>
 *
 * @RequestBody
 *   Deserializes the HTTP request body (JSON) into a Java object.
 *   Options:
 *     required = true (default); false = body is optional
 *   Pair with @Valid to trigger Bean Validation on the object.
 *
 * ResponseEntity<T>
 *   Gives you FULL control over the HTTP response:
 *     - Status code (200, 201, 204, 400, 404, etc.)
 *     - Headers
 *     - Body
 *   Patterns:
 *     ResponseEntity.ok(body)                    → 200 with body
 *     ResponseEntity.status(201).body(obj)       → 201 with body
 *     ResponseEntity.noContent().build()         → 204 no body
 *     ResponseEntity.badRequest().body(msg)      → 400 with body
 *     ResponseEntity.notFound().build()          → 404 no body
 *     ResponseEntity.created(uri).body(obj)      → 201 with Location header
 *
 * ============================================================
 */
