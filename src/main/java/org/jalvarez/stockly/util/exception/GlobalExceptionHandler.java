package org.jalvarez.stockly.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LocationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String locationNotFound(LocationNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(IngredientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String ingredientNotFound(IngredientNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(IngredientTypeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String ingredientTypeNotFound(IngredientTypeNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(MenuItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String menuItemNotFound(MenuItemNotFoundException e) { return e.getMessage(); }

    @ExceptionHandler(SaleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String saleNotFound(SaleNotFoundException e) { return e.getMessage(); }

    @ExceptionHandler(SupplierNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String supplierNotFound(SupplierNotFoundException e) { return e.getMessage(); }
}
