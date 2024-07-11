package no.sparebank1.turbo;

public class SnapshotException extends RuntimeException {
	public SnapshotException(String msg) {
		super(msg);
	}

	public SnapshotException(String msg, Exception e) {
		super(msg, e);
	}
}
