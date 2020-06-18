package it.uniroma3.siw.progetto.controller.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.progetto.controller.sessione.DatiSessione;
import it.uniroma3.siw.progetto.model.Credenziali;
import it.uniroma3.siw.progetto.service.CredenzialiService;



@Component
public class CredenzialiValidator implements Validator{

	@Autowired
	private CredenzialiService credenzialiService;

	@Autowired
	private DatiSessione datiSessione;

	@Override
	public void validate(Object obj, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "username", "obbligatorio");  //"nome" = campo di utente che deve controllare
		ValidationUtils.rejectIfEmpty(errors, "password", "obbligatorio");
		if(this.credenzialiService.esistonoCredenziali((Credenziali) obj)) {
			errors.rejectValue("username","duplicato");
		}
	}

	public void validateNuovo(Credenziali credenziali, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "username", "obbligatorio");  //"nome" = campo di utente che deve controllare
		ValidationUtils.rejectIfEmpty(errors, "password", "necessario");
		if(!credenziali.getUsername().equals(this.datiSessione.getCredenzialiLoggate().getUsername())) {
			if(this.credenzialiService.esistonoCredenziali(credenziali)) {
				errors.rejectValue("username","duplicato");
			}
		}
	}

	public void validateEsistente(Credenziali credenziali, Errors errors, List<Credenziali> credenzialiVisibili) {
		Credenziali credenzialiComplete = this.credenzialiService.trovaPerUsername(credenziali.getUsername());
		if(credenziali.getUsername().trim().isEmpty())
			errors.rejectValue("username","obbligatorio");
		else if(credenziali.getUsername().equals(this.datiSessione.getCredenzialiLoggate().getUsername())) {
			errors.rejectValue("username","impossibile");
		}
		else if(credenzialiVisibili.contains(credenzialiComplete)) {
			errors.rejectValue("username","giausato");
		}
		else if(!this.credenzialiService.esistonoCredenziali(credenziali))
			errors.rejectValue("username","inesistente");
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Credenziali.class.equals(clazz);
	}

}
