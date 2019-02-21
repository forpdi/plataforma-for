package org.forpdi.core.exception;

public class RestoreException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public RestoreException(Throwable t) {
		super("Erro ao restaurar backup.", t);
	}

}
