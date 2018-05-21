package com.amano.springBoot.service;

import com.amano.springBoot.util.GeneralException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;


@Service
public class ValidationService {

    public void checkBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> msgs = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                msgs.add(error.getDefaultMessage());
            }
            throw new GeneralException(1, String.join(", ", msgs));
        }
    }

    public void checkBindingResult(BindingResult bindingResult, int validationErrorCode) {
        if (bindingResult.hasErrors()) {
            List<String> msgs = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                msgs.add(error.getDefaultMessage());
            }
            throw new GeneralException(1, String.join(", ", msgs));
        }
    }

}
