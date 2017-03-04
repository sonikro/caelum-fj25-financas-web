package br.com.caelum.financas.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class ValorInvalidoException extends RuntimeException{
	public ValorInvalidoException(String message)
	{
		super(message);
	}
}
