package it.uniroma3.siw.progetto.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.progetto.controller.sessione.DatiSessione;
import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.service.ProgettoService;

@Component
public class ProgettoValidator implements Validator{
	
	@Autowired
	private ProgettoService progettoService;
	
	@Autowired
	private DatiSessione datiSessione;
	
	@Override
	public void validate(Object obj, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "obbligatorio");
		
		if(this.progettoService.esisteProgettoNome((Progetto) obj, datiSessione.getUtenteLoggato()))
			errors.rejectValue("nome", "duplicato");
	}
	
	public void validateNuovo(Progetto progetto, Progetto progCorrente, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "obbligatorio");
		if(!progetto.getNome().equals(progCorrente.getNome())) {
			if(this.progettoService.esisteProgettoNome(progetto, datiSessione.getUtenteLoggato()))
				errors.rejectValue("nome", "duplicato");
		}
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Progetto.class.equals(clazz);
	}

	
	
}
