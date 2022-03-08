package br.edu.ufcg.computacao.alumni.api.http;

import br.edu.ufcg.computacao.eureca.common.exceptions.CommonExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class AlumniExceptionToHttpErrorConditionTranslator extends CommonExceptionHandler {
}
