package top.jiangyixin.poseidon.core.exception;

/**
 * Poseidon Exception
 *
 * @author jiangyixin
 */
public class PoseidonException extends RuntimeException {

	public PoseidonException(String msg) {
		super(msg);
	}

	public PoseidonException(Throwable throwable) {
		super(throwable);
	}

	public PoseidonException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
}
