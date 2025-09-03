package tienda.exceptiones;

public class PersistenciaException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public PersistenciaException() {
		super();
	}

	public PersistenciaException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public PersistenciaException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public PersistenciaException(String message) {
		super(message);
		
	}

	public PersistenciaException(Throwable cause) {
		super(cause);
		
	}

}
